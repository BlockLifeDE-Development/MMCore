package com.gestankbratwurst.core.mmcore.tokenclick;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Private;
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
@Private
@RequiredArgsConstructor
@CommandAlias("tokenclick")
public class TokenClickCommand extends BaseCommand {

  private final TokenActionManager tokenActionManager;

  @Default
  public void onCommand(final Player player, final String token) {
    this.tokenActionManager.applyAction(player, token);
  }

}