package com.gestankbratwurst.core.mmcore.protocol.holograms;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

public interface IHologramLine<T> {

  AbstractHologram getHostingHologram();

  T getCurrentValue();

  Location getLocation();

  void showTo(Player player);

  void hideFrom(Player player);

  void update(T newValue);

  HologramLineType getType();

  void registerClickAction(Consumer<Player> action);

  void onClick(Player player);

  void sendMove(Player player, Vector direction);

}