package com.gestankbratwurst.core.mmcore.inventories.guis;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

@RequiredArgsConstructor
public class GUIListener implements Listener {

  private final GUIManager guiManager;

  @EventHandler
  public void onInventoryClick(final InventoryClickEvent event) {
    this.guiManager.getOptionalHandlerOf(event.getInventory()).ifPresent(handler -> handler.handleClick(event));
  }

  @EventHandler
  public void onInventoryOpen(final InventoryOpenEvent event) {
    this.guiManager.getOptionalHandlerOf(event.getInventory()).ifPresent(handler -> handler.handleOpen(event));
  }

  @EventHandler
  public void onInventoryClose(final InventoryCloseEvent event) {
    this.guiManager.getOptionalHandlerOf(event.getInventory()).ifPresent(handler -> handler.handleClose(event));
  }

}
