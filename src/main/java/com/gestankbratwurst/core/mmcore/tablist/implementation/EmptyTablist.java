package com.gestankbratwurst.core.mmcore.tablist.implementation;

import com.gestankbratwurst.core.mmcore.tablist.TabListManager;
import com.gestankbratwurst.core.mmcore.tablist.abstraction.ITabLine;
import com.gestankbratwurst.core.mmcore.tablist.abstraction.ITabList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Set;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.network.PlayerConnection;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of AvarionCore and was created at the 10.12.2019
 *
 * AvarionCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class EmptyTablist implements ITabList {

  public EmptyTablist(final TabListManager tabListManager) {
    this.playerConnectionSet = Sets.newHashSet();
    this.tabs = Lists.newArrayList();
    this.tabListManager = tabListManager;
    this.headerFooterPacket = new PacketPlayOutPlayerListHeaderFooter(new ChatMessage("EMPTY_HEADER"), new ChatMessage("EMPTY_FOOTER"));
  }

  private final TabListManager tabListManager;
  private final Set<PlayerConnection> playerConnectionSet;
  protected final ArrayList<ITabLine> tabs;
  private PacketPlayOutPlayerListHeaderFooter headerFooterPacket;

  @Override
  public void addViewer(final PlayerConnection connection) {
    this.playerConnectionSet.add(connection);
  }

  @Override
  public void removeViewer(final PlayerConnection connection) {
    this.playerConnectionSet.remove(connection);
  }

  @Override
  public Set<PlayerConnection> getViewers() {
    return this.playerConnectionSet;
  }

  @Override
  public int getSize() {
    return this.tabs.size();
  }

  @Override
  public ITabLine getLine(final int index) {
    return this.tabs.get(index);
  }

  @Override
  public void setHeader(final String header) {
    this.headerFooterPacket = new PacketPlayOutPlayerListHeaderFooter(new ChatMessage(header), this.headerFooterPacket.b);
  }

  @Override
  public String getHeader() {
    return this.headerFooterPacket.a.getText();
  }

  @Override
  public void setFooter(final String footer) {
    this.headerFooterPacket = new PacketPlayOutPlayerListHeaderFooter(this.headerFooterPacket.a, new ChatMessage(footer));
  }

  @Override
  public String getFooter() {
    return this.headerFooterPacket.b.getText();
  }

  @Override
  public void sendHeaderFooter(final PlayerConnection connection) {
    connection.sendPacket(this.headerFooterPacket);
  }

  @Override
  public void addLine(final ITabLine line) {
    this.tabs.add(line);
  }

}