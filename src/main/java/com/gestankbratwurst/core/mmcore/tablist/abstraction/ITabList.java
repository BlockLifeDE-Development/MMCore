package com.gestankbratwurst.core.mmcore.tablist.abstraction;


import java.util.Set;
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
public interface ITabList {

  default void updateDisplay(final int index, final String display) {
    final ITabLine tabline = this.getLine(index);
    tabline.setDisplay(display);
    for (final PlayerConnection connection : this.getViewers()) {
      tabline.sendDisplayUpdate(connection);
    }
  }

  default void updateTexture(final int index, final String texture, final String signature) {
    final ITabLine tabline = this.getLine(index);
    tabline.setTexture(texture, signature);
    for (final PlayerConnection connection : this.getViewers()) {
      tabline.sendProfileUpdate(connection);
    }
  }

  void addViewer(PlayerConnection connection);

  void removeViewer(PlayerConnection connection);

  Set<PlayerConnection> getViewers();

  int getSize();

  ITabLine getLine(int index);

  void setHeader(String header);

  String getHeader();

  void setFooter(String footer);

  String getFooter();

  void sendHeaderFooter(PlayerConnection connection);

  void addLine(ITabLine line);

  default void broadcastHeaderFooter() {
    for (final PlayerConnection connection : this.getViewers()) {
      this.sendHeaderFooter(connection);
    }
  }

  default void updateAndSendHeaderFooter(final String header, final String footer) {
    this.setHeader(header);
    this.setFooter(footer);
    this.broadcastHeaderFooter();
  }

  default void showTo(final PlayerConnection connection) {
    for (int index = 0; index < this.getSize(); index++) {
      this.getLine(index).send(connection);
    }
    this.sendHeaderFooter(connection);
  }

  default void hideFrom(final PlayerConnection connection) {
    for (int index = 0; index < this.getSize(); index++) {
      this.getLine(index).sendHide(connection);
    }
  }

}