package com.gestankbratwurst.core.mmcore.util.json.commons;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
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
public class ItemStackDeserializer extends StdDeserializer<CraftItemStack> {


  public ItemStackDeserializer() {
    super(CraftItemStack.class);
  }

  @Override
  public CraftItemStack deserialize(final JsonParser parser, final DeserializationContext ctxt) throws IOException {
    final JsonNode node = parser.getCodec().readTree(parser);
    final String value = node.get("Item").asText();
    return (CraftItemStack) UtilItem.deserializeItemStack(value);
  }
}
