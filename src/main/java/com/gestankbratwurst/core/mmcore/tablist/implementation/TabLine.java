package com.gestankbratwurst.core.mmcore.tablist.implementation;

import com.gestankbratwurst.core.mmcore.tablist.abstraction.ITabLine;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.UUID;
import lombok.Getter;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of AvarionCore and was created at the 10.12.2019
 *
 * AvarionCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class TabLine implements ITabLine {

  public TabLine(final int index) {
    this(UUID.randomUUID(), index, "");
  }

  public TabLine(final int index, final String display) {
    this(UUID.randomUUID(), index, display);
  }

  public TabLine(final UUID playerID, final int index, final String display) {
    final GameProfile profile = new GameProfile(playerID, " " + (char) index);
    final MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
    final WorldServer worldServer = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();

    final EntityPlayer player = new EntityPlayer(server, worldServer, profile);
    player.listName = new ChatMessage(display);

    this.entity = player;
    this.showPacket = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.a, player);
    this.hidePacket = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.e, player);
    this.namePacket = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.d, player);
  }

  @Getter
  private final EntityPlayer entity;
  private PacketPlayOutPlayerInfo showPacket;
  private final PacketPlayOutPlayerInfo hidePacket;
  private final PacketPlayOutPlayerInfo namePacket;

  @Override
  public void setDisplay(final String display) {
    this.entity.listName = new ChatMessage(display);
  }

  @Override
  public String getDisplay() {
    return this.entity.listName.getText();
  }

  @Override
  public void setTexture(final String texture, final String signature) {
    final GameProfile profile = this.entity.getProfile();
    profile.getProperties().put("textures", new Property("textures", texture, signature));
    this.showPacket = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.a, this.entity);
  }

  @Override
  public void setTextureBase64(final String textureBase64) {
    final GameProfile profile = this.entity.getProfile();
    profile.getProperties().removeAll("textures");
    profile.getProperties().put("textures", new Property("textures", textureBase64));
    this.showPacket = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.a, this.entity);
  }

  @Override
  public void send(final PlayerConnection connection) {
    connection.sendPacket(this.showPacket);
  }

  @Override
  public void sendDisplayUpdate(final PlayerConnection connection) {
    connection.sendPacket(this.namePacket);
  }

  @Override
  public void sendProfileUpdate(final PlayerConnection connection) {

  }

  @Override
  public void sendHide(final PlayerConnection connection) {
    connection.sendPacket(this.hidePacket);
  }

}