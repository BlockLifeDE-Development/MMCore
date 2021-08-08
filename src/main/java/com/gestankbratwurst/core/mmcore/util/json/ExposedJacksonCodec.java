package com.gestankbratwurst.core.mmcore.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.codec.JsonJacksonCodec;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 08.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ExposedJacksonCodec extends JsonJacksonCodec {

  public ObjectMapper getMapper() {
    return this.mapObjectMapper;
  }

}
