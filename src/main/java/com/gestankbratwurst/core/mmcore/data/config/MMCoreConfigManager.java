package com.gestankbratwurst.core.mmcore.data.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 06.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class MMCoreConfigManager {

  @Getter(AccessLevel.PROTECTED)
  private static MMCoreConfiguration configuration;

  public static void init(final JavaPlugin plugin) {
    createMainFolderIfNotExist(plugin);
    try {
      loadAndMergeConfig(plugin);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  private static void createMainFolderIfNotExist(final JavaPlugin plugin) {
    final File folder = plugin.getDataFolder();
    if (!folder.exists()) {
      folder.mkdirs();
    }
  }

  private static void loadAndMergeConfig(final JavaPlugin plugin) throws IOException {
    final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    final File configFile = new File(plugin.getDataFolder(), "configuration.yml");
    if (configFile.exists()) {
      final MMCoreConfiguration defaultConfig = new MMCoreConfiguration();
      final ObjectReader updateReader = mapper.readerForUpdating(defaultConfig);
      final MMCoreConfiguration mergedConfig = updateReader.readValue(configFile);
      MMCoreConfigManager.configuration = mergedConfig;
      mapper.writerWithDefaultPrettyPrinter().writeValue(configFile, mergedConfig);
    } else {
      final MMCoreConfiguration configuration = new MMCoreConfiguration();
      mapper.writeValue(configFile, configuration);
      MMCoreConfigManager.configuration = configuration;
    }
  }

}
