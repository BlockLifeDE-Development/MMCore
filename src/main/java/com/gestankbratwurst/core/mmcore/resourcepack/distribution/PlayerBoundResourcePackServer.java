package com.gestankbratwurst.core.mmcore.resourcepack.distribution;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 01.03.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class PlayerBoundResourcePackServer extends ResourcePackServer {

  private final Logger logger;
  private final File resourcePack;
  private final String host;
  private final String fileLocation;

  public PlayerBoundResourcePackServer(final String host, final int port, final String fileLocation, final Logger logger,
      final File resourcePack)
      throws IOException {
    super(port);
    this.logger = logger;
    this.resourcePack = resourcePack;
    this.host = host;
    this.fileLocation = fileLocation;
  }

  public String getDownloadURL() {
    return "http://" + this.host + ":" + this.port + this.fileLocation;
  }

  public String getFileHashChecksum() {
    try {
      return Files.hash(this.resourcePack, Hashing.sha1()).toString();
    } catch (final IOException e) {
      this.logger.severe("Failed to calculate resourcepack hashcode - " + e.getMessage());
      return null;
    }
  }

  @Override
  public File requestFileCallback(final ResourceServerConnection connection, final String request) {
    final Player player = this.resolvePlayerFromConnection(connection);

    if (player == null) {
      // Connection from unknown IP, refuse connection.
      return null;
    }
    this.logger.info("Connection " + connection.getClient().getInetAddress() + " is requesting + " + request);
    // Return the .zip file
    return this.resourcePack;
  }

  @Override
  public void onSuccessfulRequest(final ResourceServerConnection connection, final String request) {
    this.logger.info("Successfully served " + request + " to " + connection.getClient().getInetAddress().getHostAddress());
  }

  @Override
  public void onClientRequest(final ResourceServerConnection connection, final String request) {
    this.logger.info(connection.getClient().getInetAddress() + " is requesting the resourcepack");
  }

  @Override
  public void onRequestError(final ResourceServerConnection connection, final int code) {
    this.logger.info("Error " + code + " while attempting to serve " + connection.getClient().getInetAddress().getHostAddress());
  }

  private Player resolvePlayerFromConnection(final ResourceServerConnection connection) {
    final byte[] ip = connection.getClient().getInetAddress().getAddress();

    for (final Player player : Bukkit.getOnlinePlayers()) {
      if (Arrays.equals(player.getAddress().getAddress().getAddress(), ip)) {
        return player;
      }
    }
    return null;
  }
}
