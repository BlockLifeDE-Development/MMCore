package com.gestankbratwurst.core.mmcore.inventories.anvil;

import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.protocol.game.PacketPlayOutCloseWindow;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.IInventory;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.inventory.Container;
import net.minecraft.world.inventory.ContainerAccess;
import net.minecraft.world.inventory.ContainerAnvil;
import net.minecraft.world.inventory.Containers;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Wrapper1_17_R1 implements VersionWrapper {

  private int getRealNextContainerId(final Player player) {
    return this.toNMS(player).nextContainerCounter();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getNextContainerId(final Player player, final Object container) {
    return ((AnvilContainer) container).getContainerId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleInventoryCloseEvent(final Player player) {
    CraftEventFactory.handleInventoryCloseEvent(this.toNMS(player));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendPacketOpenWindow(final Player player, final int containerId, final String guiTitle) {
    this.toNMS(player).b.sendPacket(new PacketPlayOutOpenWindow(containerId, Containers.h, new ChatComponentText(guiTitle)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendPacketCloseWindow(final Player player, final int containerId) {
    this.toNMS(player).b.sendPacket(new PacketPlayOutCloseWindow(containerId));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setActiveContainerDefault(final Player player) {
    (this.toNMS(player)).bV = (Container) (this.toNMS(player)).bU;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setActiveContainer(final Player player, final Object container) {
    (this.toNMS(player)).bV = (Container) container;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setActiveContainerId(final Object container, final int containerId) {
    //noop
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addActiveContainerSlotListener(final Object container, final Player player) {
    this.toNMS(player).initMenu((Container) container);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Inventory toBukkitInventory(final Object container) {
    return ((Container) container).getBukkitView().getTopInventory();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object newContainerAnvil(final Player player, final String guiTitle) {
    return new AnvilContainer(player, guiTitle);
  }

  /**
   * Turns a {@link Player} into an NMS one
   *
   * @param player The player to be converted
   * @return the NMS EntityPlayer
   */
  private EntityPlayer toNMS(final Player player) {
    return ((CraftPlayer) player).getHandle();
  }

  /**
   * Modifications to ContainerAnvil that makes it so you don't have to have xp to use this anvil
   */
  private class AnvilContainer extends ContainerAnvil {

    public AnvilContainer(final Player player, final String guiTitle) {
      super(Wrapper1_17_R1.this.getRealNextContainerId(player),
          ((CraftPlayer) player).getHandle().getInventory(),
          ContainerAccess.at(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(0, 0, 0)));
      this.checkReachable = false;
      this.setTitle(new ChatMessage(guiTitle));
    }

    @Override
    public void i() {
      super.i();
      this.w.set(0);
    }

    @Override
    public void b(final EntityHuman player) {
    }

    @Override
    protected void a(final EntityHuman player, final IInventory container) {
    }

    public int getContainerId() {
      return this.j;
    }
  }
}
