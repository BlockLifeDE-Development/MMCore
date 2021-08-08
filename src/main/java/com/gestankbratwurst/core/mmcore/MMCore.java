package com.gestankbratwurst.core.mmcore;

import co.aikar.commands.PaperCommandManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.gestankbratwurst.core.mmcore.actionbar.ActionBarManager;
import com.gestankbratwurst.core.mmcore.data.access.DataAccess;
import com.gestankbratwurst.core.mmcore.data.config.MMCoreConfigManager;
import com.gestankbratwurst.core.mmcore.data.config.MMCoreConfiguration;
import com.gestankbratwurst.core.mmcore.data.model.DataManager;
import com.gestankbratwurst.core.mmcore.data.mongodb.MongoIO;
import com.gestankbratwurst.core.mmcore.protocol.holograms.impl.HologramManager;
import com.gestankbratwurst.core.mmcore.skinclient.PlayerSkinManager;
import com.gestankbratwurst.core.mmcore.tablist.TabListManager;
import com.gestankbratwurst.core.mmcore.tablist.implementation.EmptyTabList;
import com.gestankbratwurst.core.mmcore.tokenclick.TokenActionManager;
import com.gestankbratwurst.core.mmcore.tracking.ChunkTracker;
import com.gestankbratwurst.core.mmcore.tracking.EntityTracker;
import com.gestankbratwurst.core.mmcore.util.common.BukkitTime;
import com.gestankbratwurst.core.mmcore.util.common.ChatInput;
import com.gestankbratwurst.core.mmcore.util.common.NamespaceFactory;
import com.gestankbratwurst.core.mmcore.util.common.UtilItem;
import com.gestankbratwurst.core.mmcore.util.common.UtilMobs;
import com.gestankbratwurst.core.mmcore.util.common.UtilPlayer;
import com.gestankbratwurst.core.mmcore.util.items.display.ItemDisplayCompiler;
import com.gestankbratwurst.core.mmcore.util.json.JacksonProvider;
import com.gestankbratwurst.core.mmcore.util.json.commons.ItemStackDeserializer;
import com.gestankbratwurst.core.mmcore.util.json.commons.ItemStackSerializer;
import com.gestankbratwurst.core.mmcore.util.json.commons.LocationDeserializer;
import com.gestankbratwurst.core.mmcore.util.json.commons.LocationSerializer;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

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
  // @Getter
  // private static ScoreboardManager scoreboardManager;
  @Getter
  private static PaperCommandManager paperCommandManager;
  @Getter
  private static TokenActionManager tokenActionManager;
  @Getter
  private static DataManager dataManager;
  @Getter
  private static RedissonClient redissonClient;
  @Getter
  private static MongoIO mongoIO;

  @Override
  public void onEnable() {
    instance = this;
    this.getLogger().info("Loading configuration data.");
    MMCoreConfigManager.init(this);

    this.getLogger().info("Creating Redisson client.");
    final Config config = new Config();
    config.useSingleServer().setAddress(MMCoreConfiguration.get().getRedisAddress());
    config.setCodec(JacksonProvider.getCodec());
    redissonClient = Redisson.create(config);

    this.getLogger().info("Creating MongoDB client.");
    mongoIO = new MongoIO();

    DataAccess.init(redissonClient, MMCoreConfiguration.get().getServerType());

    this.getLogger().info("Creating manager instances.");

    paperCommandManager = new PaperCommandManager(this);
    protocolManager = ProtocolLibrary.getProtocolManager();
    dataManager = new DataManager(redissonClient, mongoIO);
    actionBarManager = new ActionBarManager(this);
    hologramManager = new HologramManager(this);
    tabListManager = new TabListManager(this, player -> new EmptyTabList());
    displayCompiler = new ItemDisplayCompiler(this);
    playerSkinManager = new PlayerSkinManager();
    tokenActionManager = new TokenActionManager();
    // scoreboardManager = new ScoreboardAPI(this).getBoardManager();

    this.registerDefaultJacksonSerializer();
    this.initUtils();

    TutorialStuff.enable();
  }

  private void registerDefaultJacksonSerializer() {
    this.getLogger().info("Registering default jackson (de)serializer.");
    JacksonProvider.register(Location.class, new LocationSerializer(), new LocationDeserializer(), true);
    JacksonProvider.register(CraftItemStack.class, new ItemStackSerializer(), new ItemStackDeserializer(), false);
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
    dataManager.expireAllDomains();
  }
}
