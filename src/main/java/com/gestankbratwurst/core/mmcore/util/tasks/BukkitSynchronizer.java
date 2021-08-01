package com.gestankbratwurst.core.mmcore.util.tasks;

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
public class BukkitSynchronizer implements Runnable {

  private final BukkitRunnable runnable;

  @Override
  public void run() {
    this.runnable.run();
  }

}