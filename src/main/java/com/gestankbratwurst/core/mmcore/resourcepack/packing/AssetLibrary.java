package com.gestankbratwurst.core.mmcore.resourcepack.packing;

import com.gestankbratwurst.core.mmcore.resourcepack.skins.TextureModel;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.plugin.java.JavaPlugin;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of avarioncore and was created at the 24.11.2019
 *
 * avarioncore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class AssetLibrary {

  private static final String VERSION = "1.16.5";
  private static final String ASSET_URL = "https://assets.mcasset.cloud/" + AssetLibrary.VERSION + "/assets/minecraft/models/";


  public AssetLibrary(final JavaPlugin plugin) {
    this.itemModelDefaultAssets = Maps.newHashMap();
    this.assetCacheFile = new File(plugin.getDataFolder(), "assetCache.json");
    this.loadCache();
    this.iterateAssets();
  }

  private final File assetCacheFile;
  private final Map<String, JsonObject> itemModelDefaultAssets;

  private void loadCache() {
    if (!this.assetCacheFile.exists()) {
      return;
    }
    final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    final JsonObject json;
    try {
      json = gson.fromJson(Files.readString(this.assetCacheFile.toPath()), JsonObject.class);
    } catch (final IOException e) {
      e.printStackTrace();
      return;
    }

    final JsonElement versionObject = json.get(AssetLibrary.VERSION);

    if (versionObject == null) {
      return;
    }

    for (final Entry<String, JsonElement> entry : versionObject.getAsJsonObject().entrySet()) {
      this.itemModelDefaultAssets.put(entry.getKey(), entry.getValue().getAsJsonObject());
    }
  }

  private void iterateAssets() {

    for (final TextureModel textureModel : TextureModel.values()) {
      final String modelFolder = textureModel.getBaseMaterial().isBlock() ? "block" : "item";
      final String nmsName = textureModel.getBaseMaterial().getKey().getKey();
      if (!this.itemModelDefaultAssets.containsKey(nmsName)) {
        final String jsonUrl = AssetLibrary.ASSET_URL + modelFolder + "/" + nmsName + ".json";
        final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        final String request = this.jsonGetRequest(jsonUrl);
        if (request == null) {
          return;
        }
        final JsonObject json = gson.fromJson(request, JsonObject.class);
        this.itemModelDefaultAssets.put(nmsName, json);
      }
    }

  }

  private String jsonGetRequest(final String urlQueryString) {
    String json = null;
    try {
      final URL url = new URL(urlQueryString);
      final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setInstanceFollowRedirects(false);
      connection.setRequestMethod("GET");
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("charset", "utf-8");
      connection.connect();
      final InputStream inStream = connection.getInputStream();
      final InputStreamReader isr = new InputStreamReader(inStream, StandardCharsets.UTF_8);
      int read;
      final StringBuilder builder = new StringBuilder();
      while ((read = isr.read()) != -1) {
        builder.append((char) read);
      }
      json = builder.toString();
    } catch (final IOException ex) {
      ex.printStackTrace();
    }
    return json;
  }

  protected String getAssetModelParent(final String nmsKey) {
    return this.itemModelDefaultAssets.get(nmsKey).get("parent").getAsString();
  }

  protected String getAssetModelLayer0(final String nmsKey) {
    return this.itemModelDefaultAssets.get(nmsKey)
        .get("textures")
        .getAsJsonObject()
        .get("layer0").getAsString();
  }

  public void saveCache() {

    if (this.itemModelDefaultAssets.isEmpty()) {
      return;
    }

    final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    final JsonObject json = new JsonObject();

    json.add(AssetLibrary.VERSION, gson.toJsonTree(this.itemModelDefaultAssets));

    try {
      Files.writeString(this.assetCacheFile.toPath(), gson.toJson(json));
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

}
