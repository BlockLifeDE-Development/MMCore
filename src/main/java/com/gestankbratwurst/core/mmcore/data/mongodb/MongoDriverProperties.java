package com.gestankbratwurst.core.mmcore.data.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.ServerAddress;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MongoDriverProperties {

  private String user;
  private String database;
  private String password;
  private String hostAddress;
  private int hostPort;

  @JsonIgnore
  public List<ServerAddress> getSingletonHostList() {
    return Collections.singletonList(new ServerAddress(this.hostAddress, this.hostPort));
  }

}
