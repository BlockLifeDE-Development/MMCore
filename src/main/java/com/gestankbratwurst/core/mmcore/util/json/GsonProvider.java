package com.gestankbratwurst.core.mmcore.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapterFactory;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 23.10.2020
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class GsonProvider {

  private static final Map<Type, Object> REGISTERED_ADAPTERS = new Object2ObjectOpenHashMap<>();
  private static final Set<TypeAdapterFactory> REGISTERED_FACTORIES = new ObjectOpenHashSet<>();
  private static Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
  private static boolean CHANGED = false;

  private static Gson build() {
    final GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
    builder.setPrettyPrinting();
    builder.enableComplexMapKeySerialization();
    for (final Entry<Type, Object> entry : GsonProvider.REGISTERED_ADAPTERS.entrySet()) {
      builder.registerTypeAdapter(entry.getKey(), entry.getValue());
    }

    for (final TypeAdapterFactory factory : GsonProvider.REGISTERED_FACTORIES) {
      builder.registerTypeAdapterFactory(factory);
    }

    return builder.create();
  }


  public static void register(final Type type, final Object typeAdapter) {
    GsonProvider.REGISTERED_ADAPTERS.put(type, typeAdapter);
    GsonProvider.CHANGED = true;
  }

  public static void register(final TypeAdapterFactory adapterFactory) {
    GsonProvider.REGISTERED_FACTORIES.add(adapterFactory);
    GsonProvider.CHANGED = true;
  }

  public static Gson get() {
    if (GsonProvider.CHANGED) {
      GsonProvider.GSON = GsonProvider.build();
      GsonProvider.CHANGED = false;
    }
    return GsonProvider.GSON;
  }

  public static <T> T fromJson(final String json, final Class<T> clazz) {
    return GsonProvider.get().fromJson(json, clazz);
  }

  public static String toJson(final Object object) {
    return GsonProvider.get().toJson(object);
  }

  public static JsonElement toJsonTree(final Object object) {
    return GsonProvider.get().toJsonTree(object);
  }

}