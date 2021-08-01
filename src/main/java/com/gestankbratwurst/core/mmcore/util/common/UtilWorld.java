package com.gestankbratwurst.core.mmcore.util.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.World;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 13.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UtilWorld {

  public static List<String> getUnloadedWorlds() {
    final Set<String> loaded = Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toSet());
    final List<String> unloaded = new ArrayList<>();

    final File[] worldFolders = Bukkit.getWorldContainer().listFiles();
    if (worldFolders == null) {
      return unloaded;
    }

    for (final File folder : worldFolders) {
      if (!shallowWorldCheck(folder)) {
        continue;
      }
      final String name = folder.getName();
      if (!loaded.contains(name)) {
        unloaded.add(name);
      }
    }

    return unloaded;
  }

  private static boolean shallowWorldCheck(final File folder) {
    return folder.isDirectory() && new File(folder, "level.dat").exists();
  }

}
