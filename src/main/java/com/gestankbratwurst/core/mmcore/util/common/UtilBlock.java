package com.gestankbratwurst.core.mmcore.util.common;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of avarioncore and was created at the 26.03.2020
 *
 * avarioncore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UtilBlock implements Listener {

  public static void sendBlockDamage(final Player player, final Location loc, final float progress) {
    Preconditions.checkArgument(player != null, "player must not be null");
    Preconditions.checkArgument(loc != null, "loc must not be null");
    Preconditions.checkArgument(progress >= 0.0 && progress <= 1.0, "progress must be between 0.0 and 1.0 (inclusive)");

    final int stage = (int) (9 * progress); // There are 0 - 9 damage states
    final PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(((CraftPlayer) player).getHandle().getId(),
        new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), stage);
    ((CraftPlayer) player).getHandle().b.sendPacket(packet);
  }

}