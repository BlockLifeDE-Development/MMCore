package com.gestankbratwurst.core.mmcore.skinclient.mineskin;

public enum Variant {
  AUTO(""),
  CLASSIC("classic"),
  SLIM("slim");

  private final String name;

  Variant(final String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
