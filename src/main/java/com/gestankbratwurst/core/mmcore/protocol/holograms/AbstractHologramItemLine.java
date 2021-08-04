package com.gestankbratwurst.core.mmcore.protocol.holograms;

import com.google.common.collect.Sets;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Consumer;

public abstract class AbstractHologramItemLine implements IHologramLine<ItemStack> {

  public AbstractHologramItemLine(final Location location, final ItemStack itemStack, final AbstractHologram hologram) {
    this.location = location;
    this.itemStack = itemStack;
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

  private final Location location;
  private final ItemStack itemStack;

  @Override
  public Location getLocation() {
    return this.location;
  }

  @Override
  public ItemStack getCurrentValue() {
    return this.itemStack;
  }

  @Override
  public HologramLineType getType() {
    return HologramLineType.ITEM_LINE;
  }
}
