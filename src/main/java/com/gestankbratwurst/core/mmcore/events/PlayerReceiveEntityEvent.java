package com.gestankbratwurst.core.mmcore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerReceiveEntityEvent extends PlayerEvent {

  private final int entityID;

  public PlayerReceiveEntityEvent(final Player who, final int entityID) {
    super(who);
    this.entityID = entityID;
  }

  public int getEntityID() {
    return this.entityID;
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