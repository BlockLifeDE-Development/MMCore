package com.gestankbratwurst.core.mmcore.inventories.guis;

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
public interface IInventoryHandler {

  void handleClick(InventoryClickEvent event);

  void handleOpen(InventoryOpenEvent event);

  void handleClose(InventoryCloseEvent event);

}
