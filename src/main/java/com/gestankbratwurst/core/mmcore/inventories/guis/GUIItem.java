package com.gestankbratwurst.core.mmcore.inventories.guis;

import java.util.function.Consumer;
import java.util.function.Function;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 28.07.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@Builder
public class GUIItem {

  @Getter
  private final Function<Player, ItemStack> iconCreator;
  @Getter
  private final Consumer<InventoryClickEvent> eventConsumer;

}