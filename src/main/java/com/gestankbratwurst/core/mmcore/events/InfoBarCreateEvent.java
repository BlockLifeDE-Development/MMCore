package com.gestankbratwurst.core.mmcore.events;

import com.gestankbratwurst.core.mmcore.protocol.holograms.impl.infobar.InfoBar.InfoLineSpacing;
import com.google.common.collect.Lists;
import java.util.List;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InfoBarCreateEvent extends Event implements Cancellable {

  private static final HandlerList handlers = new HandlerList();

  public static HandlerList getHandlerList() {
    return handlers;
  }

  public InfoBarCreateEvent(final Entity entity) {
    this.entity = entity;
    this.lines = Lists.newArrayList();
  }

  private final Entity entity;
  private final List<Pair<String, InfoLineSpacing>> lines;

  private boolean cancelled = false;

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public Entity getEntity() {
    return this.entity;
  }

  public List<Pair<String, InfoLineSpacing>> getLines() {
    return this.lines;
  }

  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  @Override
  public void setCancelled(final boolean cancel) {
    this.cancelled = cancel;
  }


}
