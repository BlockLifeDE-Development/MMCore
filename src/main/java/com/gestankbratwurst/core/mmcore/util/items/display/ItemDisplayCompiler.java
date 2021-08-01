package com.gestankbratwurst.core.mmcore.util.items.display;

import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.gestankbratwurst.core.mmcore.util.nbtapi.NBTItem;
import com.gestankbratwurst.core.mmcore.util.nbtapi.NBTStringList;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.function.Function;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ItemDisplayCompiler extends PacketAdapter implements Function<ItemStack, ItemStack> {

  public static final String NBT_KEY = "CompileDisplay";

  // TODO NBT -> PDC

  public static void addDisplayCompileKey(final String key, final ItemStack item) {
    addDisplayCompileKey(key, new NBTItem(item));
  }

  public static void addDisplayCompileKey(final String key, final NBTItem nbt) {
    final NBTStringList compilerList;
    if (nbt.hasKey(NBT_KEY)) {
      compilerList = nbt.getStringList(NBT_KEY);
    } else {
      compilerList = nbt.createStringList(NBT_KEY);
    }

    compilerList.add(key);
  }


  private final Object2ObjectOpenHashMap<String, DisplayConverter> compilerMap;
  private Player playerToModify;


  public ItemDisplayCompiler(final Plugin plugin) {
    super(plugin, Server.WINDOW_ITEMS, Server.SET_SLOT);
    this.compilerMap = new Object2ObjectOpenHashMap<>();
  }

  @Override
  public void onPacketSending(final PacketEvent event) {
    final PacketContainer packet = event.getPacket().deepClone();
    this.playerToModify = event.getPlayer();
    if (event.getPacketType() == Server.WINDOW_ITEMS) {
      final StructureModifier<ItemStack[]> structMod = packet.getItemArrayModifier();
      for (int index = 0; index < structMod.size(); index++) {
        final ItemStack[] itemArray = structMod.read(index);
        for (int i = 0; i < itemArray.length; i++) {
          itemArray[i] = this.apply(itemArray[i]);
        }
      }
    } else {
      final StructureModifier<ItemStack> structMod = packet.getItemModifier();
      for (int index = 0; index < structMod.size(); index++) {
        structMod.modify(index, this::apply);
      }
    }
    event.setPacket(packet);
  }

  public void registerConverter(final DisplayConverter converter) {
    this.compilerMap.put(converter.getDisplayKey(), converter);
  }

  @Override
  public ItemStack apply(final ItemStack itemStack) {
    if (itemStack == null) {
      return null;
    }
    ItemStack clone = itemStack.clone();
    final NBTItem nbt = new NBTItem(clone);
    if (!nbt.hasKey(NBT_KEY)) {
      return itemStack;
    }
    final NBTStringList displayKeys = nbt.getStringList(NBT_KEY);
    for (final String key : displayKeys) {
      final DisplayConverter converter = this.compilerMap.get(key);
      if (converter != null) {
        clone = converter.compileInfo(this.playerToModify, clone).getResult();
      }
    }
    return clone;
  }
}