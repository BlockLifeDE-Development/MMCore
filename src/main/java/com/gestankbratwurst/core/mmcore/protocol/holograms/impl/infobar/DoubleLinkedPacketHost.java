package com.gestankbratwurst.core.mmcore.protocol.holograms.impl.infobar;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.base.Preconditions;
import java.lang.reflect.InvocationTargetException;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.entity.Player;

public class DoubleLinkedPacketHost {

  public static DoubleLinkedPacketHost of(final Packet<?> packet) {
    return new DoubleLinkedPacketHost(packet);
  }

  public static DoubleLinkedPacketHost of(final PacketContainer packet) {
    return new DoubleLinkedPacketHost(packet);
  }

  private DoubleLinkedPacketHost(final Packet<?> packet) {
    this.NMSPacket = packet;
    this.protocolPacket = null;
    this.protManager = null;
    this.type = LinkedPacketType.NMS_PACKET;
  }

  private DoubleLinkedPacketHost(final PacketContainer packet) {
    this.NMSPacket = null;
    this.protocolPacket = packet;
    this.protManager = ProtocolLibrary.getProtocolManager();
    this.type = LinkedPacketType.PROTOCOL_PACKET;
  }

  private final ProtocolManager protManager;
  public final LinkedPacketType type;
  private final Packet<?> NMSPacket;
  private final PacketContainer protocolPacket;

  public void sendNMS(final PlayerConnection conn) {
    Preconditions.checkState(this.type == LinkedPacketType.NMS_PACKET);
    conn.sendPacket(this.NMSPacket);
  }

  public void sendProtocol(final Player player) {
    Preconditions.checkState(this.type == LinkedPacketType.PROTOCOL_PACKET);
    try {
      this.protManager.sendServerPacket(player, this.protocolPacket);
    } catch (final InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  public static enum LinkedPacketType {
    NMS_PACKET,
    PROTOCOL_PACKET
  }

}
