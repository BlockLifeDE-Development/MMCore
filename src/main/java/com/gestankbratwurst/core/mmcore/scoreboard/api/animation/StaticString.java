package com.gestankbratwurst.core.mmcore.scoreboard.api.animation;

public class StaticString implements AnimatableString {

  private final String string;

  public StaticString(final String string) {
    this.string = string;
  }

  @Override
  public String current() {
    return this.string;
  }

  @Override
  public String previous() {
    return this.string;
  }

  @Override
  public String next() {
    return this.string;
  }

}
