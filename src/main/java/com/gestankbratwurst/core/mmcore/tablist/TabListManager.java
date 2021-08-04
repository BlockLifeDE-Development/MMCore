package com.gestankbratwurst.core.mmcore.tablist;

import com.gestankbratwurst.core.mmcore.tablist.abstraction.TabView;
import com.gestankbratwurst.core.mmcore.tablist.implementation.AbstractTabList;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.function.Function;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of AvarionCore and was created at the 10.12.2019
 *
 * AvarionCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class TabListManager {

  public TabListManager(final JavaPlugin plugin, final Function<Player, AbstractTabList> defaultTabListProvider) {
    this.tabViewMap = Maps.newHashMap();
    Bukkit.getPluginManager().registerEvents(new TabListListener(this), plugin);
    this.defaultTabListProvider = defaultTabListProvider;
  }

  private final Map<Player, TabView> tabViewMap;
  private final Function<Player, AbstractTabList> defaultTabListProvider;

  public TabView getView(final Player player) {
    return this.tabViewMap.get(player);
  }

  protected void addPlayer(final Player player) {
    this.tabViewMap.put(player, new TabView(player));
    final AbstractTabList tabList = this.defaultTabListProvider.apply(player);
    tabList.init();
    this.tabViewMap.get(player).setAndUpdate(tabList);
  }

  protected void removePlayer(final Player player) {
    this.tabViewMap.remove(player);
  }

}