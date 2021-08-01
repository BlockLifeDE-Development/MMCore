package com.gestankbratwurst.core.mmcore.util.common;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 19.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UtilVect {

  public static String vecToString(final Vector vector) {
    final ByteBuffer buffer = ByteBuffer.allocate(24);
    buffer.rewind();
    buffer.putDouble(vector.getX());
    buffer.putDouble(vector.getY());
    buffer.putDouble(vector.getZ());
    return new String(buffer.array());
  }

  public static void forEachBlock(final BoundingBox box, final World world, final Consumer<Block> blockConsumer) {
    for (int x = (int) box.getMinX(); x <= (int) box.getMaxX() - 1; x++) {
      for (int y = (int) box.getMinY(); y <= (int) box.getMaxY() - 1; y++) {
        for (int z = (int) box.getMinZ(); z <= (int) box.getMaxZ() - 1; z++) {
          blockConsumer.accept(world.getBlockAt(x, y, z));
        }
      }
    }
  }

  public static void forEachBlockDistributed(final JavaPlugin plugin, final BoundingBox box, final World world,
      final Consumer<Block> blockConsumer, final int actionsPerTick) {
    final int chunkMinX = (int) (box.getMinX() / 16);
    final int chunkMinZ = (int) (box.getMinZ() / 16);
    final int chunkMaxX = (int) (box.getMaxX() / 16);
    final int chunkMaxZ = (int) (box.getMaxZ() / 16);
    for (int x = chunkMinX; x <= chunkMaxX; x++) {
      for (int z = chunkMinZ; z <= chunkMaxZ; z++) {
        world.setChunkForceLoaded(x, z, true);
      }
    }
    new BukkitRunnable() {

      private int x = (int) box.getMinX();
      private int y = (int) box.getMinY();
      private int z = (int) box.getMinZ();

      private void proceed() {
        this.x++;
        if (this.x >= box.getMaxX()) {
          this.x = (int) box.getMinX();
          this.z++;
          if (this.z >= box.getMaxZ()) {
            this.z = (int) box.getMinZ();
            this.y++;
          }
        }
      }

      @Override
      public void run() {
        for (int i = 0; i < actionsPerTick; i++) {
          this.acceptNext();
          if (this.checkIfDone()) {
            return;
          }
        }
      }

      private boolean checkIfDone() {
        if (this.y >= box.getMaxY()) {
          this.cancel();
          return true;
        }
        return false;
      }

      private void acceptNext() {
        blockConsumer.accept(world.getBlockAt(this.x, this.y, this.z));
        this.proceed();
      }

      private void unloadChunks() {
        for (int x = chunkMinX; x <= chunkMaxX; x++) {
          for (int z = chunkMinZ; z <= chunkMaxZ; z++) {
            world.setChunkForceLoaded(x, z, false);
          }
        }
      }

    }.runTaskTimer(plugin, 1L, 1L);
  }

  public static void showBoundingBox(final BoundingBox box, final Player player, final int viewDist) {
    final World world = player.getWorld();
    final double maxDistSq = viewDist * viewDist;
    final Location iii = new Location(world, box.getMinX(), box.getMinY(), box.getMinZ());
    final Location iai = new Location(world, box.getMinX(), box.getMaxY(), box.getMinZ());
    final Location aii = new Location(world, box.getMaxX(), box.getMinY(), box.getMinZ());
    final Location aia = new Location(world, box.getMaxX(), box.getMinY(), box.getMaxZ());
    final Location iia = new Location(world, box.getMinX(), box.getMinY(), box.getMaxZ());
    final Location aai = new Location(world, box.getMaxX(), box.getMaxY(), box.getMinZ());
    final Location iaa = new Location(world, box.getMinX(), box.getMaxY(), box.getMaxZ());
    final Location aaa = new Location(world, box.getMaxX(), box.getMaxY(), box.getMaxZ());

    final List<Location> points = new ArrayList<>();

    points.addAll(getPointsBetween(iii, aii));
    points.addAll(getPointsBetween(iii, iia));
    points.addAll(getPointsBetween(iii, iai));

    points.addAll(getPointsBetween(aia, aii));
    points.addAll(getPointsBetween(aia, iia));

    points.addAll(getPointsBetween(iai, aai));
    points.addAll(getPointsBetween(iai, iaa));
    points.addAll(getPointsBetween(iaa, iia));

    points.addAll(getPointsBetween(aii, aai));
    points.addAll(getPointsBetween(aii, aai));

    points.addAll(getPointsBetween(aaa, aai));
    points.addAll(getPointsBetween(aaa, iaa));
    points.addAll(getPointsBetween(aaa, aia));

    particles(points, player.getLocation(), maxDistSq);
  }

  private static List<Location> getPointsBetween(Location from, Location to) {
    final List<Location> locations = new ArrayList<>();
    from = from.clone();
    to = to.clone();
    final Vector dir = to.toVector().subtract(from.toVector());
    final int increments = (int) (dir.length() / 0.75);
    dir.normalize().multiply(0.75);
    for (int i = 0; i < increments; i++) {
      from.add(dir);
      locations.add(from.clone());
    }

    return locations;
  }

  private static void particles(final List<Location> locations, final Location view, final double distSq) {
    final DustOptions options = new DustOptions(Color.RED, 1F);
    final World world = view.getWorld();
    for (final Location point : locations) {
      if (view.toVector().subtract(point.toVector()).lengthSquared() < distSq) {
        world.spawnParticle(Particle.REDSTONE, point, 1, 0, 0, 0, 0, options);
      }
    }
  }

  public static Vector vecFromString(final String vectorKey) {
    final ByteBuffer buffer = ByteBuffer.wrap(vectorKey.getBytes());
    buffer.rewind();
    final double x = buffer.getDouble(0);
    final double y = buffer.getDouble(8);
    final double z = buffer.getDouble(16);
    return new Vector(x, y, z);
  }

  public static String boxToString(final BoundingBox box) {
    return box.getMaxX() + "#" + box.getMaxY() + "#" + box.getMaxZ() + "#" + box.getMinX() + "#" + box.getMinY() + "#" + box.getMinZ();
  }

  public static BoundingBox stringToBox(final String boxString) {
    final String[] split = boxString.split("#");
    final Vector max = new Vector(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]));
    final Vector min = new Vector(Double.parseDouble(split[3]), Double.parseDouble(split[4]), Double.parseDouble(split[5]));
    return BoundingBox.of(max, min);
  }

}
