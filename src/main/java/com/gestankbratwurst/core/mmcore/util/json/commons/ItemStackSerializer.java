package com.gestankbratwurst.core.mmcore.util.json.commons;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.gestankbratwurst.core.mmcore.util.common.UtilItem;
import java.io.IOException;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 08.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ItemStackSerializer extends StdSerializer<CraftItemStack> {


  public ItemStackSerializer() {
    super(CraftItemStack.class);
  }

  @Override
  public void serialize(final CraftItemStack value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
    gen.writeStartObject();

    gen.writeStringField("Item", UtilItem.serialize(value));

    gen.writeEndObject();
  }
}
