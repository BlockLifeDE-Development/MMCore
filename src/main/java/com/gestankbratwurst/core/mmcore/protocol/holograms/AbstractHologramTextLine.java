package com.gestankbratwurst.core.mmcore.protocol.holograms;

import com.google.common.collect.Sets;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Consumer;

public abstract class AbstractHologramTextLine implements IHologramLine<String> {

  public AbstractHologramTextLine(final Location location, final AbstractHologram hologram) {
    this.lineLocation = location;
    this.hologram = hologram;
    this.clickActions = Sets.newHashSet();
  }

  private final Set<Consumer<Player>> clickActions;
  private final AbstractHologram hologram;

  @Override
  public AbstractHologram getHostingHologram() {
    return this.hologram;
  }

  @Override
  public void registerClickAction(final Consumer<Player> action) {
    this.clickActions.add(action);
  }

  @Override
  public void onClick(final Player player) {
    for (final Consumer<Player> action : this.clickActions) {
      action.accept(player);
    }
  }

  @Override
  public HologramLineType getType() {
    return HologramLineType.TEXT_LINE;
  }

  @Override
  public Location getLocation() {
    return this.lineLocation;
  }

  @Override
  public String getCurrentValue() {
    return this.text;
  }

  private final Location lineLocation;
  protected String text;

}
