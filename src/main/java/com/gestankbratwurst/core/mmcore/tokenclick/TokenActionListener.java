package com.gestankbratwurst.core.mmcore.tokenclick;

import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 05.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public class TokenActionListener implements Listener {

  private final TokenActionManager tokenActionManager;

  @EventHandler
  public void onJoin(final PlayerJoinEvent event) {
    this.tokenActionManager.addPlayer(event.getPlayer());
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    this.tokenActionManager.removePlayer(event.getPlayer());
  }

}
