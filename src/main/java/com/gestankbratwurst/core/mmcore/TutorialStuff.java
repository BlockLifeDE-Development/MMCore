package com.gestankbratwurst.core.mmcore;

import com.gestankbratwurst.core.mmcore.data.model.AgnosticDataDomain;
import com.gestankbratwurst.core.mmcore.data.model.DataManager;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

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
    redisBomb();
  }

  @EventHandler
  public void onOpen(final InventoryOpenEvent event) {

  }

  private static void redisBomb() {
    System.out.println("Start redis insertion.");
    final AgnosticDataDomain<String> dataDomain = MMCore.getDataManager().getOrCreateDataDomain("Worlds", DataManager.STRING_SWAP);
    System.out.println("Get/Create Holder.");

    for (int i = 0; i < 5; i++) {
      dataDomain.getOrCreateDataHolder("MainWorld " + i).join();
      dataDomain.applyToDataHolder("MainWorld " + i, holder -> {
        if (!holder.containsKey(DataObject.class)) {
          final ThreadLocalRandom random = ThreadLocalRandom.current();
          final Location location = new Location(Bukkit.getWorlds().get(0), random.nextDouble(-2000, 2000), random.nextInt(10, 90),
              random.nextInt(-1500, 1000));
          final Material material = Material.values()[random.nextInt(Material.values().length)];
          final DataObject dataObject = new DataObject(location, new ItemStack(material), random.nextInt(50));
          holder.putData(DataObject.class, dataObject);
        }
        return holder;
      }).join();
    }

    for (int i = 0; i < 5; i++) {
      System.out.println(dataDomain.getOrCreateDataHolder("MainWorld " + i).join().getData(DataObject.class));
    }

    System.out.println("Done.");
  }

}
