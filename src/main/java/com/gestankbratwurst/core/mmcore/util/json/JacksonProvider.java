package com.gestankbratwurst.core.mmcore.util.json;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.SneakyThrows;
import org.redisson.codec.JsonJacksonCodec;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 07.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class JacksonProvider {

  @Getter
  private static final JsonJacksonCodec codec = new JsonJacksonCodec();
  private static final SimpleModule module = new SimpleModule();
  private static final ObjectMapper objectMapper = codec.getObjectMapper();

  @SneakyThrows
  public static <T> String serialize(final T object) {
    return objectMapper.registerModule(module).writeValueAsString(object);
  }

  @SneakyThrows
  public static <T> T deserialize(final String json, final Class<T> tClass) {
    return objectMapper.registerModule(module).readValue(json, tClass);
  }

  public static <T> void register(final Class<T> tClass, final JsonSerializer<T> serializer,
      final JsonDeserializer<? extends T> deserializer) {
    module.addSerializer(serializer);
    module.addDeserializer(tClass, deserializer);
    objectMapper.registerModule(module);
  }

  public static void applyToModule(final Consumer<SimpleModule> moduleConsumer) {
    moduleConsumer.accept(module);
    objectMapper.registerModule(module);
  }

  public static void applyToMapper(final Consumer<ObjectMapper> mapperConsumer) {
    mapperConsumer.accept(objectMapper);
    objectMapper.registerModule(module);
  }

}
