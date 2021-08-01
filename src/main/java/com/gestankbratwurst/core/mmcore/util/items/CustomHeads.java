package com.gestankbratwurst.core.mmcore.util.items;

import com.gestankbratwurst.core.mmcore.util.common.UtilItem;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 30.01.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public enum CustomHeads {

  END_GLOBE(CustomHeadType.BASE64,
      "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzZjYWM1OWIyYWFlNDg5YWEwNjg3YjVkODAyYjI1NTVlYjE0YTQwYmQ2MmIyMWViMTE2ZmE1NjljZGI3NTYifX19"),
  NETHER_GLOBE(CustomHeadType.BASE64,
      "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDgzNTcxZmY1ODlmMWE1OWJiMDJiODA4MDBmYzczNjExNmUyN2MzZGNmOWVmZWJlZGU4Y2YxZmRkZSJ9fX0="),
  GLOBE(CustomHeadType.BASE64,
      "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTFiMzE4OGZkNDQ5MDJmNzI2MDJiZDdjMjE0MWY1YTcwNjczYTQxMWFkYjNkODE4NjJjNjllNTM2MTY2YiJ9fX0=");

  private final CustomHeadType type;
  private final String value;

  public ItemStack getItem() {
    return this.type.produce(this.toString(), this.value);
  }

  public enum CustomHeadType {
    PLAYER {
      @Override
      public ItemStack produce(final String from, final String data) {
        return UtilItem.getHeadFromName(data);
      }
    },
    BASE64 {
      @Override
      public ItemStack produce(final String from, final String data) {
        return UtilItem.getHeadFromBase64(from, data);
      }
    };

    public abstract ItemStack produce(final String from, final String data);

  }

}
