package com.gestankbratwurst.core.mmcore.util.nbtapi;

import java.util.Set;
import java.util.UUID;
import net.minecraft.nbt.NBTTagCompound;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 28.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class NBTCompound implements Cloneable {

  public NBTCompound() {
    this(new NBTTagCompound());
  }

  public NBTCompound(final NBTTagCompound nmsCompound) {
    this.nmsCompound = nmsCompound;
  }

  protected final NBTTagCompound nmsCompound;

  public void setString(final String key, final String value) {
    this.nmsCompound.setString(key, value);
  }

  public void setByte(final String key, final byte value) {
    this.nmsCompound.setByte(key, value);
  }

  public void setShort(final String key, final short value) {
    this.nmsCompound.setShort(key, value);
  }

  public void setInt(final String key, final int value) {
    this.nmsCompound.setInt(key, value);
  }

  public void setLong(final String key, final long value) {
    this.nmsCompound.setLong(key, value);
  }

  public void setFloat(final String key, final float value) {
    this.nmsCompound.setFloat(key, value);
  }

  public void setDouble(final String key, final double value) {
    this.nmsCompound.setDouble(key, value);
  }

  public void setBoolean(final String key, final boolean value) {
    this.nmsCompound.setBoolean(key, value);
  }

  public void setByteArray(final String key, final byte[] value) {
    this.nmsCompound.setByteArray(key, value);
  }

  public void setIntArray(final String key, final int[] value) {
    this.nmsCompound.setIntArray(key, value);
  }

  public void setUUID(final String key, final UUID value) {
    this.nmsCompound.setString(key, value.toString());
  }

  public void remove(final String key) {
    this.nmsCompound.remove(key);
  }

  public void addCompound(final String key, final NBTCompound compound) {
    this.nmsCompound.set(key, compound.nmsCompound);
  }

  public NBTCompound createCompound(final String key) {
    final NBTCompound compound = new NBTCompound();
    this.nmsCompound.set(key, compound.nmsCompound);
    return compound;
  }

  public NBTCompound getCompound(final String key) {
    if (!this.hasKey(key)) {
      return null;
    }
    final NBTCompound compound = new NBTCompound(this.nmsCompound.getCompound(key));
    return compound;
  }

  public Set<String> getKeys() {
    return this.nmsCompound.getKeys();
  }

  public boolean hasKey(final String key) {
    return this.nmsCompound.hasKey(key);
  }

  public String getString(final String key) {
    return this.nmsCompound.getString(key);
  }

  public int getInt(final String key) {
    return this.nmsCompound.getInt(key);
  }

  public short getShort(final String key) {
    return this.nmsCompound.getShort(key);
  }

  public byte getByte(final String key) {
    return this.nmsCompound.getByte(key);
  }

  public long getLong(final String key) {
    return this.nmsCompound.getLong(key);
  }

  public float getFloat(final String key) {
    return this.nmsCompound.getFloat(key);
  }

  public double getDouble(final String key) {
    return this.nmsCompound.getDouble(key);
  }

  public boolean getBoolean(final String key) {
    return this.nmsCompound.getBoolean(key);
  }

  public byte[] getByteArray(final String key) {
    return this.nmsCompound.getByteArray(key);
  }

  public int[] getIntArray(final String key) {
    return this.nmsCompound.getIntArray(key);
  }

  public void addStringList(final String key, final NBTStringList list) {
    this.nmsCompound.set(key, list.nmsList);
  }

  public NBTStringList createStringList(final String key) {
    final NBTStringList list = new NBTStringList();
    this.addStringList(key, list);
    return list;
  }

  public NBTStringList getStringList(final String key) {
    return new NBTStringList(this.nmsCompound.getList(key, 8));
  }

  @Override
  public NBTCompound clone() {
    return new NBTCompound(this.nmsCompound.clone());
  }

}
