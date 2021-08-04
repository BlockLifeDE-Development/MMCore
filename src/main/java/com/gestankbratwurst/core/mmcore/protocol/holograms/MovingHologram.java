package com.gestankbratwurst.core.mmcore.protocol.holograms;

import org.bukkit.util.Vector;

public class MovingHologram {

  public MovingHologram(final AbstractHologram hologram, final Vector direction, final int ticks) {
    this.hologram = hologram;
    this.direction = direction;
    this.ticks = ticks;
  }

  private final AbstractHologram hologram;
  private final Vector direction;
  private final int ticks;
  private int ticksLived = 0;

  public AbstractHologram getHologram() {
    return this.hologram;
  }

  public boolean isAlive() {
    return this.ticks > this.ticksLived;
  }

  protected void onTick() {
    this.ticksLived++;
    if (this.direction != null) {
      this.hologram.move(this.direction);
    }
  }

}