package com.gestankbratwurst.core.mmcore.inventories.guis;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public interface IInventoryHandler {

  void handleClick(InventoryClickEvent event);

  void handleOpen(InventoryOpenEvent event);

  void handleClose(InventoryCloseEvent event);

}
