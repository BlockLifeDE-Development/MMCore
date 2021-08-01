package com.gestankbratwurst.core.mmcore.util.items.display;

import com.google.common.collect.Lists;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ItemInfo {

  public ItemInfo(final ItemStack inputItem) {
    this.inputClone = inputItem.clone();
    this.meta = this.inputClone.getItemMeta();
    if (this.meta.hasDisplayName()) {
      this.displayName = this.meta.getDisplayName();
    } else {
      this.displayName = "NO_NAME";
    }
    if (this.meta.hasLore()) {
      this.lores = this.meta.getLore();
    } else {
      this.lores = Lists.newArrayList();
    }
  }

  private final ItemStack inputClone;
  private final ItemMeta meta;
  private String displayName;
  private final List<String> lores;

  public void setLore(final List<String> lines) {
    this.lores.clear();
    for (final String line : lines) {
      this.lores.add(line);
    }
  }

  public void setLore(final String... lines) {
    this.lores.clear();
    for (final String line : lines) {
      this.lores.add(line);
    }
  }

  public void addLore(final String loreLine) {
    this.lores.add(loreLine);
  }

  public void setName(final String name) {
    this.displayName = name;
  }

  protected ItemStack getResult() {
    this.meta.setDisplayName(this.displayName);
    this.meta.setLore(this.lores);
    this.inputClone.setItemMeta(this.meta);
    return this.inputClone;
  }

}
