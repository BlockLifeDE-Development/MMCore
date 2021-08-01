package com.gestankbratwurst.core.mmcore.util.items.display;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public interface DisplayConverter {

  String getDisplayKey();

  ItemInfo compileInfo(Player player, ItemStack item);

}
