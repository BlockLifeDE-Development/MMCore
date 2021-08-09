package com.gestankbratwurst.core.mmcore.data.model;

import com.gestankbratwurst.core.mmcore.data.config.MMCoreConfiguration;
import com.gestankbratwurst.core.mmcore.data.mongodb.MongoIO;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
@SuppressWarnings("unchecked")
public class DataManager {

  public static final String PLAYER_DOMAIN_KEY = "PlayerData";
  public static final String GLOBAL_DOMAIN_KEY = "GlobalData";
  public static final BinarySwap<UUID> UUID_SWAP = BinarySwap.<UUID>builder()
      .keyToString(UUID::toString)
      .stringToKey(UUID::fromString)
      .build();
  public static final BinarySwap<String> STRING_SWAP = BinarySwap.<String>builder()
      .keyToString(String::toString)
      .stringToKey(String::valueOf)
      .build();
  private static long PLAYER_CACHE_TIME_MINUTES = 60;
  private static long DEFAULT_CACHE_TIME_MINUTES = 30;

  private final MongoIO mongoIO;
  private final RedissonClient redissonClient;
  private final Map<String, AgnosticDataDomain<?>> agnosticDataDomainMap = new HashMap<>();

  public DataManager(final RedissonClient redissonClient, final MongoIO mongoIO) {
    this.mongoIO = mongoIO;
    this.redissonClient = redissonClient;
    PLAYER_CACHE_TIME_MINUTES = MMCoreConfiguration.get().getPlayerDataCacheMinutes();
    DEFAULT_CACHE_TIME_MINUTES = MMCoreConfiguration.get().getArbitraryDataDefaultCacheMinutes();
  }

  public void expireAllDomains() {
    this.agnosticDataDomainMap.values().forEach(AgnosticDataDomain::expire);
  }

  public AgnosticDataDomain<String> getGlobalDataDomain() {
    return this.getOrCreateDataDomain(GLOBAL_DOMAIN_KEY, DEFAULT_CACHE_TIME_MINUTES, STRING_SWAP);
  }

  public AgnosticDataDomain<UUID> getPlayerDataDomain() {
    return this.getOrCreateDataDomain(PLAYER_DOMAIN_KEY, PLAYER_CACHE_TIME_MINUTES, UUID_SWAP);
  }

  public <T> AgnosticDataDomain<T> getOrCreateDataDomain(final String domainKey, final BinarySwap<T> binarySwap) {
    return (AgnosticDataDomain<T>) this.agnosticDataDomainMap
        .computeIfAbsent(domainKey, key -> this.create(domainKey, DEFAULT_CACHE_TIME_MINUTES, binarySwap));
  }

  public <T> AgnosticDataDomain<T> getOrCreateDataDomain(final String domainKey, final long cacheTimeMinutes,
      final BinarySwap<T> binarySwap) {
    return (AgnosticDataDomain<T>) this.agnosticDataDomainMap
        .computeIfAbsent(domainKey, key -> this.create(domainKey, cacheTimeMinutes, binarySwap));
  }

  private <T> AgnosticDataDomain<T> create(final String domainKey, final long cacheTimeMinutes, final BinarySwap<T> binarySwap) {
    return new AgnosticDataDomain<>(domainKey, cacheTimeMinutes, this.redissonClient, this.mongoIO, binarySwap);
  }

}