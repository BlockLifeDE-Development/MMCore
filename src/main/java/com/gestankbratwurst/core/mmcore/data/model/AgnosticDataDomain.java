package com.gestankbratwurst.core.mmcore.data.model;

import com.gestankbratwurst.core.mmcore.data.cacheloader.MongoBackedMapLoader;
import com.gestankbratwurst.core.mmcore.data.config.MMCoreConfiguration;
import com.gestankbratwurst.core.mmcore.data.mongodb.MongoIO;
import com.gestankbratwurst.core.mmcore.util.tasks.TaskManager;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.redisson.api.MapOptions;
import org.redisson.api.MapOptions.WriteMode;
import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 05.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class AgnosticDataDomain<T> {

  private static final long R_LOCK_MAX_TIMEOUT_SECONDS = 10;
  private final RMapCache<T, AgnosticDataHolder> agnosticDataHolderMap;
  private final long cacheExpireTimeMinutes;
  private final BinarySwap<T> binarySwap;
  private final Executor dataExecutor = Executors.newSingleThreadExecutor();

  public AgnosticDataDomain(
      final String domainKey,
      final long expireMinutes,
      final RedissonClient redissonClient,
      final MongoIO mongoIO,
      final BinarySwap<T> binarySwap
  ) {
    this.binarySwap = binarySwap;
    final MongoBackedMapLoader<T, AgnosticDataHolder> mapLoader = new MongoBackedMapLoader<>(
        AgnosticDataHolder.class,
        mongoIO,
        "primaryKey",
        domainKey,
        binarySwap.getStringToKey()
    );

    final MapOptions<T, AgnosticDataHolder> options = MapOptions.<T, AgnosticDataHolder>defaults()
        .loader(mapLoader)
        .writeBehindDelay(MMCoreConfiguration.get().getCacheWriteBehindDelayMillis())
        .writeBehindBatchSize(MMCoreConfiguration.get().getCacheWriteBehindBatchSize())
        .writer(mapLoader)
        .writeMode(WriteMode.WRITE_BEHIND);

    this.cacheExpireTimeMinutes = expireMinutes;
    this.agnosticDataHolderMap = redissonClient.getMapCache(domainKey, options);
  }

  public void expire() {
    this.agnosticDataHolderMap.clear();
  }

  /**
   * Mainly used for sync read only functionality. Gets the AgnosticDataHolder associated with this key. This will query the redis cache
   * sync on the thread of the caller of this method. Changes to this Object are not reflected in the cache and should not be written back.
   *
   * @param key the key
   * @return the AgnosticDataHolder
   */
  public AgnosticDataHolder getDataHolderSync(final T key) {
    return this.agnosticDataHolderMap.get(key);
  }

  /**
   * Mainly used for async read only functionality. Gets a CompletableFuture of the AgnosticDataHolder associated with this key. This will
   * query the redis cache async. Changes to this Object are not reflected in the cache and should not be written back.
   *
   * @param key the key
   * @return A CompletableFuture of the AgnosticDataHolder
   */
  public CompletableFuture<AgnosticDataHolder> getDataHolder(final T key) {
    return CompletableFuture.supplyAsync(() -> this.agnosticDataHolderMap.get(key));
  }

  /**
   * Creates an AgnosticDataHolder with a given key. Will overwrite the value that was in here before. Only use if no data exists. This
   * operation locks the cached Object and is strongly consistent.
   *
   * @param key the key
   * @return If the insertion was successful on the redis cache.
   * @see org.redisson.api.RMap#fastPut(Object, Object)
   */
  public CompletableFuture<Result> createDataHolder(final T key) {
    return this.putDataHolder(key, new AgnosticDataHolder(this.binarySwap.getKeyToString().apply(key)));
  }

  /**
   * Creates an AgnosticDataHolder with a given key. Will overwrite the value that was in here before. Only use if no data exists. This
   * operation locks the cached Object and is strongly consistent.
   *
   * @param key the key
   * @return If the insertion was successful on the redis cache.
   * @see org.redisson.api.RMap#fastPut(Object, Object)
   */
  public CompletableFuture<AgnosticDataHolder> getOrCreateDataHolder(final T key) {
    return CompletableFuture.supplyAsync(() -> {
      AgnosticDataHolder dataHolder = null;
      try {
        dataHolder = this.getDataHolder(key).get();
        if (dataHolder == null) {
          this.createDataHolder(key).get();
          return this.getDataHolder(key).get();
        }
      } catch (final InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
      return dataHolder;
    });
  }

  /**
   * Inserts an AgnosticDataHolder with a given key. Will overwrite the value that was in here before. Only use if no data exists. This
   * operation locks the cached Object and is strongly consistent.
   *
   * @param key        the key
   * @param dataHolder the new AgnosticDataHolder
   * @return If the insertion was successful on the redis cache.
   */
  public CompletableFuture<Result> putDataHolder(final T key, final AgnosticDataHolder dataHolder) {
    final RLock lock = this.agnosticDataHolderMap.getLock(key);
    return CompletableFuture.supplyAsync(() -> {
      lock.lock();
      try {
        this.agnosticDataHolderMap.fastPut(key, dataHolder, this.cacheExpireTimeMinutes, TimeUnit.MINUTES);
      } catch (final Exception e) {
        e.printStackTrace();
        return Result.FAILURE;
      } finally {
        lock.unlock();
      }
      return Result.SUCCESS;
    }, this.dataExecutor);
  }

  /**
   * Asynchronously applies actions to an AgnosticDataHolder, by first reading the Object from cache, then applying the actions and lastly
   * writing the Object back into the cache. This operation locks the cached Object and is strongly consistent. If the applied Function
   * returns null then the changes will not be written back to the cache.
   *
   * @param key    the key
   * @param action Which functionality should be applied after loading from cache.
   * @return A CompletableFuture of Result stating if the operation was successful.
   */
  public CompletableFuture<Result> applyToDataHolder(final T key, final Function<AgnosticDataHolder, AgnosticDataHolder> action) {
    final RLock lock = this.agnosticDataHolderMap.getLock(key);
    return CompletableFuture.supplyAsync(() -> {
      lock.lock();
      try {
        AgnosticDataHolder holder = this.agnosticDataHolderMap.get(key);
        if (holder == null) {
          holder = new AgnosticDataHolder(this.binarySwap.getKeyToString().apply(key));
        }
        final AgnosticDataHolder postApplication = action.apply(holder);
        if (postApplication != null) {
          this.agnosticDataHolderMap.fastPut(key, postApplication, this.cacheExpireTimeMinutes, TimeUnit.MINUTES);
        }
      } catch (final Exception e) {
        e.printStackTrace();
        return Result.FAILURE;
      } finally {
        lock.unlock();
      }
      return Result.SUCCESS;
    }, this.dataExecutor);
  }

  /**
   * Applies actions to an AgnosticDataHolder on the Minecraft main Thread, by first reading the Object from cache, then applying the
   * actions sync and lastly writing the Object back into the cache. This operation locks the cached Object and is strongly consistent. If
   * the applied Function returns null then the changes will not be written back to the cache.
   *
   * @param key    the key
   * @param action Which functionality should be applied after loading from cache.
   * @return A CompletableFuture of Result stating if the operation was successful.
   */
  public CompletableFuture<Result> applyToDataHolderSync(final T key, final Function<AgnosticDataHolder, AgnosticDataHolder> action) {
    final RLock lock = this.agnosticDataHolderMap.getLock(key);
    return CompletableFuture.supplyAsync(() -> {
      lock.lock();
      try {
        AgnosticDataHolder holder = this.agnosticDataHolderMap.get(key);
        if (holder == null) {
          holder = new AgnosticDataHolder(this.binarySwap.getKeyToString().apply(key));
        }
        final AgnosticDataHolder finalHolder = holder;
        final AgnosticDataHolder postApplication = TaskManager.getInstance().callSyncMethod(() -> action.apply(finalHolder)).get();
        if (postApplication != null) {
          this.agnosticDataHolderMap.fastPut(key, postApplication, this.cacheExpireTimeMinutes, TimeUnit.MINUTES);
        }
      } catch (final Exception e) {
        e.printStackTrace();
        return Result.FAILURE;
      } finally {
        lock.unlock();
      }
      return Result.SUCCESS;
    }, this.dataExecutor);
  }

  public CompletableFuture<Result> coupleAction(final T aKey, final T bKey,
      final BiConsumer<AgnosticDataHolder, AgnosticDataHolder> action) {
    final RLock aLock = this.agnosticDataHolderMap.getLock(aKey);
    final RLock bLock = this.agnosticDataHolderMap.getLock(bKey);
    return CompletableFuture.supplyAsync(() -> {
      aLock.lock();
      bLock.lock();
      try {
        AgnosticDataHolder aHolder = this.agnosticDataHolderMap.get(aKey);
        AgnosticDataHolder bHolder = this.agnosticDataHolderMap.get(bKey);
        if (aHolder == null) {
          aHolder = new AgnosticDataHolder(this.binarySwap.getKeyToString().apply(aKey));
        }
        if (bHolder == null) {
          bHolder = new AgnosticDataHolder(this.binarySwap.getKeyToString().apply(bKey));
        }
        action.accept(aHolder, bHolder);
        this.agnosticDataHolderMap.fastPut(aKey, aHolder, this.cacheExpireTimeMinutes, TimeUnit.MINUTES);
        this.agnosticDataHolderMap.fastPut(bKey, bHolder, this.cacheExpireTimeMinutes, TimeUnit.MINUTES);
      } catch (final Exception e) {
        e.printStackTrace();
        return Result.FAILURE;
      } finally {
        aLock.unlock();
        bLock.unlock();
      }
      return Result.SUCCESS;
    }, this.dataExecutor);
  }

  public CompletableFuture<Result> coupleActionSync(final T aKey, final T bKey,
      final BiConsumer<AgnosticDataHolder, AgnosticDataHolder> action) {
    return this.coupleAction(aKey, bKey, (x, y) -> {
      try {
        TaskManager.getInstance().callSyncMethod(() -> action.accept(x, y)).get();
      } catch (final InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    });
  }

  public enum Result {
    SUCCESS,
    FAILURE
  }

}