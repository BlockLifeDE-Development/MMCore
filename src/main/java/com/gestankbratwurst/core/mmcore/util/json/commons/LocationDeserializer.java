package com.gestankbratwurst.core.mmcore.util.json.commons;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 08.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class LocationDeserializer extends StdDeserializer<Location> {

  public LocationDeserializer() {
    super(Location.class);
  }

  @Override
  public Location deserialize(final JsonParser parser, final DeserializationContext ctxt) throws IOException {
    final JsonNode node = parser.getCodec().readTree(parser);

    final double x = node.get("x").asDouble();
    final double y = node.get("y").asDouble();
    final double z = node.get("z").asDouble();
    final float pitch = (float) node.get("pitch").asDouble();
    final float yaw = (float) node.get("yaw").asDouble();
    final World world = Bukkit.getWorld(UUID.fromString(node.get("worldID").asText()));

    return new Location(world, x, y, z, pitch, yaw);
  }
}
