package com.gestankbratwurst.core.mmcore;

import com.gestankbratwurst.core.mmcore.inventories.guis.AbstractGUIInventory;
import com.gestankbratwurst.core.mmcore.inventories.guis.GUIItem;
import com.gestankbratwurst.core.mmcore.resourcepack.skins.TextureModel;
import com.gestankbratwurst.core.mmcore.util.common.UtilPlayer;
import com.gestankbratwurst.core.mmcore.util.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 09.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class MockGUI extends AbstractGUIInventory {

  @Override
  protected Inventory createInventory(final Player player) {
    return Bukkit.createInventory(null, 5 * 9, "Zustand deines Körpers");
  }

  @Override
  protected void init(final Player player) {
    this.setGUIItem(11, this.getHeadIcon());
    this.setGUIItem(15, this.getLevelIcon());
    this.setGUIItem(19, this.getHeartIcon());
    this.setGUIItem(20, this.getTorsoIcon());
    this.setGUIItem(21, this.getHandsIcon());
    this.setGUIItem(29, this.getFeetIcon());
    this.setGUIItem(33, this.getStatsIcon());
  }

  private GUIItem getLevelIcon() {

    return GUIItem.builder()
        .iconCreator(player -> new ItemBuilder(TextureModel.HOLY_BOOK.getItem())
            .name("§6Level")
            .lore("")
            .lore("§eStärke §f[" + TextureModel.STRENGTH_ICON.getChar() + "§f]")
            .lore("§f[§6" + "|".repeat(19) + "§7" + "|".repeat(41) + "§f]")
            .lore("")
            .lore("§eIntellekt §f[" + TextureModel.INTELLECT_ICON.getChar() + "§f]")
            .lore("§f[§e" + "|".repeat(27) + "§7" + "|".repeat(33) + "§f]")
            .lore("")
            .lore("§eAnsehen §f[" + TextureModel.PRESTIGE_ICON.getChar() + "§f]")
            .lore("§f[§a" + "|".repeat(40) + "§7" + "|".repeat(20) + "§f]")
            .build())
        .build();
  }

  private GUIItem getTorsoIcon() {
    return GUIItem.builder()
        .iconCreator(player -> new ItemBuilder(TextureModel.TORSO_ICON.getItem())
            .name("§6Oberkörper")
            .lore()
            .lore("§eNährstoffe")
            .lore("§f[§aGesund§7|§6Ungesund§7|§cLeer§f]")
            .lore("§f[§a" + "|".repeat(16) + "§6" + "|".repeat(6) + "§c" + "|".repeat(18) + "§f]")
            .lore("§7Etwas hungrig")
            .lore("")
            .lore("§eWunden")
            .lore("§7- Keine Wunden")
            .build())
        .build();
  }

  private GUIItem getFeetIcon() {
    return GUIItem.builder()
        .iconCreator(player -> new ItemBuilder(TextureModel.FEET_ICON.getItem())
            .name("§6Beine/Füße")
            .lore("")
            .lore("§eWunden")
            .lore("§f- Schnitt §7[§f" + TextureModel.BANDAGE_ICON.getChar() + " §7Behandelt]")
            .lore("§f- Prellung §7[§f" + TextureModel.BANDAGE_ICON.getChar() + " §7Behandelt]")
            .build())
        .build();
  }

  private GUIItem getHandsIcon() {
    return GUIItem.builder()
        .iconCreator(player -> new ItemBuilder(TextureModel.HANDS_ICON.getItem())
            .name("§6Arme/Hände")
            .build())
        .build();
  }

  private GUIItem getHeartIcon() {
    return GUIItem.builder()
        .iconCreator(player -> new ItemBuilder(TextureModel.HEART_ICON.getItem())
            .name("§6Organismus")
            .build())
        .build();
  }

  private GUIItem getHeadIcon() {
    return GUIItem.builder()
        .iconCreator(player -> new ItemBuilder(UtilPlayer.getHead(player))
            .name("§6Kopf, Geist und Verstand")
            .lore("")
            .lore("§eBefinden: §7Sehr glücklich")
            .lore("")
            .lore("§eWunden")
            .lore("§7- Keine Wunden")
            .lore("")
            .lore("§eGeist")
            .lore("§7- Gesunder Geist")
            .build())
        .build();
  }

  private GUIItem getStatsIcon() {
    return GUIItem.builder()
        .iconCreator(player -> new ItemBuilder(TextureModel.UP_DOWN_ICON.getItem())
            .name("§6Status")
            .lore("")
            .lore("§eBoni §f" + TextureModel.DOUBLE_GREEN_ARROW_UP.getChar())
            .lore("§f- Schönes Wetter")
            .lore("§7  > Du wirst draußen etwas schneller glücklich")
            .lore("§f- Hochwertige Kleidung")
            .lore("§7  > Du erhälst etwas schneller Ansehen")
            .lore("§f- Fast Food gegessen")
            .lore("§7  > Du wirst langsamer unglücklich")
            .lore("")
            .lore("§eMali §f" + TextureModel.DOUBLE_RED_ARROW_DOWN.getChar())
            .lore("§f- Verletzte Beine")
            .lore("§7  > Du bewegst dich §f15%§7 langsamer")
            .lore("§7  > Muskeltraining ist um §f10% §7verringert")
            .lore("§f- Leichtes Übergewicht")
            .lore("§7  > Du bewegst dich §f7%§7 langsamer")
            .lore("§7  > Leicht erhöhte Chance auf Beinverletzngen")
            .build())
        .build();
  }

}
