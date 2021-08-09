package com.gestankbratwurst.core.mmcore.skinclient.mineskin;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gestankbratwurst.core.mmcore.skinclient.mineskin.data.MineskinException;
import com.gestankbratwurst.core.mmcore.skinclient.mineskin.data.Skin;
import com.gestankbratwurst.core.mmcore.skinclient.mineskin.data.SkinCallback;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class MineskinClient {

  private static final String API_BASE = "https://api.mineskin.org";
  private static final String GENERATE_BASE = API_BASE + "/generate";
  private static final String GET_BASE = API_BASE + "/get";

  private static final String ID_FORMAT = "https://api.mineskin.org/get/id/%s";
  private static final String URL_FORMAT = "https://api.mineskin.org/generate/url?url=%s&%s";
  private static final String UPLOAD_FORMAT = "https://api.mineskin.org/generate/upload?%s";
  private static final String USER_FORMAT = "https://api.mineskin.org/generate/user/%s?%s";

  private final Executor requestExecutor;
  private final String userAgent;
  private final String apiKey;

  private final JsonParser jsonParser = new JsonParser();
  private final Gson gson = new Gson();

  private long nextRequest = 0;

  @Deprecated
  public MineskinClient() {
    this.requestExecutor = Executors.newSingleThreadExecutor();
    this.userAgent = "MineSkin-JavaClient";
    this.apiKey = null;
  }

  @Deprecated
  public MineskinClient(final Executor requestExecutor) {
    this.requestExecutor = checkNotNull(requestExecutor);
    this.userAgent = "MineSkin-JavaClient";
    this.apiKey = null;
  }

  public MineskinClient(final String userAgent) {
    this.requestExecutor = Executors.newSingleThreadExecutor();
    this.userAgent = checkNotNull(userAgent);
    this.apiKey = null;
  }

  public MineskinClient(final String userAgent, final String apiKey) {
    this.requestExecutor = Executors.newSingleThreadExecutor();
    this.userAgent = checkNotNull(userAgent);
    this.apiKey = apiKey;
  }

  public MineskinClient(final Executor requestExecutor, final String userAgent, final String apiKey) {
    this.requestExecutor = checkNotNull(requestExecutor);
    this.userAgent = checkNotNull(userAgent);
    this.apiKey = apiKey;
  }

  public MineskinClient(final Executor requestExecutor, final String userAgent) {
    this.requestExecutor = checkNotNull(requestExecutor);
    this.userAgent = checkNotNull(userAgent);
    this.apiKey = null;
  }

  public long getNextRequest() {
    return this.nextRequest;
  }

  /////

  private Connection generateRequest(final String endpoint) {
    final Connection connection = Jsoup.connect(GENERATE_BASE + endpoint)
        .method(Connection.Method.POST)
        .userAgent(this.userAgent)
        .ignoreContentType(true)
        .ignoreHttpErrors(true)
        .timeout(30000);
    if (this.apiKey != null) {
      connection.header("Authorization", "Bearer " + this.apiKey);
    }
    return connection;
  }

  private Connection getRequest(final String endpoint) {
    return Jsoup.connect(GET_BASE + endpoint)
        .method(Connection.Method.GET)
        .userAgent(this.userAgent)
        .ignoreContentType(true)
        .ignoreHttpErrors(true)
        .timeout(5000);
  }


  public CompletableFuture<Skin> getId(final long id) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        final Connection connection = this.getRequest("/id/" + id);
        return this.handleResponse(connection.execute().body());
      } catch (final Exception e) {
        throw new RuntimeException(e);
      }
    }, this.requestExecutor);
  }

  public CompletableFuture<Skin> getUuid(final UUID uuid) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        final Connection connection = this.getRequest("/uuid/" + uuid);
        return this.handleResponse(connection.execute().body());
      } catch (final Exception e) {
        throw new RuntimeException(e);
      }
    }, this.requestExecutor);
  }

  public CompletableFuture<Skin> generateUrl(final String url) {
    return generateUrl(url, SkinOptions.none());
  }

  /**
   * Generates skin data from an URL
   */
  public CompletableFuture<Skin> generateUrl(final String url, final SkinOptions options) {
    checkNotNull(url);
    checkNotNull(options);
    return CompletableFuture.supplyAsync(() -> {
      try {
        if (System.currentTimeMillis() < this.nextRequest) {
          final long delay = (this.nextRequest - System.currentTimeMillis());
          Thread.sleep(delay + 1000);
        }

        final JsonObject body = options.toJson();
        body.addProperty("url", url);
        final Connection connection = this.generateRequest("/url")
            .header("Content-Type", "application/json")
            .requestBody(body.toString());
        return this.handleResponse(connection.execute().body());
      } catch (final Exception e) {
        throw new RuntimeException(e);
      }
    }, this.requestExecutor);
  }

  public CompletableFuture<Skin> generateUpload(final File file) {
    return generateUpload(file, SkinOptions.none());
  }

  /**
   * Uploads and generates skin data from a local file (with default options)
   */
  public CompletableFuture<Skin> generateUpload(final File file, final SkinOptions options) {
    checkNotNull(file);
    checkNotNull(options);
    return CompletableFuture.supplyAsync(() -> {
      try {
        if (System.currentTimeMillis() < this.nextRequest) {
          final long delay = (this.nextRequest - System.currentTimeMillis());
          Thread.sleep(delay + 1000);
        }

        final Connection connection = this.generateRequest("/upload")
            // It really doesn't like setting a content-type header here for some reason
            .data("file", file.getName(), new FileInputStream(file));
        options.addAsData(connection);
        return this.handleResponse(connection.execute().body());
      } catch (final Exception e) {
        throw new RuntimeException(e);
      }
    }, this.requestExecutor);
  }

  public CompletableFuture<Skin> generateUser(final UUID uuid) {
    return generateUser(uuid, SkinOptions.none());
  }

  /**
   * Loads skin data from an existing player
   */
  public CompletableFuture<Skin> generateUser(final UUID uuid, final SkinOptions options) {
    checkNotNull(uuid);
    checkNotNull(options);
    return CompletableFuture.supplyAsync(() -> {
      try {
        if (System.currentTimeMillis() < this.nextRequest) {
          final long delay = (this.nextRequest - System.currentTimeMillis());
          Thread.sleep(delay + 1000);
        }

        final JsonObject body = options.toJson();
        body.addProperty("user", uuid.toString());
        final Connection connection = this.generateRequest("/user")
            .header("Content-Type", "application/json")
            .requestBody(body.toString());
        return this.handleResponse(connection.execute().body());
      } catch (final Exception e) {
        throw new RuntimeException(e);
      }
    }, this.requestExecutor);
  }

  Skin handleResponse(final String body) throws MineskinException, JsonParseException {
    final JsonObject jsonObject = this.gson.fromJson(body, JsonObject.class);
    if (jsonObject.has("error")) {
      throw new MineskinException(jsonObject.get("error").getAsString());
    }

    final Skin skin = this.gson.fromJson(jsonObject, Skin.class);
    this.nextRequest = System.currentTimeMillis() + ((long) ((skin.nextRequest + 10) * 1000L));
    return skin;
  }

  ///// SkinCallback stuff below


  /*
   * ID
   */

  /**
   * Gets data for an existing Skin
   *
   * @param id       Skin-Id
   * @param callback {@link SkinCallback}
   */
  @Deprecated
  public void getSkin(final int id, final SkinCallback callback) {
    checkNotNull(callback);
    this.requestExecutor.execute(() -> {
      try {
        final Connection connection = Jsoup
            .connect(String.format(ID_FORMAT, id))
            .userAgent(this.userAgent)
            .method(Connection.Method.GET)
            .ignoreContentType(true)
            .ignoreHttpErrors(true)
            .timeout(10000);
        final String body = connection.execute().body();
        this.handleResponse(body, callback);
      } catch (final Exception e) {
        callback.exception(e);
      } catch (final Throwable throwable) {
        throw new RuntimeException(throwable);
      }
    });
  }

  /*
   * URL
   */

  /**
   * Generates skin data from an URL (with default options)
   *
   * @param url      URL
   * @param callback {@link SkinCallback}
   * @see #generateUrl(String, SkinOptions, SkinCallback)
   */
  @Deprecated
  public void generateUrl(final String url, final SkinCallback callback) {
    this.generateUrl(url, SkinOptions.none(), callback);
  }

  /**
   * Generates skin data from an URL
   *
   * @param url      URL
   * @param options  {@link SkinOptions}
   * @param callback {@link SkinCallback}
   */
  @Deprecated
  public void generateUrl(final String url, final SkinOptions options, final SkinCallback callback) {
    checkNotNull(url);
    checkNotNull(options);
    checkNotNull(callback);
    this.requestExecutor.execute(() -> {
      try {
        if (System.currentTimeMillis() < this.nextRequest) {
          final long delay = (this.nextRequest - System.currentTimeMillis());
          callback.waiting(delay);
          Thread.sleep(delay + 1000);
        }

        callback.uploading();

        final Connection connection = Jsoup
            .connect(String.format(URL_FORMAT, url, options.toUrlParam()))
            .userAgent(this.userAgent)
            .method(Connection.Method.POST)
            .ignoreContentType(true)
            .ignoreHttpErrors(true)
            .timeout(40000);
        if (this.apiKey != null) {
          connection.header("Authorization", "Bearer " + this.apiKey);
        }
        final String body = connection.execute().body();
        this.handleResponse(body, callback);
      } catch (final Exception e) {
        callback.exception(e);
      } catch (final Throwable throwable) {
        throw new RuntimeException(throwable);
      }
    });
  }

  /*
   * Upload
   */

  /**
   * Uploads and generates skin data from a local file (with default options)
   *
   * @param file     File to upload
   * @param callback {@link SkinCallback}
   */
  @Deprecated
  public void generateUpload(final File file, final SkinCallback callback) {
    this.generateUpload(file, SkinOptions.none(), callback);
  }

  /**
   * Uploads and generates skin data from a local file
   *
   * @param file     File to upload
   * @param options  {@link SkinOptions}
   * @param callback {@link SkinCallback}
   */
  @Deprecated
  public void generateUpload(final File file, final SkinOptions options, final SkinCallback callback) {
    checkNotNull(file);
    checkNotNull(options);
    checkNotNull(callback);
    this.requestExecutor.execute(() -> {
      try {
        if (System.currentTimeMillis() < this.nextRequest) {
          final long delay = (this.nextRequest - System.currentTimeMillis());
          callback.waiting(delay);
          Thread.sleep(delay + 1000);
        }

        callback.uploading();

        final Connection connection = Jsoup
            .connect(String.format(UPLOAD_FORMAT, options.toUrlParam()))
            .userAgent(this.userAgent)
            .method(Connection.Method.POST)
            .data("file", file.getName(), new FileInputStream(file))
            .ignoreContentType(true)
            .ignoreHttpErrors(true)
            .timeout(40000);
        if (this.apiKey != null) {
          connection.header("Authorization", "Bearer " + this.apiKey);
        }
        final String body = connection.execute().body();
        this.handleResponse(body, callback);
      } catch (final Exception e) {
        callback.exception(e);
      } catch (final Throwable throwable) {
        throw new RuntimeException(throwable);
      }
    });
  }

  /*
   * User
   */

  /**
   * Loads skin data from an existing player (with default options)
   *
   * @param uuid     {@link UUID} of the player
   * @param callback {@link SkinCallback}
   */
  @Deprecated
  public void generateUser(final UUID uuid, final SkinCallback callback) {
    this.generateUser(uuid, SkinOptions.none(), callback);
  }

  /**
   * Loads skin data from an existing player
   *
   * @param uuid     {@link UUID} of the player
   * @param options  {@link SkinOptions}
   * @param callback {@link SkinCallback}
   */
  @Deprecated
  public void generateUser(final UUID uuid, final SkinOptions options, final SkinCallback callback) {
    checkNotNull(uuid);
    checkNotNull(options);
    checkNotNull(callback);
    this.requestExecutor.execute(() -> {
      try {
        if (System.currentTimeMillis() < this.nextRequest) {
          final long delay = (this.nextRequest - System.currentTimeMillis());
          callback.waiting(delay);
          Thread.sleep(delay + 1000);
        }

        callback.uploading();

        final Connection connection = Jsoup
            .connect(String.format(USER_FORMAT, uuid.toString(), options.toUrlParam()))
            .userAgent(this.userAgent)
            .method(Connection.Method.GET)
            .ignoreContentType(true)
            .ignoreHttpErrors(true)
            .timeout(40000);
        if (this.apiKey != null) {
          connection.header("Authorization", "Bearer " + this.apiKey);
        }
        final String body = connection.execute().body();
        this.handleResponse(body, callback);
      } catch (final Exception e) {
        callback.exception(e);
      } catch (final Throwable throwable) {
        throw new RuntimeException(throwable);
      }
    });
  }

  @Deprecated
  void handleResponse(final String body, final SkinCallback callback) {
    try {
      final JsonObject jsonObject = this.jsonParser.parse(body).getAsJsonObject();
      if (jsonObject.has("error")) {
        callback.error(jsonObject.get("error").getAsString());
        return;
      }

      final Skin skin = this.gson.fromJson(jsonObject, Skin.class);
      this.nextRequest = System.currentTimeMillis() + ((long) ((skin.nextRequest + 10) * 1000L));
      callback.done(skin);
    } catch (final JsonParseException e) {
      callback.parseException(e, body);
    } catch (final Throwable throwable) {
      throw new RuntimeException(throwable);
    }
  }

}
