package com.gestankbratwurst.core.mmcore.util.json;

import com.gestankbratwurst.core.mmcore.util.common.UtilVect;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 08.03.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class BoundingBoxSerializer implements JsonSerializer<BoundingBox>, JsonDeserializer<BoundingBox> {

  @Override
  public BoundingBox deserialize(final JsonElement jsonElement, final Type type,
      final JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    final JsonObject jsonObject = jsonElement.getAsJsonObject();
    final Vector max = UtilVect.vecFromString(jsonObject.get("Max").getAsString());
    final Vector min = UtilVect.vecFromString(jsonObject.get("Min").getAsString());
    return BoundingBox.of(max, min);
  }

  @Override
  public JsonElement serialize(final BoundingBox boundingBox, final Type type, final JsonSerializationContext jsonSerializationContext) {
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("Max", UtilVect.vecToString(boundingBox.getMax()));
    jsonObject.addProperty("Min", UtilVect.vecToString(boundingBox.getMin()));
    return jsonObject;
  }
}
