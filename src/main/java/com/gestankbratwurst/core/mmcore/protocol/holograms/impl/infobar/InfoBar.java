package com.gestankbratwurst.core.mmcore.protocol.holograms.impl.infobar;


import com.gestankbratwurst.core.mmcore.protocol.holograms.impl.infobar.DoubleLinkedPacketHost.LinkedPacketType;
import com.gestankbratwurst.core.mmcore.protocol.holograms.infobars.AbstractInfoBar;
import com.gestankbratwurst.core.mmcore.protocol.holograms.infobars.InfoBarManager;
import com.gestankbratwurst.core.mmcore.protocol.packetwrapper.WrapperPlayServerMount;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.EntityPig;
import net.minecraft.world.entity.animal.EntityRabbit;
import net.minecraft.world.entity.animal.EntityTurtle;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class InfoBar extends AbstractInfoBar {

  public InfoBar(final Entity entity, final InfoBarManager infoBarManager) {
    super(entity, infoBarManager);
    this.lines = Lists.newArrayList();
    this.spawnPacketSupplier = Lists.newArrayList();
    this.lineEntityIDs = new IntOpenHashSet();
  }

  private final IntSet lineEntityIDs;
  private final ArrayList<Supplier<DoubleLinkedPacketHost>> spawnPacketSupplier;
  private final ArrayList<LineEntity> lines;

  @Override
  protected void showTo(final Player player) {
    this.viewingPlayer.add(player);
    final PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
    for (final Supplier<DoubleLinkedPacketHost> doubleLinkedPacketHostSupplier : this.spawnPacketSupplier) {
      final DoubleLinkedPacketHost packet = doubleLinkedPacketHostSupplier.get();
      if (packet.type == LinkedPacketType.NMS_PACKET) {
        packet.sendNMS(connection);
      } else {
        packet.sendProtocol(player);
      }
    }
  }

  @Override
  protected void hideFrom(final Player player) {
    this.viewingPlayer.remove(player);
    int index = 0;
    final int[] ids = new int[this.lineEntityIDs.size()];
    for (final int id : this.lineEntityIDs) {
      ids[index++] = id;
    }
    final PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(ids);
    ((CraftPlayer) player).getHandle().b.sendPacket(destroyPacket);
  }

  @Override
  public void editLine(final int index, final Function<String, String> lineEdit) {
    final LineEntity line = this.lines.get(index);
    final DoubleLinkedPacketHost packet = line.setNameAndGetMeta(lineEdit.apply(line.currentLine));
    for (final Player player : this.viewingPlayer) {
      packet.sendNMS(((CraftPlayer) player).getHandle().b);
    }
  }

  @Override
  public void setLine(final int index, final String newLine) {
    final LineEntity line = this.lines.get(index);
    final DoubleLinkedPacketHost packet = line.setNameAndGetMeta(newLine);
    for (final Player player : this.viewingPlayer) {
      packet.sendNMS(((CraftPlayer) player).getHandle().b);
    }
  }

  @Override
  public void addLine(final String newLine, final InfoLineSpacing spacing) {
    final LinePart spacingPart = spacing.linePartFunction.apply(this.entity.getLocation());

    final LineEntity lineEntity = new LineEntity(this.entity.getLocation(), newLine);

    final int lineID = lineEntity.getHandle().getId();
    final int spacingID = spacingPart.getHandle().getId();

    this.lineEntityIDs.add(lineID);
    this.lineEntityIDs.add(spacingID);

    this.infoBarManager.addMapping(lineID, this.getEntity());
    this.infoBarManager.addMapping(spacingID, this.getEntity());

    final ArrayList<Supplier<DoubleLinkedPacketHost>> newPackets = Lists.newArrayList();

    final net.minecraft.world.entity.Entity hostEntity = this.lines.size() == 0 ?
        ((CraftEntity) this.entity).getHandle() : this.lines.get(this.lines.size() - 1);

    newPackets.add(spacingPart::getLivingPacket);
    newPackets.add(() -> this.getMountPacket(hostEntity, spacingPart.getHandle()));
    newPackets.add(spacingPart::getMetaPacket);

    newPackets.add(lineEntity::getSpawnPacket);
    newPackets.add(() -> spacingPart.getMountPacket(lineEntity.getHandle().getId()));
    newPackets.add(spacingPart::getMetaPacket);
    newPackets.add(lineEntity::getMetaPacket);

    this.lines.add(lineEntity);

    final Map<Player, PlayerConnection> connections = new HashMap<>();

    for (final Player player : this.viewingPlayer) {
      connections.put(player, ((CraftPlayer) player).getHandle().b);
    }

    for (final Supplier<DoubleLinkedPacketHost> packetSupplier : newPackets) {
      this.spawnPacketSupplier.add(packetSupplier);
      final DoubleLinkedPacketHost packet = packetSupplier.get();
      if (packet.type == LinkedPacketType.NMS_PACKET) {
        for (final PlayerConnection conn : connections.values()) {
          packet.sendNMS(conn);
        }
      } else {
        for (final Player player : connections.keySet()) {
          packet.sendProtocol(player);
        }
      }

    }
  }

  private DoubleLinkedPacketHost getMountPacket(final net.minecraft.world.entity.Entity mount,
      final net.minecraft.world.entity.Entity rider) {
    final WrapperPlayServerMount packet = new WrapperPlayServerMount();
    packet.setEntityID(mount.getId());
    packet.setPassengerIds(new int[]{rider.getId()});

    return DoubleLinkedPacketHost.of(packet.getHandle());
  }

  @Override
  public int getSize() {
    return this.lines.size();
  }

  interface LinePart {

    net.minecraft.world.entity.Entity getHandle();

    DoubleLinkedPacketHost getSpawnPacket();

    DoubleLinkedPacketHost getMetaPacket();

    DoubleLinkedPacketHost setNameAndGetMeta(String line);

    DoubleLinkedPacketHost getMountPacket(int riderID);

    DoubleLinkedPacketHost getLivingPacket();
  }

  private static final class LineEntity extends EntityArmorStand implements LinePart {

    public LineEntity(final Location location, final String line) {
      super(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(),
          location.getZ());
      this.setMarker(true);
      this.setInvisible(true);
      this.setCustomNameVisible(line != null);
      if (this.getCustomNameVisible()) {
        this.currentLine = line;
        this.setCustomName(new ChatMessage(this.currentLine));
      } else {
        this.currentLine = "";
      }
    }

    private void setCurrentLine(final String line) {
      this.setCustomName(new ChatMessage(line));
      this.currentLine = line;
    }

    private String currentLine;

    @Override
    public DoubleLinkedPacketHost getSpawnPacket() {
      return DoubleLinkedPacketHost.of(new PacketPlayOutSpawnEntity(this));
    }

    @Override
    public DoubleLinkedPacketHost getMetaPacket() {
      return DoubleLinkedPacketHost.of(new PacketPlayOutEntityMetadata(this.getId(), this.getDataWatcher(), true));
    }

    @Override
    public DoubleLinkedPacketHost setNameAndGetMeta(final String line) {
      this.setCurrentLine(line);
      return this.getMetaPacket();
    }

    @Override
    public DoubleLinkedPacketHost getMountPacket(final int riderID) {
      final WrapperPlayServerMount packet = new WrapperPlayServerMount();
      packet.setEntityID(this.getId());
      packet.setPassengerIds(new int[]{riderID});

      return DoubleLinkedPacketHost.of(packet.getHandle());
    }

    @Override
    public net.minecraft.world.entity.Entity getHandle() {
      return this;
    }

    @Override
    public DoubleLinkedPacketHost getLivingPacket() {
      return null;
    }

  }

  private static final class SmallSpacingEntity extends EntityTurtle implements LinePart {

    public SmallSpacingEntity(final Location location) {
      super(EntityTypes.aT, ((CraftWorld) location.getWorld()).getHandle());
      this.setInvisible(true);
      this.setPosition(location.getX(), location.getY(), location.getZ());
      this.setAge(-100);
      this.ageLocked = true;
    }


    @Override
    public DoubleLinkedPacketHost getSpawnPacket() {
      return DoubleLinkedPacketHost.of(new PacketPlayOutSpawnEntity(this));
    }

    @Override
    public DoubleLinkedPacketHost getMetaPacket() {
      return DoubleLinkedPacketHost
          .of(new PacketPlayOutEntityMetadata(this.getId(), this.getDataWatcher(), true));
    }

    @Override
    public DoubleLinkedPacketHost setNameAndGetMeta(final String line) {
      this.setCustomName(new ChatMessage(line));
      return this.getMetaPacket();
    }

    @Override
    public DoubleLinkedPacketHost getMountPacket(final int riderID) {
      final WrapperPlayServerMount packet = new WrapperPlayServerMount();
      packet.setEntityID(this.getId());
      packet.setPassengerIds(new int[]{riderID});

      return DoubleLinkedPacketHost.of(packet.getHandle());
    }

    @Override
    public net.minecraft.world.entity.Entity getHandle() {
      return this;
    }

    @Override
    public DoubleLinkedPacketHost getLivingPacket() {
      return DoubleLinkedPacketHost.of(new PacketPlayOutSpawnEntityLiving(this));
    }

  }

  private static final class MediumSpacingEntity extends EntityRabbit implements LinePart {

    public MediumSpacingEntity(final Location location) {
      super(EntityTypes.au, ((CraftWorld) location.getWorld()).getHandle());
      this.setPosition(location.getX(), location.getY(), location.getZ());
      this.setInvisible(true);
      this.setAge(-100);
      this.ageLocked = true;
    }


    @Override
    public DoubleLinkedPacketHost getSpawnPacket() {
      return DoubleLinkedPacketHost.of(new PacketPlayOutSpawnEntity(this));
    }

    @Override
    public DoubleLinkedPacketHost getMetaPacket() {
      return DoubleLinkedPacketHost
          .of(new PacketPlayOutEntityMetadata(this.getId(), this.getDataWatcher(), true));
    }

    @Override
    public DoubleLinkedPacketHost setNameAndGetMeta(final String line) {
      this.setCustomName(new ChatMessage(line));
      return this.getMetaPacket();
    }

    @Override
    public DoubleLinkedPacketHost getMountPacket(final int riderID) {
      final WrapperPlayServerMount packet = new WrapperPlayServerMount();
      packet.setEntityID(this.getId());
      packet.setPassengerIds(new int[]{riderID});

      return DoubleLinkedPacketHost.of(packet.getHandle());
    }

    @Override
    public net.minecraft.world.entity.Entity getHandle() {
      return this;
    }

    @Override
    public DoubleLinkedPacketHost getLivingPacket() {
      return DoubleLinkedPacketHost.of(new PacketPlayOutSpawnEntityLiving(this));
    }

  }

  private static final class LargeSpacingEntity extends EntityPig implements LinePart {

    public LargeSpacingEntity(final Location location) {
      super(EntityTypes.an, ((CraftWorld) location.getWorld()).getHandle());
      this.setPosition(location.getX(), location.getY(), location.getZ());
      this.setInvisible(true);
      this.setAge(-100);
      this.ageLocked = true;
    }


    @Override
    public DoubleLinkedPacketHost getSpawnPacket() {
      return DoubleLinkedPacketHost.of(new PacketPlayOutSpawnEntity(this));
    }

    @Override
    public DoubleLinkedPacketHost getMetaPacket() {
      return DoubleLinkedPacketHost
          .of(new PacketPlayOutEntityMetadata(this.getId(), this.getDataWatcher(), true));
    }

    @Override
    public DoubleLinkedPacketHost setNameAndGetMeta(final String line) {
      this.setCustomName(new ChatMessage(line));
      return this.getMetaPacket();
    }

    @Override
    public DoubleLinkedPacketHost getMountPacket(final int riderID) {
      final WrapperPlayServerMount packet = new WrapperPlayServerMount();
      packet.setEntityID(this.getId());
      packet.setPassengerIds(new int[]{riderID});

      return DoubleLinkedPacketHost.of(packet.getHandle());
    }

    @Override
    public net.minecraft.world.entity.Entity getHandle() {
      return this;
    }

    @Override
    public DoubleLinkedPacketHost getLivingPacket() {
      return DoubleLinkedPacketHost.of(new PacketPlayOutSpawnEntityLiving(this));
    }

  }

  @Override
  public boolean isInLineOfSight(final Player player) {
    return player.hasLineOfSight(this.entity);
  }

  @AllArgsConstructor
  public enum InfoLineSpacing {

    SMALL(0.12, SmallSpacingEntity::new),
    MEDIUM(0.25, MediumSpacingEntity::new),
    LARGE(0.45, LargeSpacingEntity::new);

    private final double spacingValue;

    private final Function<Location, LinePart> linePartFunction;

    public double getSpacingValue() {
      return this.spacingValue;
    }

  }

}