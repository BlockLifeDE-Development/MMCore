package com.gestankbratwurst.core.mmcore;

import com.gestankbratwurst.core.mmcore.data.model.IndexableObject;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 06.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@NoArgsConstructor
@AllArgsConstructor
public class DataObject implements IndexableObject<UUID> {

  private final UUID uid = UUID.randomUUID();
  @Getter
  @Setter
  private ItemStack itemStack;

  @Override
  public String getIndexedField() {
    return "uid";
  }

  @Override
  public UUID getFieldValue() {
    return this.uid;
  }

  @Override
  public String toString() {
    return this.itemStack.toString();
  }

}
