package com.gestankbratwurst.core.mmcore.data.mongodb;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UpdateOptions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 07.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class MongoJsonIO extends MongoIO {

  private final String collectionName;

  public MongoJsonIO(final String collectionName) {
    super();
    this.collectionName = collectionName;
  }

  public void persistJson(final String keyName, final String key, final String json) {
    final Document document = Document.parse(json);

    final MongoCollection<Document> collection = this.database.getCollection(this.collectionName);

    collection.createIndex(Indexes.hashed(keyName));

    final UpdateOptions options = new UpdateOptions().upsert(true);

    final Bson filter = Filters.eq(keyName, key);

    collection.updateOne(filter, document, options);
  }

  public String loadJson(final String keyName, final String key) {
    final MongoCollection<Document> collection = this.database.getCollection(this.collectionName);
    final Bson filter = Filters.eq(keyName, key);
    final Document document = collection.find(filter).first();
    if (document == null) {
      return null;
    }
    return document.toJson();
  }

  public Iterable<String> loadKeys(final String keyName) {
    final MongoCollection<Document> collection = this.database.getCollection(this.collectionName);
    final FindIterable<Document> documents = collection.find().projection(Projections.fields(
        Projections.excludeId(),
        Projections.include(keyName)
    ));
    final List<String> keys = new ArrayList<>();
    documents.forEach(document -> keys.add(document.getString(keyName)));
    return keys;
  }

  public void removeKeys(final String keyName, final Collection<String> keys) {
    final MongoCollection<Document> collection = this.database.getCollection(this.collectionName);
    for (final String key : keys) {
      collection.deleteOne(Filters.eq(keyName, key));
    }
  }

}
