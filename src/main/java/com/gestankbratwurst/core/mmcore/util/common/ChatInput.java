package com.gestankbratwurst.core.mmcore.util.common;

import com.gestankbratwurst.core.mmcore.util.tasks.TaskManager;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 08.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ChatInput implements Listener {

  private static final Map<UUID, Consumer<String>> PROMPTS = new HashMap<>();

  public static void init(final JavaPlugin host) {
    Bukkit.getPluginManager().registerEvents(new ChatInput(), host);
  }

  public static void create(final Player player, final Consumer<String> consumer) {
    PROMPTS.put(player.getUniqueId(), consumer);
  }

  @EventHandler
  public void onChat(final AsyncPlayerChatEvent event) {
    final String msg = event.getMessage();
    final UUID playerID = event.getPlayer().getUniqueId();
    if (PROMPTS.containsKey(playerID)) {
      TaskManager.getInstance().runBukkitSync(() -> PROMPTS.remove(playerID).accept(msg));
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    final UUID playerID = event.getPlayer().getUniqueId();
    if (PROMPTS.containsKey(playerID)) {
      TaskManager.getInstance().runBukkitSync(() -> PROMPTS.remove(playerID));
    }
  }

}
