package com.gestankbratwurst.core.mmcore.tracking;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.gestankbratwurst.core.mmcore.events.PlayerReceiveEntityEvent;
import com.gestankbratwurst.core.mmcore.events.PlayerUnloadsEntityEvent;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityTracker implements Listener {

  private static final Map<Player, IntSet> playerViews = new HashMap<>();

  public EntityTracker(final JavaPlugin host) {
    Bukkit.getOnlinePlayers().forEach(player -> EntityTracker.playerViews.put(player, new IntOpenHashSet()));

    ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(host, PacketType.Play.Server.SPAWN_ENTITY_LIVING) {

      @Override
      public void onPacketSending(final PacketEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
          if (event.getPacket().getMeta("mmcore-ignore").isPresent()) {
            return;
          }

          final PacketContainer packet = event.getPacket();

          final Player viewer = event.getPlayer();
          final int entityID = packet.getIntegers().read(0);

          final PlayerReceiveEntityEvent receiveEvent = new PlayerReceiveEntityEvent(viewer, entityID);
          Bukkit.getScheduler().runTask(host, () -> Bukkit.getPluginManager().callEvent(receiveEvent));
        }
      }
    });

    ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(host, PacketType.Play.Server.ENTITY_DESTROY) {

      @Override
      public void onPacketSending(final PacketEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.ENTITY_DESTROY) {
          if (event.getPacket().getMeta("mmcore-ignore").isPresent()) {
            return;
          }

          final PacketContainer packet = event.getPacket();

          final int[] entityIDs = packet.getIntegerArrays().getValues().get(0);
          final Player viewer = event.getPlayer();

          final PlayerUnloadsEntityEvent unloadEvent = new PlayerUnloadsEntityEvent(viewer, entityIDs);
          Bukkit.getScheduler().runTask(host, () -> Bukkit.getPluginManager().callEvent(unloadEvent));
        }
      }
    });
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onJoin(final PlayerJoinEvent event) {
    playerViews.put(event.getPlayer(), new IntOpenHashSet());
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onQuit(final PlayerQuitEvent event) {
    playerViews.remove(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onEntityShowing(final PlayerReceiveEntityEvent event) {
    playerViews.get(event.getPlayer()).add(event.getEntityID());
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onEntityHiding(final PlayerUnloadsEntityEvent event) {
    final Set<Integer> ints = playerViews.get(event.getPlayer());
    for (final int id : event.getEntityIDs()) {
      ints.remove(id);
    }
  }

  public static IntSet getEntityViewOf(final Player player) {
    return new IntOpenHashSet(playerViews.get(player));
  }
}