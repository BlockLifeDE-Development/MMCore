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

  @JsonProperty("ServerName")
  private final String serverName = "Instanz_I";
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
  @JsonProperty("ResourcePackServerIP")
  private final String resourcePackServerIP = "127.0.0.1";
  @JsonProperty("ResourcePackServerPort")
  private final int resourcePackServerPort = 9988;
  @JsonProperty("MineSkinClientAPIToken")
  private final String mineSkinClientAPIToken = "4e4d5e9f0d61a084e0673f99f49fd182280fb670151209f46fdc5c2a38867fdb";
  @JsonProperty("MineSkinClientKeySecret")
  private final String mineSkinClientKeySecret = "4ee3343aec34213a2df5616b137c6a36f4a0e89884b9b3b4852019b7faa33c4d912d9cd89c78c4b968d0b78d34b0c35d9025b8f7d7da4a78c2ca131ff5c05528";
  @JsonProperty("MineSkinClientUserAgent")
  private final String mineSkinClientUserAgent = "BlockLifeAgent";

}