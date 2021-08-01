package com.gestankbratwurst.core.mmcore.util.common;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 31.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class NamespaceFactory {

  public static void init(final JavaPlugin plugin) {
    if (instance == null) {
      instance = new NamespaceFactory(plugin);
    }
  }

  private static NamespaceFactory instance;

  private NamespaceFactory(final JavaPlugin plugin) {
    this.cachedKeys = new Object2ObjectOpenHashMap<>();
    this.plugin = plugin;
  }

  private final JavaPlugin plugin;
  private final Map<String, NamespacedKey> cachedKeys;

  public static NamespacedKey provide(final String key) {
    NamespacedKey nsk = instance.cachedKeys.get(key);
    if (nsk == null) {
      nsk = new NamespacedKey(instance.plugin, key);
      instance.cachedKeys.put(key, nsk);
    }
    return nsk;
  }

}
