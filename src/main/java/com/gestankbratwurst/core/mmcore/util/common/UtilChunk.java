package com.gestankbratwurst.core.mmcore.util.common;


import com.gestankbratwurst.core.mmcore.tracking.ChunkTracker;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.longs.LongSet;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 17.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UtilChunk {

  public static int[] getChunkCoords(final long chunkKey) {
    final int x = ((int) chunkKey);
    final int z = (int) (chunkKey >> 32);
    return new int[]{x, z};
  }

  public static long getChunkKey(final int x, final int z) {
    return (long) x & 0xFFFFFFFFL | ((long) z & 0xFFFFFFFFL) << 32;
  }

  public static long getChunkKey(final Chunk chunk) {
    return (long) chunk.getX() & 0xffffffffL | ((long) chunk.getZ() & 0xffffffffL) << 32;
  }

  public static Chunk keyToChunk(final World world, final long chunkID) {
    Preconditions.checkArgument(world != null, "World cannot be null");
    return world.getChunkAt((int) chunkID, (int) (chunkID >> 32));
  }

  public static boolean isChunkLoaded(final Location loc) {
    final int chunkX = loc.getBlockX() >> 4;
    final int chunkZ = loc.getBlockZ() >> 4;
    final World world = loc.getWorld();
    if (world == null) {
      return false;
    }
    return world.isChunkLoaded(chunkX, chunkZ);
  }

  public static long getChunkKey(final Location loc) {
    return getChunkKey(loc.getBlockX() >> 4, loc.getBlockZ() >> 4);
  }

  public static long getChunkKey(final ChunkSnapshot chunk) {
    return (long) chunk.getX() & 0xffffffffL | ((long) chunk.getZ() & 0xffffffffL) << 32;
  }

  public static LongSet getChunkViews(final Player player) {
    return ChunkTracker.getChunkViews(player);
  }

  public static boolean isChunkInView(final Player player, final Chunk chunk) {
    return ChunkTracker.getChunkViews(player).contains(chunk.getChunkKey());
  }

}
