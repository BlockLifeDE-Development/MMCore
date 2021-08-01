package com.gestankbratwurst.core.mmcore.util.json;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Type;
import java.util.Map.Entry;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 04.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class MultimapSerializer implements JsonSerializer<Multimap<?, ?>>, JsonDeserializer<Multimap<?, ?>> {

  @Override
  public Multimap<?, ?> deserialize(final JsonElement jsonElement, final Type type,
      final JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    final Multimap<Object, Object> multimap = LinkedHashMultimap.create();

    final JsonObject jsonObject = jsonElement.getAsJsonObject();

    for (final Entry<String, JsonElement> entry : jsonObject.entrySet()) {
      multimap.put(entry.getKey(), jsonDeserializationContext.deserialize(entry.getValue(), Property.class));
    }

    return multimap;
  }

  @Override
  public JsonElement serialize(final Multimap<?, ?> multimap, final Type type,
      final JsonSerializationContext jsonSerializationContext) {
    final JsonObject jsonObject = new JsonObject();
    for (final Entry<?, ?> entry : multimap.entries()) {
      jsonObject.add(entry.getKey().toString(), jsonSerializationContext.serialize(entry.getValue()));
    }
    return jsonObject;
  }

}
