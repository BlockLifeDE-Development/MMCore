package com.gestankbratwurst.core.mmcore.util.json;

import com.gestankbratwurst.core.mmcore.util.common.UtilItem;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 15.11.2020
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ItemStackArraySerializer implements JsonSerializer<ItemStack[]>, JsonDeserializer<ItemStack[]> {


  @Override
  public ItemStack[] deserialize(final JsonElement jsonElement, final Type type,
      final JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    return UtilItem.deserialize(jsonElement.getAsJsonObject().get("Base64Content").getAsString());
  }

  @Override
  public JsonElement serialize(final ItemStack[] itemStacks, final Type type, final JsonSerializationContext jsonSerializationContext) {
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("Base64Content", UtilItem.serialize(itemStacks));
    return jsonObject;
  }
}
