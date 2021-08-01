package com.gestankbratwurst.core.mmcore.util.common.sub;

import com.gestankbratwurst.core.mmcore.util.common.sub.ChannelingCancel.ChannelCancelType;
import java.util.function.Consumer;
import lombok.Getter;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 13.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class WaitingPlayer {

  public WaitingPlayer(final Player player, final int ticks, final boolean cancelOnDamage, final Consumer<Player> afterWait,
      final Consumer<ChannelingCancel> onCancel) {
    this.player = player;
    this.afterWait = afterWait;
    this.onCancel = onCancel;
    this.ticksLeft = ticks;
    this.cancelOnDamage = cancelOnDamage;
  }

  @Getter
  private final Player player;
  private final Consumer<Player> afterWait;
  private final Consumer<ChannelingCancel> onCancel;
  private int ticksLeft;
  @Getter
  private final boolean cancelOnDamage;

  public void cancel(final ChannelCancelType channelCancelType) {
    if (this.player.isOnline()) {
      this.onCancel.accept(new ChannelingCancel(this.player, channelCancelType));
    }
  }

  public boolean lookup() {
    this.ticksLeft--;
    if (this.ticksLeft == 0) {
      if (this.player.isOnline()) {
        this.afterWait.accept(this.player);
      }
      return true;
    }
    return false;
  }

}
