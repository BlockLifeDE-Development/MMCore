package com.gestankbratwurst.core.mmcore.util.tasks;

import java.util.function.Consumer;
import org.bukkit.plugin.java.JavaPlugin;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 03.01.2020
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class BukkitSyncConsumer<T> implements Runnable {

  public BukkitSyncConsumer(final T object, final Consumer<T> action, final JavaPlugin plugin) {
    this.plugin = plugin;
    this.sync = new ConsumingBukkitSync<>(object, action);
  }

  private final JavaPlugin plugin;
  private final ConsumingBukkitSync<T> sync;

  @Override
  public void run() {
    this.sync.runTask(this.plugin);
  }

}
