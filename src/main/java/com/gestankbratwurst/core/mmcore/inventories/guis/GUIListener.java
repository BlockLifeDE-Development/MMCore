package com.gestankbratwurst.core.mmcore.inventories.guis;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 27.07.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class GUIListener implements Listener {

  private final GUIManager guiManager;

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    guiManager.getOptionalHandlerOf(event.getInventory()).ifPresent(handler -> handler.handleClick(event));
  }

  @EventHandler
  public void onInventoryOpen(InventoryOpenEvent event) {
    guiManager.getOptionalHandlerOf(event.getInventory()).ifPresent(handler -> handler.handleOpen(event));
  }

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent event) {
    guiManager.getOptionalHandlerOf(event.getInventory()).ifPresent(handler -> handler.handleClose(event));
  }

}
