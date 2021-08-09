package com.gestankbratwurst.core.mmcore.resourcepack;

import com.gestankbratwurst.core.mmcore.inventories.guis.AbstractGUIInventory;
import com.gestankbratwurst.core.mmcore.inventories.guis.GUIItem;
import com.gestankbratwurst.core.mmcore.resourcepack.skins.TextureModel;
import com.gestankbratwurst.core.mmcore.util.common.UtilPlayer;
import com.gestankbratwurst.core.mmcore.util.items.ItemBuilder;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 21.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class SkinChooserGUI extends AbstractGUIInventory {

  @Override
  protected Inventory createInventory(final Player player) {
    return Bukkit.createInventory(player, 5 * 9, "Skin wählen");
  }

  @Override
  protected void init(final Player player) {
    Arrays.stream(TextureModel.values()).filter(TextureModel::isPlayerSkinModel).forEach(this::addClickableSkin);
  }

  private void addClickableSkin(final TextureModel model) {

    this.addGUIItem(GUIItem.builder()
        .iconCreator(player -> this.createHeadIcon(model))
        .eventConsumer(event -> this.handleEvent(event, model))
        .build());
  }

  private ItemStack createHeadIcon(final TextureModel model) {
    return new ItemBuilder(model.getHead())
        .name("§eModel: §f" + model.toString())
        .lore("")
        .lore("§7[Klicke zum anlegen]")
        .build();
  }

  private void handleEvent(final InventoryClickEvent event, final TextureModel model) {
    UtilPlayer.playSound((Player) event.getWhoClicked(), Sound.UI_BUTTON_CLICK);
    model.applySkinTo((Player) event.getWhoClicked());
  }

}
