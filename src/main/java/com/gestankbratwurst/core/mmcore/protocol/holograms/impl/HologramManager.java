package com.gestankbratwurst.core.mmcore.protocol.holograms.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.gestankbratwurst.core.mmcore.protocol.holograms.AbstractHologramManager;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
import org.bukkit.plugin.java.JavaPlugin;

public class HologramManager extends AbstractHologramManager {

  public HologramManager(final JavaPlugin host) {
    super(host, new HologramFactory());
    final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    this.registerClickListener(protocolManager, host);
  }

  private void registerClickListener(final ProtocolManager protocolManager, final JavaPlugin host) {

    protocolManager.addPacketListener(new PacketAdapter(host, PacketType.Play.Client.USE_ENTITY) {

      private final long minDelayBetweenClicks = 200L;
      private final Map<UUID, Long> clickInterval = Maps.newHashMap();

      @Override
      public void onPacketReceiving(final PacketEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
          final UUID playerID = event.getPlayer().getUniqueId();
          if (this.clickInterval.containsKey(playerID)
              && System.currentTimeMillis() < this.clickInterval.get(playerID) + this.minDelayBetweenClicks) {
            return;
          } else {
            this.clickInterval.put(playerID, System.currentTimeMillis());
          }
          final PacketContainer packet = event.getPacket();
          final int entityID = packet.getIntegers().getValues().get(0);
          HologramManager.this.onInteract(event.getPlayer(), entityID);
        }
      }

    });
  }
}