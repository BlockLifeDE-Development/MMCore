package com.gestankbratwurst.core.mmcore.tablist.abstraction;


import lombok.Getter;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of AvarionCore and was created at the 10.12.2019
 *
 * AvarionCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class TabView {

  public TabView(final Player player) {
    this.player = player;
    this.connection = ((CraftPlayer) player).getHandle().b;
  }

  @Getter
  private final Player player;
  @Getter
  private final PlayerConnection connection;
  @Getter
  private ITabList tablist;

  public void setTablist(final ITabList newTablist) {
    if (this.tablist != null) {
      this.tablist.removeViewer(this.connection);
    }
    this.tablist = newTablist;
    newTablist.addViewer(this.connection);
  }

  public void setAndUpdate(final ITabList newTablist) {
    this.tablist.hideFrom(this.connection);
    this.setTablist(newTablist);
    this.tablist.showTo(this.connection);
  }

}