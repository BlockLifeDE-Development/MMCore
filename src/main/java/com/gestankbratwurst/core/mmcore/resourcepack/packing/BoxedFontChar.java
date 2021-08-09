package com.gestankbratwurst.core.mmcore.resourcepack.packing;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of avarioncore and was created at the 25.11.2019
 *
 * avarioncore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class BoxedFontChar {

  protected char value = '⛔';

  public char getAsCharacter() {
    return this.value;
  }

  @Override
  public String toString() {
    return String.valueOf(this.value);
  }

}
