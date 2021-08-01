package com.gestankbratwurst.core.mmcore.scoreboard.api;

public class Entry {

  private String name;
  private int position;

  public Entry(final String name, final int position) {
    this.position = position;
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public int getPosition() {
    return this.position;
  }

  public void setPosition(final int position) {
    this.position = position;
  }

}
