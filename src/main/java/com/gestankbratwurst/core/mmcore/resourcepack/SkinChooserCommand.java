package com.gestankbratwurst.core.mmcore.resourcepack;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 09.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@CommandPermission("admin")
@CommandAlias("skinchooser")
public class SkinChooserCommand extends BaseCommand {

  @Default
  public void onCommand(final Player sender) {
    new SkinChooserGUI().openFor(sender);
  }

}
