package com.gestankbratwurst.core.mmcore.skinclient;

import com.gestankbratwurst.core.mmcore.MMCore;
import com.gestankbratwurst.core.mmcore.data.config.MMCoreConfiguration;
import com.gestankbratwurst.core.mmcore.data.model.AgnosticDataDomain;
import com.gestankbratwurst.core.mmcore.skinclient.mineskin.MineskinClient;
import com.gestankbratwurst.core.mmcore.skinclient.mineskin.SkinOptions;
import com.gestankbratwurst.core.mmcore.skinclient.mineskin.Variant;
import com.gestankbratwurst.core.mmcore.skinclient.mineskin.Visibility;
import com.gestankbratwurst.core.mmcore.skinclient.mineskin.data.Skin;
import com.google.common.base.Preconditions;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import org.bukkit.plugin.java.JavaPlugin;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of AvarionCore and was created at the 11.12.2019
 *
 * AvarionCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class PlayerSkinManager {

  protected PlayerSkinManager() {
    final MMCoreConfiguration configuration = MMCoreConfiguration.get();
    this.mineskinClient = new MineskinClient(configuration.getMineSkinClientUserAgent(), configuration.getMineSkinClientAPIToken());
    this.skinMap = new HashMap<>();
    this.namedSkinIds = new HashMap<>();
  }

  private final HashMap<String, Integer> namedSkinIds;
  private final HashMap<Integer, Skin> skinMap;
  private final transient MineskinClient mineskinClient;

  public static PlayerSkinManager load() {
    final AgnosticDataDomain<String> skinDomain = MMCore.getDataManager().getGlobalDataDomain();
    skinDomain.getOrCreateDataHolder("SkinCache").join();
    final PlayerSkinManager loadedManager = skinDomain.getDataHolder("SkinCache").join().getData(PlayerSkinManager.class);
    return loadedManager == null ? new PlayerSkinManager() : loadedManager;
  }

  public void persist() {
    final AgnosticDataDomain<String> skinDomain = MMCore.getDataManager().getGlobalDataDomain();
    skinDomain.getOrCreateDataHolder("SkinCache").join();
    skinDomain.applyToDataHolder("SkinCache", dataHolder -> {
      dataHolder.putData(PlayerSkinManager.class, this);
      return dataHolder;
    });
  }

  public void requestNamedSkin(final String skinName, final File imageFile, final boolean scale, final Consumer<Skin> skinConsumer) {
    Integer id = this.namedSkinIds.get(skinName);

    if (id == null) {
      if (scale) {
        this.uploadAndScaleHeadImage(imageFile, skinName, uploadedSkin -> this.namedSkinIds.put(skinName, uploadedSkin.id));
      } else {
        this.uploadImage(imageFile, skinName, uploadedSkin -> this.namedSkinIds.put(skinName, uploadedSkin.id));
      }
    }

    id = this.namedSkinIds.get(skinName);

    if (id == null) {
      return;
    }
    this.requestSkin(id, skinConsumer);
  }

  public void requestSkin(final int id, final Consumer<Skin> skinConsumer) {
    Skin skin = this.skinMap.get(id);

    final long unixWeek = TimeUnit.DAYS.toMillis(7);
    final long unixDay = unixWeek / 7;

    if (skin == null) {
      JavaPlugin.getPlugin(MMCore.class).getLogger().info("§7Downloading Skin with ID [" + id + "] from Mineskin.org");
      skin = this.mineskinClient.getId(id).join();
      skin.downloadTimestamp = System.currentTimeMillis();
      this.skinMap.put(skin.id, skin);
    } else if (skin.downloadTimestamp + unixWeek + ThreadLocalRandom.current().nextLong(-unixDay, unixDay) < System.currentTimeMillis()) {
      JavaPlugin.getPlugin(MMCore.class).getLogger().info("Skin with ID [" + id + "] has old cache data. Downloading.");
      skin = this.mineskinClient.getId(id).join();
      skin.downloadTimestamp = System.currentTimeMillis();
      this.skinMap.put(skin.id, skin);
    }

    JavaPlugin.getPlugin(MMCore.class).getLogger().info("Getting Skin with ID [" + id + "] from skin cache.");
    skinConsumer.accept(skin);
  }

  public void uploadImage(final File imageFile, final String name, final Consumer<Skin> skinConsumer) {
    // final BufferedImage image = ImageIO.read(imageFile);

    // Preconditions.checkArgument(image.getWidth() == 64 && image.getHeight() == 64);

    skinConsumer.accept(this.mineskinClient.generateUpload(imageFile, SkinOptions.create(name, Variant.AUTO, Visibility.PRIVATE)).join());
  }

  public void uploadHeadImage(final File imageFile, final String name, final Consumer<Skin> skinConsumer) {
    final BufferedImage headImage;
    try {
      headImage = ImageIO.read(imageFile);
    } catch (final IOException e) {
      e.printStackTrace();
      return;
    }
    Preconditions.checkArgument(headImage.getWidth() == 8 && headImage.getHeight() == 8);
    final BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
    final int[] xHeadOffsets = new int[]{8, 16, 0, 8, 16, 24};
    final int[] yHeadOffsets = new int[]{0, 0, 8, 8, 8, 8, 8};
    for (int hx = 0; hx < 8; hx++) {
      for (int hy = 0; hy < 8; hy++) {
        final int rgb = headImage.getRGB(hx, hy);
        for (int index = 0; index < 6; index++) {
          final int x = xHeadOffsets[index] + hx;
          final int y = yHeadOffsets[index] + hy;
          image.setRGB(x, y, rgb);
        }
      }
    }
    final File uploadFile = new File(imageFile.getParent(), imageFile.getName().replace(".png", "") + "_scaled.png");
    try {
      ImageIO.write(image, "png", uploadFile);
    } catch (final IOException e) {
      e.printStackTrace();
    }
    this.uploadImage(uploadFile, name, skinConsumer);
  }

  public void uploadAndScaleHeadImage(final File imageFile, final String name, final Consumer<Skin> skinConsumer) {
    final BufferedImage headImage;
    try {
      headImage = ImageIO.read(imageFile);
    } catch (final IOException e) {
      e.printStackTrace();
      return;
    }
    final double widthScale = 8D / (double) headImage.getWidth();
    final double heightScale = 8D / (double) headImage.getWidth();
    final BufferedImage image = this.scale(headImage, widthScale, heightScale);
    final File uploadFile = new File(imageFile.getParent(), imageFile.getName().replace(".png", "") + "_8.png");
    try {
      ImageIO.write(image, "png", uploadFile);
    } catch (final IOException e) {
      e.printStackTrace();
    }
    this.uploadHeadImage(uploadFile, name, skinConsumer);
  }

  private BufferedImage scale(final BufferedImage before, final double scaleWidth, final double scaleHeight) {
    final int w = before.getWidth();
    final int h = before.getHeight();
    BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    final AffineTransform at = new AffineTransform();
    at.scale(scaleWidth, scaleHeight);
    final AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
    after = scaleOp.filter(before, after);
    return after.getSubimage(0, 0, (int) (w * scaleWidth + 0.5D), ((int) (h * scaleHeight + 0.5D)));
  }

}
