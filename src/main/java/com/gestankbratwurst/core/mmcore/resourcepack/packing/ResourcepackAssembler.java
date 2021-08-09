package com.gestankbratwurst.core.mmcore.resourcepack.packing;

import com.gestankbratwurst.core.mmcore.MMCore;
import com.gestankbratwurst.core.mmcore.resourcepack.distribution.ResourcepackManager;
import com.gestankbratwurst.core.mmcore.resourcepack.skins.BlockModel;
import com.gestankbratwurst.core.mmcore.resourcepack.skins.FontMeta;
import com.gestankbratwurst.core.mmcore.resourcepack.skins.ModelData;
import com.gestankbratwurst.core.mmcore.resourcepack.skins.TextureModel;
import com.gestankbratwurst.core.mmcore.skinclient.PlayerSkinManager;
import com.gestankbratwurst.core.mmcore.util.io.ResourceCopy;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of avarioncore and was created at the 24.11.2019
 *
 * avarioncore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ResourcepackAssembler {

  private static final int META_FORMAT = 5;
  // TODO pack description
  private static final String META_DESC = "- -";

  public ResourcepackAssembler(final JavaPlugin plugin, final AssetLibrary assetLibrary) {
    this.plugin = plugin;
    this.clearPluginFolder(plugin);
    this.playerSkinManager = MMCore.getPlayerSkinManager();
    this.packFolderSet = new ObjectLinkedOpenHashSet<>();
    this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    this.assetLibrary = assetLibrary;

    this.skinBackupFile = new File(plugin.getDataFolder(), "skindata.json");
    this.skinCacheFile = new File(plugin.getDataFolder(), "skinchache.json");

    this.packFolderSet.add(this.packFolder = new File(plugin.getDataFolder() + File.separator + "resourcepack"));
    this.packFolderSet.add(this.assetFolder = new File(this.packFolder + File.separator + "assets"));
    this.packFolderSet.add(this.minecraftFolder = new File(this.assetFolder + File.separator + "minecraft"));
    this.packFolderSet.add(this.blockStateFolder = new File(this.minecraftFolder + File.separator + "blockstates"));
    this.packFolderSet.add(this.fontFolder = new File(this.minecraftFolder + File.separator + "font"));
    this.packFolderSet.add(this.langFolder = new File(this.minecraftFolder + File.separator + "lang"));
    this.packFolderSet.add(this.modelsFolder = new File(this.minecraftFolder + File.separator + "models"));
    this.packFolderSet.add(this.itemModelFolder = new File(this.modelsFolder + File.separator + "item"));
    this.packFolderSet.add(this.blockModelFolder = new File(this.modelsFolder + File.separator + "block"));
    this.packFolderSet.add(this.particlesFolder = new File(this.minecraftFolder + File.separator + "particles"));
    this.packFolderSet.add(this.soundsFolder = new File(this.minecraftFolder + File.separator + "sounds" + File.separator + "custom"));
    this.packFolderSet.add(this.texturesFolder = new File(this.minecraftFolder + File.separator + "textures"));
    this.mcmetaFile = new File(this.packFolder, "pack.mcmeta");
    this.soundsFile = new File(this.minecraftFolder, "sounds.json");
    final File stampFolder = new File(plugin.getDataFolder() + File.separator + ResourcepackManager.SERVER_TIMESTAMP);
    stampFolder.mkdirs();

    this.resourceZipFile = new File(stampFolder, "serverpack.zip");
  }

  private final ObjectLinkedOpenHashSet<File> packFolderSet;
  private final JavaPlugin plugin;
  private final PlayerSkinManager playerSkinManager;
  private final Gson gson;
  @Getter
  private final AssetLibrary assetLibrary;

  private final File skinBackupFile;
  private final File skinCacheFile;
  private final File resourceZipFile;

  private final File packFolder;
  private final File assetFolder;
  private final File minecraftFolder;
  private final File blockStateFolder;
  private final File fontFolder;
  private final File langFolder;
  private final File modelsFolder;
  private final File itemModelFolder;
  private final File blockModelFolder;
  private final File particlesFolder;
  private final File soundsFolder;
  private final File texturesFolder;

  private final File mcmetaFile;
  private final File soundsFile;

  private void clearPluginFolder(final JavaPlugin plugin) {
    for (final File folder : Objects.requireNonNull(plugin.getDataFolder().listFiles())) {
      if (folder.isDirectory()) {
        final String folderName = folder.getName();
        if (folderName.contains("temp") || folderName.matches("[0-9]+")) {
          try {
            FileUtils.deleteDirectory(folder);
          } catch (final IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  public void zipResourcepack() throws IOException {

    this.setupBaseFiles();

    this.createMetaFile();

    this.compileModels();

    final FileOutputStream fos = new FileOutputStream(this.resourceZipFile);
    final ZipOutputStream zos = new ZipOutputStream(fos);
    this.zipFile(this.assetFolder, this.assetFolder.getName(), zos);
    this.zipFile(this.mcmetaFile, this.mcmetaFile.getName(), zos);

    zos.close();
    fos.close();
  }

  private void createSoundFiles(final File tempFolder) {
    final File tempSoundFolder = new File(tempFolder + File.separator + "sounds");
    if (!tempSoundFolder.exists()) {
      return;
    }
    final JsonObject jsonObject = new JsonObject();
    try {
      for (final File soundTempFile : Objects.requireNonNull(tempSoundFolder.listFiles())) {
        final String fileName = soundTempFile.getName().toLowerCase();
        FileUtils.copyFile(soundTempFile, new File(this.soundsFolder, fileName));
        final JsonObject soundJson = new JsonObject();
        final JsonArray soundList = new JsonArray();
        final String fileStrip = fileName.contains(".") ? fileName.split("\\.")[0] : fileName;
        soundList.add("custom/" + fileStrip);
        soundJson.add("sounds", soundList);
        jsonObject.add("custom." + fileStrip, soundJson);
      }
    } catch (final IOException e) {
      e.printStackTrace();
    }
    if (!jsonObject.entrySet().isEmpty()) {
      final String data = this.gson.toJson(jsonObject);
      try {
        Files.writeString(this.soundsFile.toPath(), data);
      } catch (final IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void compileModels() throws IOException {
    final Path temp = Files.createTempDirectory(this.plugin.getDataFolder().toPath(), "temp_rp");
    final File tempFolder = temp.toFile();
    final JsonObject fontJson = new JsonObject();
    final JsonArray providerArray = new JsonArray();
    final char fontIndex = (char) 0x2F00;

    // Textures
    this.exportData(tempFolder);

    // Blocks
    this.loadBlockModels(tempFolder);

    // Items
    this.loadItemModels(tempFolder, fontIndex, providerArray);

    // Skins
    this.createSkinData();

    // TTF
    // TODO fix ttf
    this.createTrueTypeFont(tempFolder, providerArray, fontJson);

    // Json models
    this.createModelJsonFiles(this.assetLibrary);

    // Sounds
    this.createSoundFiles(tempFolder);

    FileUtils.deleteDirectory(tempFolder);
  }


  private void createMetaFile() throws IOException {
    final OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(this.mcmetaFile));
    osw.write(new PackMeta(ResourcepackAssembler.META_FORMAT, ResourcepackAssembler.META_DESC).getAsJsonString());
    osw.close();
  }


  private void exportData(final File tempFolder) {
    try {
      final ResourceCopy copy = new ResourceCopy();
      final JarFile jf = new JarFile(MMCore.class.getProtectionDomain().getCodeSource().getLocation().getPath());
      copy.copyResourceDirectory(jf, "resourcepack", tempFolder);
    } catch (final IOException ex) {
      ex.printStackTrace();
    }
  }


  private void loadBlockModels(final File tempFolder) throws IOException {
    final File modelBlockFolder = new File(tempFolder, "blockstates");
    final Map<String, JsonObject> blockstateJsonMap = Maps.newHashMap();
    for (final BlockModel blockModel : BlockModel.values()) {
      final String vanillaName = blockModel.getBaseMaterial().getKey().getKey();
      final File modelBlockImage = new File(modelBlockFolder, blockModel.toString() + ".png");
      if (!modelBlockImage.exists()) {
        this.plugin.getLogger().severe("Could not find image of " + blockModel.toString());
        continue;
      }

      final JsonObject stateJson;
      if (blockstateJsonMap.containsKey(vanillaName)) {
        stateJson = blockstateJsonMap.get(vanillaName);
      } else {
        stateJson = new JsonObject();
        blockstateJsonMap.put(vanillaName, stateJson);
      }
      final JsonObject variantsJson;
      if (stateJson.has("variants")) {
        variantsJson = stateJson.get("variants").getAsJsonObject();
      } else {
        variantsJson = new JsonObject();
      }
      final JsonObject modelJson = new JsonObject();
      modelJson.addProperty("model", "block/" + blockModel.toString().toLowerCase());
      variantsJson.add(blockModel.getBlockStateApplicant(), modelJson);
      stateJson.add("variants", variantsJson);

      final JsonObject customModelJson = new JsonObject();
      customModelJson.addProperty("parent", "block/cube_all");
      final JsonObject textureJson = new JsonObject();
      textureJson.addProperty("all", blockModel.toString().toLowerCase());
      customModelJson.add("textures", textureJson);
      FileUtils.copyFile(modelBlockImage, new File(this.texturesFolder, blockModel.toString().toLowerCase() + ".png"));
      final OutputStreamWriter osw = new OutputStreamWriter(
          new FileOutputStream(new File(this.blockModelFolder, blockModel.toString().toLowerCase() + ".json")), StandardCharsets.UTF_8);
      osw.write(this.gson.toJson(customModelJson));
      osw.close();
    }

    for (final Entry<String, JsonObject> entry : blockstateJsonMap.entrySet()) {
      final File stateFile = new File(this.blockStateFolder, entry.getKey() + ".json");
      final OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(stateFile), StandardCharsets.UTF_8);
      osw.write(this.gson.toJson(entry.getValue()));
      osw.close();
    }
  }


  private void loadItemModels(final File tempFolder, char fontIndex, final JsonArray providerArray) throws IOException {
    final File textureTempFolder = new File(tempFolder + File.separator + "textures");
    final File imageCache = new File(this.plugin.getDataFolder() + File.separator + "imagecache");
    if (!imageCache.exists()) {
      imageCache.mkdirs();
    }

    // Load static models
    for (final File subFolder : textureTempFolder.listFiles()) {
      for (final File icon : subFolder.listFiles()) {
        final TextureModel textureModel = TextureModel.valueOf(icon.getName().replace(".png", ""));
        final File cachedImage = new File(imageCache, textureModel.toString() + ".png");
        FileUtils.copyFile(icon, cachedImage);
        textureModel.setLinkedImageFile(cachedImage);
        final boolean isBlock = textureModel.getBaseMaterial().isBlock();
        final String nmsName = textureModel.getBaseMaterial().getKey().getKey();
        final File resourceTextureFolder = new File(this.texturesFolder + File.separator + nmsName);
        resourceTextureFolder.mkdirs();
        final File imageFile = new File(resourceTextureFolder, "" + textureModel.getModelID() + ".png");
        FileUtils.copyFile(icon, imageFile);
        final File modNMFolder = isBlock ? this.blockModelFolder : this.itemModelFolder;
        final File resourceModelFolder = new File(modNMFolder + File.separator + nmsName);
        resourceModelFolder.mkdirs();
        final File resourceModelFile = new File(resourceModelFolder, "" + textureModel.getModelID() + ".json");

        final String iconPath = "minecraft:" + nmsName + "/" + textureModel.getModelID() + ".png";
        textureModel.getBoxedFontChar().value = fontIndex;
        final FontMeta fontMeta = textureModel.getFontMeta();
        final JsonObject fontProvider = new JsonObject();
        fontProvider.addProperty("file", iconPath);
        final JsonArray charArray = new JsonArray();
        charArray.add(fontIndex);
        fontProvider.add("chars", charArray);
        fontProvider.addProperty("height", fontMeta.getHeight());
        fontProvider.addProperty("ascent", fontMeta.getAscent());
        fontProvider.addProperty("type", fontMeta.getType());
        providerArray.add(fontProvider);
        fontIndex++;

        final ModelData modelData = textureModel.getModelData();
        if (modelData != null) {
          final JsonObject modelJson = new JsonObject();
          modelJson.addProperty("parent", textureModel.getModelData().getModelParent());
          final JsonObject textureJson = new JsonObject();
          textureJson.addProperty("layer0", nmsName + "/" + textureModel.getModelID());
          textureJson.addProperty("particles", nmsName + "/" + textureModel.getModelID());
          modelJson.add("textures", textureJson);

          final JsonObject elementsJson = textureModel.getModelData().getElementsJson();
          if (elementsJson != null) {
            modelJson.add("elements", elementsJson);
          }

          final JsonObject displayJson = textureModel.getModelData().getDisplayJson();
          if (displayJson != null) {
            modelJson.add("display", displayJson);
          }

          final String data = this.gson.toJson(modelJson);
          final OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(resourceModelFile));
          osw.write(data);
          osw.close();
        }
      }
    }

    // Load custom models
    final File customtextureFolder = new File(this.texturesFolder + File.separator + "custom"); // minecraft/textures/custom
    final File tempCustomModelFolder = new File(tempFolder + File.separator + "custommodel"); // intern
    final File rawTextureFolder = new File(tempFolder + File.separator + "raw"); // intern
    final File customTempModelFolder = new File(tempCustomModelFolder + File.separator + "models"); // intern
    final File customTempTextureFolder = new File(tempCustomModelFolder + File.separator + "textures"); // intern
    // itemModelFolder

    // --- --- --- Copy all textures --- --- ---
    if (!customtextureFolder.exists()) {
      customtextureFolder.mkdirs();
    }
    for (final File textureFile : customTempTextureFolder.listFiles()) {
      FileUtils.copyFile(textureFile, new File(customtextureFolder, textureFile.getName()));
    }

    // --- --- --- Copy all model files --- --- ---
    final File customModelFolder = new File(this.itemModelFolder + File.separator + "custom"); // minecraft/models/items/custom
    if (!customModelFolder.exists()) {
      customModelFolder.mkdirs();
    }

    for (final File cModelFileFolder : customTempModelFolder.listFiles()) {
      for (final File cModelFile : cModelFileFolder.listFiles()) {
        TextureModel.valueOf(cModelFile.getName().replace(".json", ""));
        FileUtils.copyFile(cModelFile, new File(customModelFolder, cModelFile.getName().toLowerCase()));
      }
    }

//    for (File raw : rawTextureFolder.listFiles()) {
//      FileUtils.copyFile(raw, new File(itemModelFolder, raw.getName().toLowerCase()));
//    }
  }


  private void createSkinData() {
    for (final TextureModel textureModel : TextureModel.values()) {
      if (textureModel.isHeadSkinEnabled()) {
        final String name = "BLOCK_LIFE_" + textureModel.toString();
        final File imageFile = textureModel.getLinkedImageFile();
        this.playerSkinManager.requestNamedSkin(name, imageFile, !textureModel.isPlayerSkinModel(), textureModel::setSkin);
      }
    }
    this.playerSkinManager.persist();
  }


  private void createTrueTypeFont(final File tempFolder, final JsonArray providerArray, final JsonObject fontJson) throws IOException {
    final JsonObject ttfProvider = new JsonObject();
    ttfProvider.addProperty("type", "ttf");
    ttfProvider.addProperty("size", 10);
    ttfProvider.addProperty("oversample", 4.5);
    final JsonArray shiftArray = new JsonArray();
    shiftArray.add(0);
    shiftArray.add(1);
    ttfProvider.add("shift", shiftArray);
    ttfProvider.addProperty("file", "minecraft:uniformcenter.ttf");
    // FIXME add ttf back
    // providerArray.add(ttfProvider);

    final File ttfFile = new File(tempFolder, "uniformcenter.ttf");
    FileUtils.copyFile(ttfFile, new File(this.fontFolder, "uniformcenter.ttf"));

    final File tempUnicodeFolder = new File(tempFolder + File.separator + "fontdata");
    final File unicodeFolder = new File(this.texturesFolder + File.separator + "font");
    unicodeFolder.mkdirs();
    for (final File unicodeImage : tempUnicodeFolder.listFiles()) {
      FileUtils.copyFile(unicodeImage, new File(unicodeFolder, unicodeImage.getName()));
    }

    fontJson.add("providers", providerArray);
    final File fontFile = new File(this.fontFolder, "default.json");
    final OutputStreamWriter oswFont = new OutputStreamWriter(new FileOutputStream(fontFile), StandardCharsets.UTF_8);
    oswFont.write(this.gson.toJson(fontJson));
    oswFont.close();
  }


  private void createModelJsonFiles(final AssetLibrary assetLibrary) throws IOException {
    final Map<Material, JsonObject> cachedJsons = Maps.newHashMap();

    for (final TextureModel textureModel : TextureModel.values()) {
      final String nmsName = textureModel.getBaseMaterial().getKey().getKey();
      final boolean isBlock = textureModel.getBaseMaterial().isBlock();
      final JsonArray overrideArray;
      final JsonObject modelObject;

      if (textureModel.getModelData() == null) {
        continue;
      }

      if (!cachedJsons.containsKey(textureModel.getBaseMaterial())) {
        modelObject = new JsonObject();
        modelObject.addProperty("parent", assetLibrary.getAssetModelParent(nmsName));
        if (!isBlock) {
          final JsonObject textureObject = new JsonObject();
          textureObject.addProperty("layer0", assetLibrary.getAssetModelLayer0(nmsName));
          modelObject.add("textures", textureObject);
        }

        overrideArray = new JsonArray();
        modelObject.add("overrides", overrideArray);

      } else {
        modelObject = cachedJsons.get(textureModel.getBaseMaterial());
        overrideArray = modelObject.get("overrides").getAsJsonArray();
      }

      final JsonObject overrideObject = new JsonObject();
      final String customModelName;
      if (textureModel.isCustomModelDataEnabled()) {
        customModelName = assetLibrary.getAssetModelLayer0(nmsName).split("/")[0] + "/custom/" + textureModel.toString().toLowerCase();
      } else {
        customModelName = assetLibrary.getAssetModelLayer0(nmsName).split("/")[0] + "/" + nmsName + "/" + textureModel.getModelID();
      }
      overrideObject.addProperty("model", customModelName);
      final JsonObject predicateObject = new JsonObject();
      predicateObject.addProperty("custom_model_data", textureModel.getModelID());
      overrideObject.add("predicate", predicateObject);
      overrideArray.add(overrideObject);

      modelObject.add("overrides", overrideArray);
      cachedJsons.put(textureModel.getBaseMaterial(), modelObject);
    }

    for (final TextureModel textureModel : TextureModel.values()) {
      if (textureModel.getModelData() != null) {
        final File modelFolder = textureModel.getBaseMaterial().isBlock() ? this.blockModelFolder : this.itemModelFolder;
        final File modelFile = new File(modelFolder, textureModel.getBaseMaterial().getKey().getKey() + ".json");
        final OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(modelFile));
        osw.write(this.gson.toJson(cachedJsons.get(textureModel.getBaseMaterial())));
        osw.close();
      }
    }
  }


  private void setupBaseFiles() throws IOException {
    FileUtils.deleteDirectory(this.assetFolder);
    for (final File folder : this.packFolderSet) {
      folder.mkdirs();
    }
  }


  private void zipFile(final File fileToZip, final String fileName, final ZipOutputStream zipOut) throws IOException {
    if (fileToZip.isHidden()) {
      return;
    }
    if (fileToZip.isDirectory()) {
      if (fileName.endsWith("/")) {
        zipOut.putNextEntry(new ZipEntry(fileName));
        zipOut.closeEntry();
      } else {
        zipOut.putNextEntry(new ZipEntry(fileName + "/"));
        zipOut.closeEntry();
      }
      final File[] children = fileToZip.listFiles();
      for (final File childFile : children) {
        this.zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
      }
      return;
    }
    final FileInputStream fis = new FileInputStream(fileToZip);
    final ZipEntry zipEntry = new ZipEntry(fileName);
    zipOut.putNextEntry(zipEntry);
    final byte[] bytes = new byte[1024];
    int length;
    while ((length = fis.read(bytes)) >= 0) {
      zipOut.write(bytes, 0, length);
    }
    fis.close();
  }


  private void await(final long ms) {
    try {
      Thread.sleep(ms);
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
  }


}
