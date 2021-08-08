package com.gestankbratwurst.core.mmcore;

import com.gestankbratwurst.core.mmcore.data.model.AgnosticDataDomain;
import com.gestankbratwurst.core.mmcore.data.model.DataManager;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
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
    final AgnosticDataDomain<String> dataDomain = MMCore.getDataManager().getOrCreateDataDomain("Items", DataManager.STRING_SWAP);
    System.out.println("Get/Create Holder.");

    for (int i = 0; i < 10; i++) {
      dataDomain.getOrCreateDataHolder("Item " + i).join();
    }

    for (int i = 0; i < 10; i++) {
      dataDomain.applyToDataHolder("Item " + i, holder -> {
        if (!holder.containsKey(DataObject.class)) {
          final ThreadLocalRandom random = ThreadLocalRandom.current();
          holder.putData(DataObject.class, new DataObject(new ItemStack(Material.values()[random.nextInt(Material.values().length)])));
        }
        return holder;
      }).join();
    }

    for (int i = 0; i < 10; i++) {
      System.out.println(dataDomain.getOrCreateDataHolder("Item " + i).join().getData(DataObject.class));
    }

    System.out.println("Done.");
  }

}
