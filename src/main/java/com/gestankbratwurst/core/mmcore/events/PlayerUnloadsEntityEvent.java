package com.gestankbratwurst.core.mmcore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerUnloadsEntityEvent extends PlayerEvent {

  private final int[] entityIDs;

  public PlayerUnloadsEntityEvent(final Player who, final int[] entityIDs) {
    super(who);
    this.entityIDs = entityIDs;
  }

  public int[] getEntityIDs() {
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