package com.gestankbratwurst.core.mmcore;

import co.aikar.commands.PaperCommandManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.gestankbratwurst.core.mmcore.actionbar.ActionBarManager;
import com.gestankbratwurst.core.mmcore.protocol.holograms.impl.HologramManager;
import com.gestankbratwurst.core.mmcore.scoreboard.ScoreboardManager;
import com.gestankbratwurst.core.mmcore.skinclient.PlayerSkinManager;
import com.gestankbratwurst.core.mmcore.tablist.TabListManager;
import com.gestankbratwurst.core.mmcore.tablist.implementation.EmptyTabList;
import com.gestankbratwurst.core.mmcore.tracking.ChunkTracker;
import com.gestankbratwurst.core.mmcore.tracking.EntityTracker;
import com.gestankbratwurst.core.mmcore.util.common.BukkitTime;
import com.gestankbratwurst.core.mmcore.util.common.ChatInput;
import com.gestankbratwurst.core.mmcore.util.common.NamespaceFactory;
import com.gestankbratwurst.core.mmcore.util.common.UtilItem;
import com.gestankbratwurst.core.mmcore.util.common.UtilMobs;
import com.gestankbratwurst.core.mmcore.util.common.UtilPlayer;
import com.gestankbratwurst.core.mmcore.util.items.display.ItemDisplayCompiler;
import com.gestankbratwurst.core.mmcore.util.json.BoundingBoxSerializer;
import com.gestankbratwurst.core.mmcore.util.json.GsonProvider;
import com.gestankbratwurst.core.mmcore.util.json.ItemStackArraySerializer;
import com.gestankbratwurst.core.mmcore.util.json.ItemStackSerializer;
import com.gestankbratwurst.core.mmcore.util.json.LocationSerializer;
import com.gestankbratwurst.core.mmcore.util.json.MultimapSerializer;
import com.google.common.collect.Multimap;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;

public final class MMCore extends JavaPlugin {

  @Getter(AccessLevel.MODULE)
  private static MMCore instance;
  @Getter
  private static ActionBarManager actionBarManager;
  @Getter
  private static HologramManager hologramManager;
  @Getter
  private static TabListManager tabListManager;
  @Getter
  private static ItemDisplayCompiler displayCompiler;
  @Getter
  private static ProtocolManager protocolManager;
  @Getter
  private static PlayerSkinManager playerSkinManager;
  @Getter
  private static ScoreboardManager scoreboardManager;
  @Getter
  private static PaperCommandManager paperCommandManager;

  @Override
  public void onEnable() {
    instance = this;
    this.getLogger().info("Creating manager instances.");

    paperCommandManager = new PaperCommandManager(this);
    protocolManager = ProtocolLibrary.getProtocolManager();
    actionBarManager = new ActionBarManager(this);
    hologramManager = new HologramManager(this);
    tabListManager = new TabListManager(this, player -> new EmptyTabList());
    displayCompiler = new ItemDisplayCompiler(this);
    playerSkinManager = new PlayerSkinManager();
    // scoreboardManager = new ScoreboardAPI(this).getBoardManager();

    this.registerDefaultGsonSerializer();
    this.initUtils();

    TutorialStuff.enable();
  }

  private void registerDefaultGsonSerializer() {
    this.getLogger().info("Registering default gson serializer.");
    GsonProvider.register(ItemStack.class, new ItemStackSerializer());
    GsonProvider.register(CraftItemStack.class, new ItemStackSerializer());
    GsonProvider.register(ItemStack[].class, new ItemStackArraySerializer());
    GsonProvider.register(CraftItemStack[].class, new ItemStackArraySerializer());
    GsonProvider.register(Location.class, new LocationSerializer());
    GsonProvider.register(Multimap.class, new MultimapSerializer());
    GsonProvider.register(BoundingBox.class, new BoundingBoxSerializer());
  }

  private void initUtils() {
    this.getLogger().info("Initiating utility classes.");
    ProtocolLibrary.getProtocolManager().addPacketListener(displayCompiler);
    BukkitTime.start(this);
    ChatInput.init(this);
    NamespaceFactory.init(this);
    UtilPlayer.init(this);
    UtilMobs.init(this);
    UtilItem.init(this);
    Bukkit.getPluginManager().registerEvents(new ChunkTracker(this), this);
    Bukkit.getPluginManager().registerEvents(new EntityTracker(this), this);
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
