package com.gestankbratwurst.core.mmcore.util.tasks;

import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 03.01.2020
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public class ConsumingBukkitSync<T> extends BukkitRunnable {

  private final T object;
  private final Consumer<T> action;

  @Override
  public void run() {
    this.action.accept(this.object);
  }

}