package com.gestankbratwurst.core.mmcore.protocol.holograms.impl;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.gestankbratwurst.core.mmcore.protocol.holograms.AbstractHologramTextLine;
import com.gestankbratwurst.core.mmcore.protocol.holograms.HologramLineType;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 04.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class DynamicHologramTextLine extends AbstractHologramTextLine {

  public DynamicHologramTextLine(final Location location, final Function<Player, String> showFunction, final Hologram hologram) {
    super(location, hologram);
    this.textEntity = new DynamicTextEntity(location, showFunction);
  }

  private final DynamicTextEntity textEntity;

  @Override
  public void showTo(final Player player) {
    this.textEntity.sendSpawnPacket(player);
  }

  @Override
  public void hideFrom(final Player player) {
    this.textEntity.sendDespawnPacket(player);
  }

  @Override
  public void update(final String newValue) {
    for (final Player player : this.getHostingHologram().getViewers()) {
      this.textEntity.updateMetadata(player);
    }
  }

  @Override
  public HologramLineType getType() {
    return HologramLineType.DYNAMIC_TEXT_LINE;
  }

  private static final class DynamicTextEntity extends EntityArmorStand {

    private final Function<Player, String> showFunction;

    public DynamicTextEntity(final Location location, final Function<Player, String> showFunction) {
      super(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());
      this.showFunction = showFunction;
      this.setMarker(true);
      this.setInvulnerable(true);
      this.setInvisible(true);
      this.setCustomName(new ChatMessage("§cERROR"));
      this.setCustomNameVisible(true);
    }

    public void sendSpawnPacket(final Player player) {
      this.setCustomName(new ChatMessage(this.showFunction.apply(player)));
      ((CraftPlayer) player).getHandle().b.sendPacket(new PacketPlayOutSpawnEntity(this));
      this.updateMetadata(player);
    }

    public void sendDespawnPacket(final Player player) {
      ((CraftPlayer) player).getHandle().b.sendPacket(new PacketPlayOutEntityDestroy(this.getId()));
    }

    public void updateMetadata(final Player player) {
      this.setCustomName(new ChatMessage(this.showFunction.apply(player)));
      final PacketContainer container = PacketContainer
          .fromPacket(new PacketPlayOutEntityMetadata(this.getId(), this.getDataWatcher(), true));
      container.setMeta("mmcore-hologram", true);
      try {
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, container);
      } catch (final InvocationTargetException e) {
        e.printStackTrace();
      }
//			((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityMetadata(this.getId(), this.getDataWatcher(), true));
    }


  }

  @Override
  public void sendMove(final Player player, final Vector direction) {
    final PacketPlayOutRelEntityMove movePacket = new PacketPlayOutRelEntityMove(this.textEntity.getId(),
        (short) (direction.getX() * 4096), (short) (direction.getY() * 4096),
        (short) (direction.getZ() * 4096), false);
    ((CraftPlayer) player).getHandle().b.sendPacket(movePacket);
  }
}
