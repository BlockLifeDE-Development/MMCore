package com.gestankbratwurst.core.mmcore.util.json.commons;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.bukkit.Location;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 08.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class LocationSerializer extends StdSerializer<Location> {

  public LocationSerializer() {
    super(Location.class);
  }

  @Override
  public void serialize(final Location value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeNumberField("x", value.getX());
    gen.writeNumberField("y", value.getY());
    gen.writeNumberField("z", value.getZ());
    gen.writeNumberField("pitch", value.getPitch());
    gen.writeNumberField("yaw", value.getYaw());
    gen.writeStringField("worldID", value.getWorld().getUID().toString());
    gen.writeEndObject();
  }
}
