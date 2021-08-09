package com.gestankbratwurst.core.mmcore.skinclient.mineskin.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Skin {

  public int id;
  public String uuid;
  public String name;
  public SkinData data;
  public long timestamp;
  @JsonProperty("private")
  public boolean prvate;
  public int views;
  public int accountId;
  public long downloadTimestamp;

  public double nextRequest;

}
