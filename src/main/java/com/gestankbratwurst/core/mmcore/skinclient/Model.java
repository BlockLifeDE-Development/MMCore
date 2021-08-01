package com.gestankbratwurst.core.mmcore.skinclient;

public enum Model {

  DEFAULT("steve"),
  SLIM("slim");

  private final String name;

  Model(final String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
