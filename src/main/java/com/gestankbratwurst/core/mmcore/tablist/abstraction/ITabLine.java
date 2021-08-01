package com.gestankbratwurst.core.mmcore.tablist.abstraction;

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
public interface ITabLine {

  void setDisplay(String display);

  String getDisplay();

  void setTexture(String texture, String signature);

  void setTextureBase64(String textureBase64);

  void send(PlayerConnection connection);

  void sendDisplayUpdate(PlayerConnection connection);

  void sendProfileUpdate(PlayerConnection connection);

  void sendHide(PlayerConnection connection);

}