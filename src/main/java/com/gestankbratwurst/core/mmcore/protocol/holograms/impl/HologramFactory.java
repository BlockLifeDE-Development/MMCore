package com.gestankbratwurst.core.mmcore.protocol.holograms.impl;

import com.gestankbratwurst.core.mmcore.protocol.holograms.AbstractHologramManager;
import com.gestankbratwurst.core.mmcore.protocol.holograms.IHologramFactory;
import java.util.UUID;
import java.util.function.Predicate;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HologramFactory implements IHologramFactory {

  @Override
  public Hologram supplyHologram(
      final Location location, final Predicate<Player> viewFilter, final AbstractHologramManager manager, final UUID uid) {
    return new Hologram(location, viewFilter, manager, uid);
  }

}
