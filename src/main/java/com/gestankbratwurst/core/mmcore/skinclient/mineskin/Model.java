package com.gestankbratwurst.core.mmcore.skinclient.mineskin;

@Deprecated
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

  public Variant toVariant() {
    return switch (this) {
      case DEFAULT -> Variant.CLASSIC;
      case SLIM -> Variant.SLIM;
    };
  }

}
