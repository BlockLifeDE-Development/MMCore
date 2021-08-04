package com.gestankbratwurst.core.mmcore.protocol.holograms.impl;


import com.gestankbratwurst.core.mmcore.protocol.holograms.AbstractHologramItemLine;
import com.gestankbratwurst.core.mmcore.protocol.packetwrapper.WrapperPlayServerMount;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class HologramItemLine extends AbstractHologramItemLine {

  public HologramItemLine(final Location location, final ItemStack itemStack, final Hologram hologram) {
    super(location, itemStack, hologram);
    this.itemEntity = new ItemEntity(location, itemStack);
    this.itemVehicle = new ItemVehicle(location.clone().add(0, -0.1, 0));
    this.packets = new Packet<?>[6];
    this.packets[0] = this.itemEntity.getSpawnPacket();
    this.packets[1] = this.itemEntity.getDespawnPacket();
    this.packets[2] = this.itemEntity.getUpdatePacket();
    this.packets[3] = this.itemVehicle.getSpawnPacket();
    this.packets[4] = this.itemVehicle.getDespawnPacket();
    this.packets[5] = this.itemVehicle.getHidePacket();
    this.mountPacket = this.itemVehicle.getMountPacket(this.itemEntity);
  }

  private final ItemEntity itemEntity;
  private final ItemVehicle itemVehicle;
  private final Packet<?>[] packets;
  private WrapperPlayServerMount mountPacket;

  @Override
  public void showTo(final Player player) {
    final PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
    connection.sendPacket(this.packets[0]);
    connection.sendPacket(this.packets[3]);
    connection.sendPacket(this.packets[2]);
    connection.sendPacket(this.packets[5]);
    this.mountPacket.sendPacket(player);
  }

  @Override
  public void hideFrom(final Player player) {
    final PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
    connection.sendPacket(this.packets[1]);
    connection.sendPacket(this.packets[4]);
  }

  @Override
  public void update(final ItemStack newValue) {
    this.itemEntity.setItemStack(CraftItemStack.asNMSCopy(newValue));
    this.packets[0] = this.itemEntity.getSpawnPacket();
    this.packets[1] = this.itemEntity.getDespawnPacket();
    this.packets[2] = this.itemEntity.getUpdatePacket();
    this.mountPacket = this.itemVehicle.getMountPacket(this.itemEntity);

    for (final Player player : this.getHostingHologram().getViewers()) {
      final PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
      connection.sendPacket(this.packets[2]);
      connection.sendPacket(this.packets[5]);
    }
  }

  private static final class ItemVehicle extends EntityArmorStand {

    public ItemVehicle(final Location location) {
      super(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY() + 0.2,
          location.getZ());
      this.setMarker(true);
      this.setInvisible(true);
      this.setCustomNameVisible(false);
    }

    public PacketPlayOutSpawnEntity getSpawnPacket() {
      return new PacketPlayOutSpawnEntity(this);
    }

    public PacketPlayOutEntityMetadata getHidePacket() {
      return new PacketPlayOutEntityMetadata(this.getId(), this.getDataWatcher(), true);
    }

    public PacketPlayOutEntityDestroy getDespawnPacket() {
      return new PacketPlayOutEntityDestroy(this.getId());
    }

    public WrapperPlayServerMount getMountPacket(final EntityItem item) {
      final WrapperPlayServerMount packet = new WrapperPlayServerMount();

      packet.setEntityID(this.getId());
      packet.setPassengerIds(new int[]{item.getId()});

      return packet;
    }

  }

  private static final class ItemEntity extends EntityItem {

    private ItemEntity(final Location location, final ItemStack item) {
      super(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(),
          location.getZ(), CraftItemStack.asNMSCopy(item));
      this.setInvulnerable(true);
      this.setNoGravity(true);
      this.setMot(new Vec3D(0, 0, 0));
      this.velocityChanged();
    }

    @Override
    public void tick() {

    }

    public PacketPlayOutSpawnEntity getSpawnPacket() {
      return new PacketPlayOutSpawnEntity(this);
    }

    public PacketPlayOutEntityDestroy getDespawnPacket() {
      return new PacketPlayOutEntityDestroy(this.getId());
    }

    public PacketPlayOutEntityMetadata getUpdatePacket() {
      return new PacketPlayOutEntityMetadata(this.getId(), this.getDataWatcher(), true);
    }

  }

  @Override
  public void sendMove(final Player player, final Vector direction) {
    final PacketPlayOutRelEntityMove movePacket = new PacketPlayOutRelEntityMove(this.itemEntity.getId(),
        (short) (direction.getX() * 4096), (short) (direction.getY() * 4096),
        (short) (direction.getZ() * 4096), false);
    ((CraftPlayer) player).getHandle().b.sendPacket(movePacket);
  }

}
