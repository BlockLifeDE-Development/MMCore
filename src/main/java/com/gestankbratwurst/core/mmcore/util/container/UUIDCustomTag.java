package com.gestankbratwurst.core.mmcore.util.container;

import java.util.UUID;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 06.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UUIDCustomTag implements PersistentDataType<String, UUID> {

  @Override
  public Class<String> getPrimitiveType() {
    return String.class;
  }

  @Override
  public Class<UUID> getComplexType() {
    return UUID.class;
  }

  @Override
  public String toPrimitive(final UUID uuid, final PersistentDataAdapterContext persistentDataAdapterContext) {
    return uuid.toString();
  }

  @Override
  public UUID fromPrimitive(final String str, final PersistentDataAdapterContext persistentDataAdapterContext) {
    return UUID.fromString(str);
  }
}
