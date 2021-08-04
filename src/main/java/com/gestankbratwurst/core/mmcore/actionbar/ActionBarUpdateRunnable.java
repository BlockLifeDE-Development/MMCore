package com.gestankbratwurst.core.mmcore.actionbar;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 28.07.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ActionBarUpdateRunnable implements Runnable {

  protected static final long UPDATE_PERIOD = 10L;

  public ActionBarUpdateRunnable(final ActionBarManager actionBarManager) {
    this.actionBarManager = actionBarManager;
  }

  private final ActionBarManager actionBarManager;

  @Override
  public void run() {
    this.actionBarManager.updateAndShowAll();
  }

}
