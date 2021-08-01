package com.gestankbratwurst.core.mmcore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerUnloadsChunkEvent extends Event {

  private static final HandlerList handlers = new HandlerList();

  private final long chunkKey;
  private final Player player;

  public PlayerUnloadsChunkEvent(final Player who, final long chunkKey) {
    this.chunkKey = chunkKey;
    this.player = who;
  }

  public long getChunkKey() {
    return this.chunkKey;
  }

  public Player getPlayer() {
    return this.player;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

}
