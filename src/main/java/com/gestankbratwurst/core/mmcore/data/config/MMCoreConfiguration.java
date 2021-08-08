package com.gestankbratwurst.core.mmcore.data.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gestankbratwurst.core.mmcore.data.access.ServerType;
import com.gestankbratwurst.core.mmcore.data.mongodb.MongoDriverProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 06.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@Getter
@NoArgsConstructor
public class MMCoreConfiguration {

  public static MMCoreConfiguration get() {
    return MMCoreConfigManager.getConfiguration();
  }

  @JsonProperty("ServerType")
  private final ServerType serverType = ServerType.MASTER;
  @JsonProperty("RedisAddress")
  private final String redisAddress = "redis://127.0.0.1:6379";
  @JsonProperty("MongoDriverProperties")
  private final MongoDriverProperties mongoDriverProperties = MongoDriverProperties.builder()
      .database("BlockLife")
      .user("admin")
      .password("pw123")
      .hostAddress("localhost")
      .hostPort(27017)
      .build();
  @JsonProperty("ArbitraryDataDefaultCacheMinutes")
  private final long arbitraryDataDefaultCacheMinutes = 30;
  @JsonProperty("PlayerDataCacheMinutes")
  private final long playerDataCacheMinutes = 60;
  @JsonProperty("CacheWriteBehindDelayMillis")
  private final int cacheWriteBehindDelayMillis = 5000;
  @JsonProperty("CacheWriteBehindBatchSize")
  private final int cacheWriteBehindBatchSize = 50;

}