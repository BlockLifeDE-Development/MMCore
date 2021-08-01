package com.gestankbratwurst.core.mmcore.util.common;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.gestankbratwurst.core.mmcore.util.common.sub.ChannelingCancel;
import com.gestankbratwurst.core.mmcore.util.common.sub.ChannelingCancel.ChannelCancelType;
import com.gestankbratwurst.core.mmcore.util.common.sub.WaitingPlayer;
import com.gestankbratwurst.core.mmcore.util.tasks.TaskManager;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Base64;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.inventory.Container;
import net.minecraft.world.inventory.Containers;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 17.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UtilPlayer implements Listener, Runnable {

  private static UtilPlayer instance;
  private static final boolean initialized = false;
  private static final Vector UP_VEC = new Vector(0, 1, 0);
  private static final Vector DOWN_VEC = new Vector(0, -1, 0);
  private static final Object2ObjectMap<Player, WaitingPlayer> WAITING_PLAYERS = new Object2ObjectOpenHashMap<>();

  public static void renameCurrentInv(final Player player, final String title, final Containers<?> containerType) {
    final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
    // FIXME active container
    final Container current = entityPlayer.bV;
    // FIXME current.windowId
    final PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(current.j, containerType, new ChatMessage(title));
    entityPlayer.b.sendPacket(packet);
  }

  public static void init(final JavaPlugin host) {
    Preconditions.checkArgument(!UtilPlayer.initialized, "UtilPlayer is already initialized!");
    UtilPlayer.instance = new UtilPlayer();
    Bukkit.getPluginManager().registerEvents(UtilPlayer.instance, host);
    TaskManager.getInstance().runRepeatedBukkit(UtilPlayer.instance, 1L, 1L);
  }

  public static boolean isInCave(final Player player) {
    return player.getEyeLocation().getBlock().getType() == Material.CAVE_AIR;
  }

  public static GameProfile getGameProfile(final Player player) {
    return ((CraftPlayerProfile) player.getPlayerProfile()).getGameProfile();
  }

  public static Block getBlockLookingAt(final Player player, final double maxDistance, final FluidCollisionMode collisionMode) {
    final Location eyeLoc = player.getEyeLocation();
    final Vector direction = eyeLoc.getDirection();
    final RayTraceResult traceResult = player.getWorld().rayTraceBlocks(eyeLoc, direction, maxDistance, collisionMode);
    if (traceResult == null) {
      return null;
    }
    return traceResult.getHitBlock();
  }

  public static Block getBlockLookingAt(final Player player, final double maxDistance) {
    return UtilPlayer.getBlockLookingAt(player, maxDistance, FluidCollisionMode.NEVER);
  }

  public static void forceWait(final Player player, final int ticks, final boolean cancelOnDamage, final Consumer<Player> afterwait,
      final Consumer<ChannelingCancel> onCancel) {
    UtilPlayer.WAITING_PLAYERS.put(player, new WaitingPlayer(player, ticks, cancelOnDamage, afterwait, onCancel));
  }

  public static ItemStack getHead(final OfflinePlayer player) {
    final ItemStack head = new ItemStack(Material.PLAYER_HEAD);
    final SkullMeta headMeta = (SkullMeta) head.getItemMeta();
    headMeta.setOwningPlayer(player);
    head.setItemMeta(headMeta);
    return head;
  }

  @EventHandler
  public void onDamage(final EntityDamageByEntityEvent event) {
    final Entity entity = event.getEntity();
    if (!(entity instanceof Player)) {
      return;
    }
    final WaitingPlayer waiting = UtilPlayer.WAITING_PLAYERS.get(entity);
    if (waiting == null) {
      return;
    }
    if (waiting.isCancelOnDamage()) {
      waiting.cancel(ChannelCancelType.DAMAGE);
      UtilPlayer.WAITING_PLAYERS.remove(entity);
    }
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    UtilPlayer.WAITING_PLAYERS.remove(event.getPlayer());
  }

  @EventHandler
  public void onMoving(final PlayerMoveEvent event) {
    if (!UtilPlayer.WAITING_PLAYERS.containsKey(event.getPlayer())) {
      return;
    }
    final Location from = event.getFrom();
    final Location to = event.getTo();
    if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
      UtilPlayer.WAITING_PLAYERS.get(event.getPlayer()).cancel(ChannelCancelType.MOVE);
      UtilPlayer.WAITING_PLAYERS.remove(event.getPlayer());
    }
  }

  public static Location getPlayerRightHandLocation(final Player player) {
    final Location eyeLoc = player.getEyeLocation();
    final Location playerLoc = player.getEyeLocation();
    final double yawRightHandDirection = Math.toRadians(-1 * eyeLoc.getYaw() - 45);
    final double x = 0.5 * Math.sin(yawRightHandDirection) + eyeLoc.getX();
    final double y = playerLoc.getY() - 0.5;
    final double z = 0.5 * Math.cos(yawRightHandDirection) + playerLoc.getZ();
    return new Location(player.getWorld(), x, y, z);
  }

  public static BlockFace getExactFacing(final Player player) {
    final Vector direction = player.getEyeLocation().getDirection();
    if (direction.angle(UtilPlayer.UP_VEC) <= (Math.PI / 4)) {
      return BlockFace.UP;
    } else if (direction.angle(UtilPlayer.DOWN_VEC) <= (Math.PI / 4)) {
      return BlockFace.DOWN;
    }
    return player.getFacing();
  }

  public static void playSound(final Player player, final Sound sound, final float volume, final float pitch) {
    player.playSound(player.getEyeLocation(), sound, pitch, volume);
  }

  public static void playUIClick(final Player player) {
    UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK, 0.8F, 1.2F);
  }

  public static void playSound(final Player player, final Sound sound) {
    UtilPlayer.playSound(player, sound, 1F, 1F);
  }

  public static String getEncodedTexture(final String url) {
    final byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
    return new String(encodedData);
  }


  @Override
  public void run() {
    if (!UtilPlayer.WAITING_PLAYERS.isEmpty()) {
      final Set<WaitingPlayer> removers = Sets.newHashSet();
      for (final WaitingPlayer waiting : UtilPlayer.WAITING_PLAYERS.values()) {
        if (waiting.lookup()) {
          removers.add(waiting);
        }
      }
      for (final WaitingPlayer waiting : removers) {
        UtilPlayer.WAITING_PLAYERS.remove(waiting.getPlayer());
      }
    }
  }
}
