package com.gestankbratwurst.core.mmcore.skinclient.mineskin;

public enum Visibility {

  PUBLIC(0),
  PRIVATE(1);

  private final int code;

  Visibility(final int code) {
    this.code = code;
  }

  public int getCode() {
    return this.code;
  }
}
