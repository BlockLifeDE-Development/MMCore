package com.gestankbratwurst.core.mmcore.data.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 05.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@SuppressWarnings("unchecked")
public class AgnosticDataHolder implements IndexableObject<String> {

  @Setter
  @Getter
  private String primaryKey;
  private final Map<Class<?>, Object> arbitraryDataMap = new HashMap<>();

  public AgnosticDataHolder(final String primaryKey) {
    this.primaryKey = primaryKey;
  }

  protected AgnosticDataHolder() {

  }

  public <T> boolean containsKey(final Class<T> dataClass) {
    return this.arbitraryDataMap.containsKey(dataClass);
  }

  /**
   * Puts the data associated with that class into the Map. This only supports one Object per class.
   *
   * @param dataClass    The data Class
   * @param dataInstance The data Object
   * @param <T>          The type of the Object
   * @see java.util.Map#put(Object, Object)
   */
  public <T> void putData(final Class<T> dataClass, final T dataInstance) {
    this.arbitraryDataMap.put(dataClass, dataInstance);
  }

  /**
   * Removes the data associated with that class from the Map.
   *
   * @param dataClass The data Class
   * @param <T>       The type of the Object
   * @return The previous value or null
   * @see java.util.Map#remove(Object)
   */
  public <T> T removeData(final Class<T> dataClass) {
    return (T) this.arbitraryDataMap.remove(dataClass);
  }

  /**
   * Retrieves the data associated with that class from the Map.
   *
   * @param dataClass The data Class
   * @param <T>       The type of the Object
   * @return The current value or null
   * @see java.util.Map#get(Object)
   */
  public <T> T getData(final Class<T> dataClass) {
    final Object data = this.arbitraryDataMap.get(dataClass);
    return data == null ? null : (T) data;
  }

  public Set<Class<?>> getMappedKeys() {
    return new HashSet<>(this.arbitraryDataMap.keySet());
  }

  protected void putDataUnsafe(final Class<?> dataClass, final Object dataInstance) {
    this.arbitraryDataMap.put(dataClass, dataInstance);
  }

  @Override
  public String getIndexedField() {
    return "primaryKey";
  }

  @Override
  public String getFieldValue() {
    return this.primaryKey;
  }
}
