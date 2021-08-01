package com.gestankbratwurst.core.mmcore.skinclient;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gestankbratwurst.core.mmcore.skinclient.data.Skin;
import com.gestankbratwurst.core.mmcore.skinclient.data.SkinCallback;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class MineskinClient {

  private static final String ID_FORMAT = "https://api.mineskin.org/get/id/%s";
  private static final String URL_FORMAT = "https://api.mineskin.org/generate/url?url=%s&%s";
  private static final String UPLOAD_FORMAT = "https://api.mineskin.org/generate/upload?%s";
  private static final String USER_FORMAT = "https://api.mineskin.org/generate/user/%s?%s";

  private final Executor requestExecutor;
  private final String userAgent;

  private final JsonParser jsonParser = new JsonParser();
  private final Gson gson = new Gson();

  private long nextRequest = 0;

  public MineskinClient() {
    this.requestExecutor = Executors.newSingleThreadExecutor();
    this.userAgent = "MineSkin-JavaClient";
  }

  public MineskinClient(final Executor requestExecutor) {
    this.requestExecutor = checkNotNull(requestExecutor);
    this.userAgent = "MineSkin-JavaClient";
  }

  public MineskinClient(final Executor requestExecutor, final String userAgent) {
    this.requestExecutor = checkNotNull(requestExecutor);
    this.userAgent = checkNotNull(userAgent);
  }

  public long getNextRequest() {
    return this.nextRequest;
  }

  /*
   * ID
   */

  /**
   * Gets data for an existing Skin
   *
   * @param id       Skin-Id
   * @param callback {@link SkinCallback}
   */
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
        final String body = connection.execute().body();
        this.handleResponse(body, callback);
      } catch (final Exception e) {
        callback.exception(e);
      } catch (final Throwable throwable) {
        throw new RuntimeException(throwable);
      }
    });
  }

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
