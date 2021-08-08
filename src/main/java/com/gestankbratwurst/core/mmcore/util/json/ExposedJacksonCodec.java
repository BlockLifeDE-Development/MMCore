package com.gestankbratwurst.core.mmcore.util.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTypeResolverBuilder;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.xml.datatype.XMLGregorianCalendar;
import org.bukkit.plugin.java.PluginClassLoader;
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

  private Set<Class<?>> excludedTypesForTypeResolver;

  public ExposedJacksonCodec() {
    super(PluginClassLoader.getPlatformClassLoader());
  }

  protected void addExcludedTypeForTypeResolver(final Class<?> clazz) {
    this.excludedTypesForTypeResolver.add(clazz);
  }

  @Override
  protected void init(final ObjectMapper objectMapper) {
    this.excludedTypesForTypeResolver = new HashSet<>(Arrays.asList(
        JsonNode.class,
        XMLGregorianCalendar.class
    ));
    objectMapper.setSerializationInclusion(Include.NON_NULL);
    objectMapper.setVisibility(objectMapper.getSerializationConfig()
        .getDefaultVisibilityChecker()
        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
        .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
        .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    objectMapper.enable(Feature.WRITE_BIGDECIMAL_AS_PLAIN);
    objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    objectMapper.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
    objectMapper.addMixIn(Throwable.class, ThrowableMixIn.class);
  }


  @Override
  protected void initTypeInclusion(final ObjectMapper mapObjectMapper) {
    final TypeResolverBuilder<?> mapTyper = new DefaultTypeResolverBuilder(DefaultTyping.NON_FINAL, LaissezFaireSubTypeValidator.instance) {
      @Override
      public boolean useForType(JavaType javaType) {
        while (javaType.isArrayType()) {
          javaType = javaType.getContentType();
        }
        final Class<?> rawClass = javaType.getRawClass();
        // to fix problem with wrong long to int conversion
        if (rawClass == Long.class) {
          return true;
        }
        if (ExposedJacksonCodec.this.excludedTypesForTypeResolver.contains(rawClass)) {
          return false;
        }
        return !javaType.isFinal(); // includes Object.class
      }
    };
    mapTyper.init(JsonTypeInfo.Id.CLASS, null);
    mapTyper.inclusion(JsonTypeInfo.As.PROPERTY);
    mapObjectMapper.setDefaultTyping(mapTyper);
  }


}
