package com.gestankbratwurst.core.mmcore.data.access;

import com.google.common.base.Preconditions;
import org.redisson.api.RedissonClient;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 06.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class DataAccess {

  private static final String MASTER_REGISTERED_KEY = "is_master_registered";
  private static boolean initialized = false;

  public static void init(final RedissonClient redissonClient, final ServerType serverType) {
    Preconditions.checkState(!initialized, "You can only initialize DataAceess once.");
    initialized = true;
    registerAsMaster(redissonClient);
  }

  private static void registerAsMaster(final RedissonClient redissonClient) {
    
  }

}