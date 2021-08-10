package com.gestankbratwurst.core.mmcore.resourcepack.skins;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.gestankbratwurst.core.mmcore.resourcepack.packing.BoxedFontChar;
import com.gestankbratwurst.core.mmcore.skinclient.mineskin.data.Skin;
import com.gestankbratwurst.core.mmcore.util.common.NamespaceFactory;
import com.gestankbratwurst.core.mmcore.util.common.UtilItem;
import com.gestankbratwurst.core.mmcore.util.items.ItemBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.io.File;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of avarioncore and was created at the 24.11.2019
 *
 * avarioncore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public enum TextureModel {

  BLACK_ARROW_DOWN(Material.STICK, 1000, false, false, false),
  BLACK_ARROW_LEFT(Material.STICK, 1001, false, false, false),
  BLACK_ARROW_RIGHT(Material.STICK, 1002, false, false, false),
  BLACK_ARROW_UP(Material.STICK, 1003, false, false, false),
  GREEN_CHECK(Material.STICK, 1004, false, false, false),
  RED_X(Material.STICK, 1005, false, false, false),
  DOUBLE_GRAY_ARROW_UP(Material.STICK, 1006, false, false, false),
  DOUBLE_GRAY_ARROW_DOWN(Material.STICK, 1007, false, false, false),
  DOUBLE_GRAY_ARROW_LEFT(Material.STICK, 1008, false, false, false),
  DOUBLE_GRAY_ARROW_RIGHT(Material.STICK, 1009, false, false, false),
  DOUBLE_RED_ARROW_UP(Material.STICK, 1010, false, false, false),
  DOUBLE_GREEN_ARROW_DOWN(Material.STICK, 1011, false, false, false),
  GREEN_PLUS(Material.STICK, 1012, false, false, false),
  SCROLL(Material.STICK, 1013, false, false, false),

  LETTER_NO(Material.STICK, 1014, false, false, false),
  LETTER_YES(Material.STICK, 1015, false, false, false),
  LOCK_OPEN(Material.STICK, 1016, false, false, false),
  LOCK_CLOSED(Material.STICK, 1017, false, false, false),

  HUMAN_ICON(Material.STICK, 1018, false, false, false),
  ORC_ICON(Material.STICK, 1019, false, false, false),
  DWARF_ICON(Material.STICK, 1020, false, false, false),
  ELF_ICON(Material.STICK, 1021, false, false, false),
  UNDEAD_ICON(Material.STICK, 1022, false, false, false),
  WAR_ICON(Material.STICK, 1023, false, false, false),
  PEACE_ICON(Material.STICK, 1024, false, false, false),
  BROWN_BOOK(Material.STICK, 1025, false, false, false),
  HORN_ICON(Material.STICK, 1026, false, false, false),
  ELF_ORB(Material.STICK, 1027, false, false, false),
  CEMENT_BALL(Material.STICK, 1028, false, false, false),
  HOLY_BOOK(Material.STICK, 1029, false, false, false),
  ROLE_ICON(Material.STICK, 1030, false, false, false),
  FLAME_ICON_EMPTY(Material.STICK, 1031, false, false, false, FontMeta.of(12, 9, "bitmap")),
  FLAME_ICON_HALF(Material.STICK, 1032, false, false, false, FontMeta.of(12, 9, "bitmap")),
  FLAME_ICON_FULL(Material.STICK, 1033, false, false, false, FontMeta.of(12, 9, "bitmap")),
  PRECISION_ICON(Material.STICK, 1034, false, false, false),
  HEALTH_ICON(Material.STICK, 1035, false, false, false),
  DAMAGE_ICON(Material.STICK, 1036, false, false, false),
  DEFENCE_ICON(Material.STICK, 1037, false, false, false),
  ATTRIBUTES_ICON(Material.STICK, 1038, false, false, false),
  BASE_UPGRADE_ICON(Material.STICK, 1039, false, false, false),
  BASE_UPGRADE_DENY_ICON(Material.STICK, 1040, false, false, false),

  GOLD_PILE_TINY(Material.STICK, 1100, false, false, false),
  GOLD_PILE_SMALL(Material.STICK, 1101, false, false, false),
  GOLD_PILE_MEDIUM(Material.STICK, 1102, false, false, false),
  GOLD_PILE_BIG(Material.STICK, 1103, false, false, false),
  GOLD_PILE_HUGE(Material.STICK, 1104, false, false, false),
  GOLD_PILE_BAR_SMALL(Material.STICK, 1105, false, false, false),
  GOLD_PILE_BAR_MEDIUM(Material.STICK, 1106, false, false, false),
  GOLD_PILE_BAR_BIG(Material.STICK, 1107, false, false, false),
  SILVER_PILE_TINY(Material.STICK, 1108, false, false, false),
  SILVER_PILE_SMALL(Material.STICK, 1109, false, false, false),
  SILVER_PILE_MEDIUM(Material.STICK, 1110, false, false, false),
  SILVER_PILE_BIG(Material.STICK, 1111, false, false, false),
  SILVER_PILE_HUGE(Material.STICK, 1112, false, false, false),
  COPPER_PILE_TINY(Material.STICK, 1113, false, false, false),
  COPPER_PILE_SMALL(Material.STICK, 1114, false, false, false),
  COPPER_PILE_MEDIUM(Material.STICK, 1115, false, false, false),
  COPPER_PILE_BIG(Material.STICK, 1116, false, false, false),
  COPPER_PILE_HUGE(Material.STICK, 1117, false, false, false),
  COINS_GOLD_0(Material.STICK, 1118, false, false, false),
  COINS_GOLD_1(Material.STICK, 1119, false, false, false),
  COINS_GOLD_2(Material.STICK, 1120, false, false, false),
  COINS_GOLD_3(Material.STICK, 1121, false, false, false),
  COINS_GOLD_4(Material.STICK, 1122, false, false, false),
  COINS_GOLD_5(Material.STICK, 1123, false, false, false),
  COINS_SILVER_0(Material.STICK, 1124, false, false, false),
  COINS_SILVER_1(Material.STICK, 1125, false, false, false),
  COINS_SILVER_2(Material.STICK, 1126, false, false, false),
  COINS_SILVER_3(Material.STICK, 1127, false, false, false),
  COINS_SILVER_4(Material.STICK, 1128, false, false, false),
  COINS_SILVER_5(Material.STICK, 1129, false, false, false),
  COINS_COPPER_0(Material.STICK, 1130, false, false, false),
  COINS_COPPER_1(Material.STICK, 1131, false, false, false),
  COINS_COPPER_2(Material.STICK, 1132, false, false, false),
  COINS_COPPER_3(Material.STICK, 1133, false, false, false),
  COINS_COPPER_4(Material.STICK, 1134, false, false, false),
  COINS_COPPER_5(Material.STICK, 1135, false, false, false),
  BARS_GOLD_0(Material.STICK, 1136, false, false, false),
  BARS_GOLD_1(Material.STICK, 1137, false, false, false),
  BARS_GOLD_2(Material.STICK, 1138, false, false, false),
  KILL_QUEST_ICON(Material.STONE_SWORD, 1139, false, false, false),
  GATHER_QUEST_ICON(Material.STONE_SWORD, 1140, false, false, false),
  ACTION_BAR_ICON_MIDDLE(Material.STICK, 1141, false, false, false),
  ACTION_BAR_ICON_LEFT(Material.STICK, 1142, false, false, false),
  ACTION_BAR_ICON_RIGHT(Material.STICK, 1143, false, false, false),
  WOODEN_SPEAR_PIKE(Material.STICK, 1144, false, false, false),
  STONE_SPEAR_PIKE(Material.STICK, 1145, false, false, false),
  GOLDEN_SPEAR_PIKE(Material.STICK, 1146, false, false, false),
  IRON_SPEAR_PIKE(Material.STICK, 1147, false, false, false),
  DIAMOND_SPEAR_PIKE(Material.STICK, 1148, false, false, false),
  NETHERITE_SPEAR_PIKE(Material.STICK, 1149, false, false, false),
  SWORD_FIGHTER_ICON(Material.STICK, 1150, false, false, false),
  ALCHEMIST_ICON(Material.STICK, 1151, false, false, false),
  AXE_FIGHTER_ICON(Material.STICK, 1152, false, false, false),
  SPEAR_FIGHTER_ICON(Material.STICK, 1153, false, false, false),
  BOW_FIGHTER_ICON(Material.STICK, 1154, false, false, false),
  SHAMAN_ICON(Material.STICK, 1155, false, false, false),
  ALCHEMIST_STAFF_HEAD(Material.STICK, 1156, false, false, false),
  SHAMAN_STAFF_HEAD(Material.STICK, 1157, false, false, false),
  LOGO(Material.STICK, 1158, false, false, false, FontMeta.of(48, 48, "bitmap")),
  TORSO_ICON(Material.STICK, 1159, false, false, false),
  FEET_ICON(Material.STICK, 1160, false, false, false),
  HANDS_ICON(Material.STICK, 1161, false, false, false),
  HEART_ICON(Material.STICK, 1162, false, false, false),
  UP_DOWN_ICON(Material.STICK, 1163, false, false, false),
  DOUBLE_GREEN_ARROW_UP(Material.STICK, 1164, false, false, false),
  DOUBLE_RED_ARROW_DOWN(Material.STICK, 1165, false, false, false),
  BANDAGE_ICON(Material.STICK, 1166, false, false, false),
  STRENGTH_ICON(Material.STICK, 1167, false, false, false),
  INTELLECT_ICON(Material.STICK, 1168, false, false, false),
  PRESTIGE_ICON(Material.STICK, 1169, false, false, false),

  DWARF_CANON(Material.STICK, 1200, false, true, false),
  HUMAN_CROWN(Material.STICK, 1201, false, true, false),
  DWARF_CROWN(Material.STICK, 1202, false, true, false),
  ELF_CROWN(Material.STICK, 1203, false, true, false),
  UNDEAD_CROWN(Material.STICK, 1204, false, true, false),
  CANON_BALL(Material.STICK, 1205, false, true, false),
  HEALING_FOUNTAIN(Material.STICK, 1206, false, true, false),
  ELF_ORB_MODEL(Material.STICK, 1207, false, true, false),
  UNDEAD_TOTEM(Material.STICK, 1208, false, true, false),
  ORC_HORN(Material.STICK, 1209, false, true, false),
  HOLY_CIRCLE(Material.STICK, 1210, false, true, false),
  WOODEN_SPEAR(Material.WOODEN_SWORD, 1211, false, true, false),
  STONE_SPEAR(Material.STONE_SWORD, 1212, false, true, false),
  GOLDEN_SPEAR(Material.GOLDEN_SWORD, 1213, false, true, false),
  IRON_SPEAR(Material.IRON_SWORD, 1214, false, true, false),
  DIAMOND_SPEAR(Material.DIAMOND_SWORD, 1215, false, true, false),
  NETHERITE_SPEAR(Material.NETHERITE_SWORD, 1216, false, true, false),
  SHAMAN_STAFF(Material.STONE_SWORD, 1217, false, true, false),
  ALCHEMIST_STAFF(Material.STONE_SWORD, 1218, false, true, false),
  ORC_CROWN(Material.STICK, 1219, false, true, false),
  LOW_QUALITY_MAP(Material.STICK, 1220, false, true, false),
  HIGH_QUALITY_MAP(Material.STICK, 1221, false, true, false),
  BRICK_HAMMER(Material.IRON_PICKAXE, 1222, false, true, false),

  TRADE_GUI(Material.STICK, 2000, false, true, false),
  ADMIN_SHOP_BORDER(Material.STICK, 2001, false, true, false),
  RECIPE_VIEW_UI(Material.STICK, 2002, false, true, false),
  MOCK_UI(Material.STICK, 2003, false, false, false, new FontMeta(-100, -8, 90, 0, "bitmap")),

  DWARF_SKIN_1(Material.STICK, 3000, false, false, true),
  /*
  DWARF_SKIN_2(Material.STICK, 3001, false, false, true),
  DWARF_SKIN_3(Material.STICK, 3002, false, false, true),
  DWARF_SKIN_4(Material.STICK, 3003, false, false, true),
  DWARF_SKIN_5(Material.STICK, 3004, false, false, true),
  DWARF_SKIN_6(Material.STICK, 3005, false, false, true),
  DWARF_SKIN_7(Material.STICK, 3006, false, false, true),
  DWARF_SKIN_8(Material.STICK, 3007, false, false, true),
  DWARF_SKIN_9(Material.STICK, 3008, false, false, true),
  DWARF_SKIN_10(Material.STICK, 3009, false, false, true),

  ELF_SKIN_1(Material.STICK, 3010, false, false, true),
  ELF_SKIN_2(Material.STICK, 3011, false, false, true),
  ELF_SKIN_3(Material.STICK, 3012, false, false, true),
  ELF_SKIN_4(Material.STICK, 3013, false, false, true),
  ELF_SKIN_5(Material.STICK, 3014, false, false, true),
  ELF_SKIN_6(Material.STICK, 3015, false, false, true),
  ELF_SKIN_7(Material.STICK, 3016, false, false, true),
  ELF_SKIN_8(Material.STICK, 3017, false, false, true),
  ELF_SKIN_9(Material.STICK, 3018, false, false, true),
  ELF_SKIN_10(Material.STICK, 3019, false, false, true),

  HUMAN_SKIN_1(Material.STICK, 3020, false, false, true),
  HUMAN_SKIN_2(Material.STICK, 3021, false, false, true),
  HUMAN_SKIN_3(Material.STICK, 3022, false, false, true),
  HUMAN_SKIN_4(Material.STICK, 3023, false, false, true),
  HUMAN_SKIN_5(Material.STICK, 3024, false, false, true),
  HUMAN_SKIN_6(Material.STICK, 3025, false, false, true),
  HUMAN_SKIN_7(Material.STICK, 3026, false, false, true),
  HUMAN_SKIN_8(Material.STICK, 3027, false, false, true),
  HUMAN_SKIN_9(Material.STICK, 3028, false, false, true),
  HUMAN_SKIN_10(Material.STICK, 3029, false, false, true),

  ORC_SKIN_1(Material.STICK, 3030, false, false, true),
  ORC_SKIN_2(Material.STICK, 3031, false, false, true),
  ORC_SKIN_3(Material.STICK, 3032, false, false, true),
  ORC_SKIN_4(Material.STICK, 3033, false, false, true),
  ORC_SKIN_5(Material.STICK, 3034, false, false, true),
  ORC_SKIN_6(Material.STICK, 3035, false, false, true),
  ORC_SKIN_7(Material.STICK, 3036, false, false, true),
  ORC_SKIN_8(Material.STICK, 3037, false, false, true),
  ORC_SKIN_9(Material.STICK, 3038, false, false, true),
  ORC_SKIN_10(Material.STICK, 3039, false, false, true),

  UNDEAD_SKIN_1(Material.STICK, 3040, false, false, true),
  UNDEAD_SKIN_2(Material.STICK, 3041, false, false, true),
  UNDEAD_SKIN_3(Material.STICK, 3042, false, false, true),
  UNDEAD_SKIN_4(Material.STICK, 3043, false, false, true),
  UNDEAD_SKIN_5(Material.STICK, 3044, false, false, true),
  UNDEAD_SKIN_6(Material.STICK, 3045, false, false, true),
  UNDEAD_SKIN_7(Material.STICK, 3046, false, false, true),
  UNDEAD_SKIN_8(Material.STICK, 3047, false, false, true),
  UNDEAD_SKIN_9(Material.STICK, 3048, false, false, true),
  UNDEAD_SKIN_10(Material.STICK, 3049, false, false, true)
 */;

  TextureModel(final Material baseMaterial, final int modelID, final boolean headEnabled, final boolean customModelDataEnabled,
      final boolean playerSkinModel) {
    this.baseMaterial = baseMaterial;
    this.modelID = modelID;
    this.modelData = ModelData.defaultGenerated();
    this.fontMeta = FontMeta.common();
    this.boxedFontChar = new BoxedFontChar();
    this.headSkinEnabled = playerSkinModel || headEnabled;
    this.customModelDataEnabled = customModelDataEnabled;
    this.playerSkinModel = playerSkinModel;
  }

  TextureModel(final Material baseMaterial, final int modelID, final boolean headEnabled, final boolean customModelDataEnabled,
      final boolean playerSkinModel, final FontMeta fontMeta) {
    this.baseMaterial = baseMaterial;
    this.modelID = modelID;
    this.modelData = ModelData.defaultGenerated();
    this.fontMeta = fontMeta;
    this.boxedFontChar = new BoxedFontChar();
    this.headSkinEnabled = playerSkinModel || headEnabled;
    this.customModelDataEnabled = customModelDataEnabled;
    this.playerSkinModel = playerSkinModel;
  }

  TextureModel(final Material baseMaterial, final int modelID, final ModelData modelData, final FontMeta fontMeta,
      final boolean headEnabled,
      final boolean customModelDataEnabled, final boolean playerSkinModel) {
    Bukkit.getServer().getName();
    this.baseMaterial = baseMaterial;
    this.modelID = modelID;
    this.modelData = modelData;
    this.fontMeta = fontMeta;
    this.boxedFontChar = new BoxedFontChar();
    this.headSkinEnabled = headEnabled;
    this.customModelDataEnabled = customModelDataEnabled;
    this.playerSkinModel = playerSkinModel;
  }

  @Getter
  private final Material baseMaterial;
  @Getter
  private final int modelID;
  @Getter
  private final ModelData modelData;
  @Getter
  private final FontMeta fontMeta;
  @Getter
  private final BoxedFontChar boxedFontChar;
  @Getter
  private final boolean headSkinEnabled;
  @Getter
  private final boolean customModelDataEnabled;
  @Getter
  private final boolean playerSkinModel;
  @Getter
  @Setter
  private Skin skin;
  @Getter
  @Setter
  private File linkedImageFile;
  @Getter
  private GameProfile gameProfile;

  private ItemStack head;

  private ItemStack item;

  private void initProfile() {
    if (this.gameProfile == null && this.skin != null) {
      this.gameProfile = new GameProfile(this.skin.data.uuid, this.skin.name);
      this.gameProfile.getProperties()
          .put("textures", new Property("textures", this.skin.data.texture.value, this.skin.data.texture.signature));
    }
  }

  public void applySkinTo(final Player player) {
    final PlayerProfile profile = player.getPlayerProfile();
    profile.removeProperty("textures");
    profile.setProperty(new ProfileProperty("textures", this.skin.data.texture.value, this.skin.data.texture.signature));
    player.setPlayerProfile(profile);
  }

  public char getChar() {
    return this.boxedFontChar.getAsCharacter();
  }

  public ItemStack getItem() {
    if (this.item == null) {
      this.item = new ItemBuilder(this.baseMaterial)
          .modelData(this.modelID)
          .name(this.toString())
          .addPersistentData("Model", PersistentDataType.STRING, this.toString())
          .build();
    }
    return this.item.clone();
  }

  public ItemStack getHead() {
    if (this.head != null) {
      return this.head.clone();
    }
    this.initProfile();

    this.head = UtilItem.produceHead(this.gameProfile);

    final ItemMeta meta = this.head.getItemMeta();
    final PersistentDataContainer container = meta.getPersistentDataContainer();
    container.set(NamespaceFactory.provide("Model"), PersistentDataType.STRING, this.toString());
    this.head.setItemMeta(meta);
    return this.head.clone();
  }

}