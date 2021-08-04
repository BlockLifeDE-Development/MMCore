package com.gestankbratwurst.core.mmcore;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.metadata.LazyMetadataValue;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 01.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class TutorialStuff implements Listener {

  public static void enable() {
    Bukkit.getPluginManager().registerEvents(new TutorialStuff(), MMCore.getInstance());
    LazyMetadataValue m;
  }

}
