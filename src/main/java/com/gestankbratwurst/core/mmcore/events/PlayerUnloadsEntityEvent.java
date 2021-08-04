package com.gestankbratwurst.core.mmcore.events;

import it.unimi.dsi.fastutil.ints.IntList;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerUnloadsEntityEvent extends PlayerEvent {

  private final IntList entityIDs;

  public PlayerUnloadsEntityEvent(final Player who, final IntList entityIDs) {
    super(who);
    this.entityIDs = entityIDs;
  }


  public IntList getEntityIDs() {
    return this.entityIDs;
  }

  private static final HandlerList handlers = new HandlerList();

  public static HandlerList getHandlerList() {
    return handlers;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }
}