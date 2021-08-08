package com.gestankbratwurst.core.mmcore.tokenclick;

import com.gestankbratwurst.core.mmcore.MMCore;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 05.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class TokenActionManager {

  private final Map<Player, TokenActionDomain> playerDomainMap = new HashMap<>();

  public TokenActionManager() {
    Bukkit.getPluginManager().registerEvents(new TokenActionListener(this), JavaPlugin.getPlugin(MMCore.class));
    MMCore.getPaperCommandManager().registerCommand(new TokenClickCommand(this));
  }

  public ClickEvent createClickableAction(final Player player, final Consumer<Player> action) {
    return this.createClickableAction(player, this.createRandomToken(), action);
  }

  public ClickEvent createClickableAction(final Player player, final String token, final Consumer<Player> action) {
    this.addAction(player, token, action);
    return ClickEvent.runCommand("tokenclick " + token);
  }

  protected void applyAction(final Player player, final String token) {
    this.playerDomainMap.get(player).applyAction(token);
  }

  protected void addPlayer(final Player player) {
    this.playerDomainMap.put(player, new TokenActionDomain(player));
  }

  protected void removePlayer(final Player player) {
    this.playerDomainMap.remove(player);
  }

  private void addAction(final Player player, final String token, final Consumer<Player> action) {
    this.playerDomainMap.get(player).addAction(token, action);
  }

  private String createRandomToken() {
    return UUID.randomUUID().toString();
  }

}
