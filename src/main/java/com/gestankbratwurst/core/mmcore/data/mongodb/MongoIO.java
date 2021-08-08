package com.gestankbratwurst.core.mmcore.data.mongodb;

import com.gestankbratwurst.core.mmcore.data.config.MMCoreConfiguration;
import com.gestankbratwurst.core.mmcore.data.model.IndexableObject;
import com.gestankbratwurst.core.mmcore.util.json.JacksonProvider;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReplaceOptions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import org.bson.Document;
import org.bson.conversions.Bson;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of AvarionCore and was created at the 03.07.2020
 *
 * AvarionCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class MongoIO {

  protected final MongoDatabase database;

  public MongoIO() {
    final MongoDriverProperties props = MMCoreConfiguration.get().getMongoDriverProperties();
    final MongoCredential credential = MongoCredential
        .createCredential(props.getUser(), props.getDatabase(), props.getPassword().toCharArray());
    final MongoClientSettings settings = MongoClientSettings.builder()
        .credential(credential)
        .build();

    final MongoClient mongoClient = MongoClients.create(settings);
    this.database = mongoClient.getDatabase(props.getDatabase());
  }

  public <T extends IndexableObject<?>> void persist(final String collectionName, final T object) {
    final String json = JacksonProvider.serialize(object);
    final Document document = Document.parse(json);

    final MongoCollection<Document> collection = this.database.getCollection(collectionName);

    collection.createIndex(Indexes.hashed(object.getIndexedField()));

    final ReplaceOptions options = new ReplaceOptions().upsert(true);

    final Bson filter = Filters.eq(object.getIndexedField(), object.getFieldValue());

    collection.replaceOne(filter, document, options);
  }

  public <K, T> T load(final Class<T> tClass, final String collectionName, final String fieldName, final K fieldValue) {
    final MongoCollection<Document> collection = this.database.getCollection(collectionName);
    final Bson filter = Filters.eq(fieldName, fieldValue);
    final Document document = collection.find(filter).first();
    if (document == null) {
      return null;
    }
    return JacksonProvider.deserialize(document.toJson(), tClass);
  }

  public <K> Iterable<K> loadAllKeys(final String collectionName, final String primaryKey, final Function<String, K> keyLoader) {
    final MongoCollection<Document> collection = this.database.getCollection(collectionName);
    final FindIterable<Document> documents = collection.find().projection(Projections.fields(
        Projections.excludeId(),
        Projections.include(primaryKey)
    ));
    final List<K> keys = new ArrayList<>();
    documents.forEach(document -> keys.add(keyLoader.apply(document.getString(primaryKey))));
    return keys;
  }

  public <K> void deleteAll(final String collectionName, final String primaryKeyName, final Collection<K> keys) {
    final MongoCollection<Document> collection = this.database.getCollection(collectionName);
    for (final K key : keys) {
      collection.deleteOne(Filters.eq(primaryKeyName, key));
    }
  }

}