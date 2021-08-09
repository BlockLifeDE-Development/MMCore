package com.gestankbratwurst.core.mmcore.resourcepack.distribution;

import com.gestankbratwurst.core.mmcore.MMCore;
import com.gestankbratwurst.core.mmcore.data.config.MMCoreConfiguration;
import java.io.File;
import java.io.IOException;
import org.bukkit.plugin.java.JavaPlugin;

public class ResourcepackManager {

  public static final long SERVER_TIMESTAMP = System.currentTimeMillis();
  public static final String RESOURCEPACK_FILE_NAME = "serverpack.zip";

  private final int port;
  private final String host;
  private final File pack;
  private final String fileLocation = "/" + ResourcepackManager.SERVER_TIMESTAMP + "/" + ResourcepackManager.RESOURCEPACK_FILE_NAME;

  private PlayerBoundResourcePackServer server;

  public ResourcepackManager() {
    final MMCore plugin = JavaPlugin.getPlugin(MMCore.class);
    final MMCoreConfiguration configuration = MMCoreConfiguration.get();
    this.port = configuration.getResourcePackServerPort();
    this.host = configuration.getResourcePackServerIP();
    final File stampFolder = new File(plugin.getDataFolder() + File.separator + ResourcepackManager.SERVER_TIMESTAMP);
    this.pack = new File(stampFolder, ResourcepackManager.RESOURCEPACK_FILE_NAME);
    this.startServer(plugin);
  }

  public String getResourceHash() {
    return this.server.getFileHashChecksum();
  }

  public String getDownloadURL() {
    return this.server.getDownloadURL();
  }

  public void shutdown() {
    this.server.terminate();
  }

  private void startServer(final JavaPlugin plugin) {
    plugin.getLogger().info("Starting async HTTP ResourcePackServer");
    try {
      this.server = new PlayerBoundResourcePackServer(this.host, this.port, this.fileLocation, plugin.getLogger(), this.pack);
      this.server.start();
      plugin.getLogger().info("Successfully started the HTTP ResourcePackServer");
    } catch (final IOException ex) {
      plugin.getLogger().severe("Failed to start HTTP ResourcePackServer!");
      ex.printStackTrace();
    }
  }

}
