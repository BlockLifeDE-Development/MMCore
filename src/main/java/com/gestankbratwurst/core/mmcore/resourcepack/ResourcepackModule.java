package com.gestankbratwurst.core.mmcore.resourcepack;

import com.gestankbratwurst.core.mmcore.MMCore;
import com.gestankbratwurst.core.mmcore.resourcepack.distribution.ResourcePackListener;
import com.gestankbratwurst.core.mmcore.resourcepack.distribution.ResourcepackManager;
import com.gestankbratwurst.core.mmcore.resourcepack.packing.AssetLibrary;
import com.gestankbratwurst.core.mmcore.resourcepack.packing.ResourcepackAssembler;
import org.bukkit.Bukkit;

public class ResourcepackModule {

  private ResourcepackManager resourcepackManager;
  private AssetLibrary assetLibrary;

  public void enable(final MMCore plugin) {
    this.assetLibrary = new AssetLibrary(plugin);
    plugin.getLogger().info("Blocking main thread. Waiting for resourcepack server to start.");
    try {
      new ResourcepackAssembler(plugin, this.assetLibrary).zipResourcepack();
    } catch (final Exception e) {
      e.printStackTrace();
    }

    try {
      this.resourcepackManager = new ResourcepackManager();
    } catch (final Exception e) {
      e.printStackTrace();
      Bukkit.shutdown();
    }

    Bukkit.getPluginManager().registerEvents(new ResourcePackListener(this.resourcepackManager), plugin);
  }

  public void disable(final MMCore plugin) {
    if (this.resourcepackManager == null) {
      System.out.println("§c ResourcepackManager is null.");
    } else {
      this.resourcepackManager.shutdown();
    }
    this.assetLibrary.saveCache();
  }
}
