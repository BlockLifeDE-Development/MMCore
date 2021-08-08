package com.gestankbratwurst.core.mmcore.tokenclick;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 05.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class TokenActionDomain {

  private final Player player;
  private final Cache<String, Consumer<Player>> actionMap = Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(15)).build();

  public void addAction(final String token, final Consumer<Player> action) {
    this.actionMap.put(token, action);
  }

  public void applyAction(final String token) {
    final Consumer<Player> action = this.actionMap.getIfPresent(token);
    if (action != null) {
      action.accept(this.player);
    }
  }

}
