package com.gestankbratwurst.core.mmcore.protocol.holograms;

import java.util.UUID;
import java.util.function.Predicate;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IHologramFactory {

  AbstractHologram supplyHologram(Location location, Predicate<Player> viewFilter, AbstractHologramManager manager, UUID uid);

  default AbstractHologram supplyHologram(final Location location, final Predicate<Player> viewFilter, final AbstractHologramManager manager) {
    return this.supplyHologram(location, viewFilter, manager, UUID.randomUUID());
  }

}
