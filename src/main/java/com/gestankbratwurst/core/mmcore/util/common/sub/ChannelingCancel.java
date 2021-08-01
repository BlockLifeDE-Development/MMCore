package com.gestankbratwurst.core.mmcore.util.common.sub;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 25.01.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public class ChannelingCancel {

  @Getter
  private final Player player;
  @Getter
  private final ChannelCancelType cancelType;

  public enum ChannelCancelType {
    MOVE(),
    DAMAGE()
  }

}
