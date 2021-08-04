package com.gestankbratwurst.core.mmcore.inventories.guis;

import com.gestankbratwurst.core.mmcore.MMCore;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class GUIManager {

  protected static GUIManager instance;

  protected static GUIManager getInstance() {
    if (instance == null) {
      instance = new GUIManager();
      final GUIListener listener = new GUIListener(instance);
      Bukkit.getPluginManager().registerEvents(listener, JavaPlugin.getPlugin(MMCore.class));
    }
    return instance;
  }

  private final Map<Inventory, AbstractGUIInventory> inventoryHandlerMap = new Object2ObjectOpenHashMap<>();

  protected void addHandler(final Inventory inventory, final AbstractGUIInventory handler) {
    this.inventoryHandlerMap.put(inventory, handler);
  }

  protected Optional<AbstractGUIInventory> getOptionalHandlerOf(final Inventory inventory) {
    return Optional.ofNullable(this.inventoryHandlerMap.get(inventory));
  }

  protected void terminate(final Inventory inventory) {
    this.inventoryHandlerMap.remove(inventory);
  }

}