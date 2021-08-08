package com.gestankbratwurst.core.mmcore.data.cacheloader;

import com.gestankbratwurst.core.mmcore.data.model.IndexableObject;
import com.gestankbratwurst.core.mmcore.data.mongodb.MongoIO;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import org.redisson.api.map.MapLoader;
import org.redisson.api.map.MapWriter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 07.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public class MongoBackedMapLoader<K, V extends IndexableObject<?>> implements MapLoader<K, V>, MapWriter<K, V> {

  private final Class<V> valueClass;
  private final MongoIO mongoIO;
  private final String primaryKeyName;
  private final String domainName;
  private final Function<String, K> keyLoaderFunction;

  @Override
  public V load(final K key) {
    return this.mongoIO.load(this.valueClass, this.domainName, this.primaryKeyName, key);
  }

  @Override
  public Iterable<K> loadAllKeys() {
    return this.mongoIO.loadAllKeys(this.domainName, this.primaryKeyName, this.keyLoaderFunction);
  }

  @Override
  public void write(final Map<K, V> map) {
    for (final Entry<K, V> entry : map.entrySet()) {
      this.mongoIO.persist(this.domainName, entry.getValue());
    }
  }

  @Override
  public void delete(final Collection<K> keys) {
    this.mongoIO.deleteAll(this.domainName, this.primaryKeyName, keys);
  }

}