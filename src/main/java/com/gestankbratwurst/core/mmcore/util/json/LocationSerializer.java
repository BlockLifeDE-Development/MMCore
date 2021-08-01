package com.gestankbratwurst.core.mmcore.util.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 24.01.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class LocationSerializer implements JsonSerializer<Location>, JsonDeserializer<Location> {

  @Override
  public Location deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    final JsonObject jsonObject = jsonElement.getAsJsonObject();
    final World world = Bukkit.getWorld(UUID.fromString(jsonObject.get("WorldID").getAsString()));
    final JsonObject positionObject = jsonObject.get("Position").getAsJsonObject();
    final JsonObject rotationObject = jsonObject.get("Rotation").getAsJsonObject();
    final double x = positionObject.get("X").getAsDouble();
    final double y = positionObject.get("Y").getAsDouble();
    final double z = positionObject.get("Z").getAsDouble();
    final float pitch = rotationObject.get("Pitch").getAsFloat();
    final float yaw = rotationObject.get("Yaw").getAsFloat();

    return new Location(world, x, y, z, pitch, yaw);
  }

  @Override
  public JsonElement serialize(final Location location, final Type type, final JsonSerializationContext jsonSerializationContext) {
    final JsonObject jsonObject = new JsonObject();

    jsonObject.addProperty("WorldID", location.getWorld().getUID().toString());
    final JsonObject positionObject = new JsonObject();
    positionObject.addProperty("X", location.getX());
    positionObject.addProperty("Y", location.getY());
    positionObject.addProperty("Z", location.getZ());
    jsonObject.add("Position", positionObject);
    final JsonObject rotationObject = new JsonObject();
    rotationObject.addProperty("Pitch", location.getPitch());
    rotationObject.addProperty("Yaw", location.getYaw());
    jsonObject.add("Rotation", rotationObject);

    return jsonObject;
  }
}
