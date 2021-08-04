package com.gestankbratwurst.core.mmcore.protocol.holograms;

import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.entity.Player;

public class HologramView implements Iterable<AbstractHologram> {

  public HologramView(final Player player) {
    this.player = player;
    this.viewingHolograms = Sets.newHashSet();
  }

  private final Player player;
  private final Set<AbstractHologram> viewingHolograms;

  public boolean isViewing(final AbstractHologram hologram) {
    return this.viewingHolograms.contains(hologram);
  }

  public void addHologram(final AbstractHologram hologram) {
    this.viewingHolograms.add(hologram);
    hologram.showTo(this.player);
  }

  public void removeHologram(final AbstractHologram hologram) {
    this.viewingHolograms.remove(hologram);
    hologram.hideFrom(this.player);
  }

  @Override
  public Iterator<AbstractHologram> iterator() {
    return this.viewingHolograms.iterator();
  }

}