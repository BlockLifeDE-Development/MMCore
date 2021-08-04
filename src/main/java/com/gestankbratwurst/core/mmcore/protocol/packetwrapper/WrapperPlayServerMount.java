package com.gestankbratwurst.core.mmcore.protocol.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.World;
import org.bukkit.entity.Entity;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 03.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class WrapperPlayServerMount extends AbstractPacket {

  public static final PacketType TYPE = PacketType.Play.Server.MOUNT;

  public WrapperPlayServerMount() {
    super(new PacketContainer(TYPE), TYPE);
    this.handle.getModifier().writeDefaults();
  }

  public WrapperPlayServerMount(final PacketContainer packet) {
    super(packet, TYPE);
  }

  /**
   * Retrieve Entity ID.
   * <p>
   * Notes: vehicle's EID
   *
   * @return The current Entity ID
   */
  public int getEntityID() {
    return this.handle.getIntegers().read(0);
  }

  /**
   * Retrieve the entity involved in this event.
   *
   * @param world - the current world of the entity.
   * @return The involved entity.
   */
  public Entity getEntity(final World world) {
    return this.handle.getEntityModifier(world).read(0);
  }

  /**
   * Retrieve the entity involved in this event.
   *
   * @param event - the packet event.
   * @return The involved entity.
   */
  public Entity getEntity(final PacketEvent event) {
    return this.getEntity(event.getPlayer().getWorld());
  }

  /**
   * Set Entity ID.
   *
   * @param value - new value.
   */
  public void setEntityID(final int value) {
    this.handle.getIntegers().write(0, value);
  }

  public int[] getPassengerIds() {
    return this.handle.getIntegerArrays().read(0);
  }

  public void setPassengerIds(final int[] value) {
    this.handle.getIntegerArrays().write(0, value);
  }

  public List<Entity> getPassengers(final PacketEvent event) {
    return this.getPassengers(event.getPlayer().getWorld());
  }

  public List<Entity> getPassengers(final World world) {
    final int[] ids = this.getPassengerIds();
    final List<Entity> passengers = new ArrayList<>();
    final ProtocolManager manager = ProtocolLibrary.getProtocolManager();

    for (final int id : ids) {
      final Entity entity = manager.getEntityFromID(world, id);
      if (entity != null) {
        passengers.add(entity);
      }
    }

    return passengers;
  }

  public void setPassengers(final List<Entity> value) {
    final int[] array = new int[value.size()];
    for (int i = 0; i < value.size(); i++) {
      array[i] = value.get(i).getEntityId();
    }

    this.setPassengerIds(array);
  }
}