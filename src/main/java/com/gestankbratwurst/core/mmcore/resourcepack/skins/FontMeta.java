package com.gestankbratwurst.core.mmcore.resourcepack.skins;

import lombok.AllArgsConstructor;
import lombok.Data;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of avarioncore and was created at the 25.11.2019
 *
 * avarioncore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@Data
@AllArgsConstructor
public class FontMeta {

  private final int horizontalShift;
  private final int verticalShift;
  private final int height;
  private final int ascent;
  private final String type;

  public static FontMeta of(final int height, final int ascent, final String type) {
    return new FontMeta(0, 0, height, ascent, type);
  }

  public static FontMeta of(final int height, final int ascent) {
    return new FontMeta(0, 0, height, ascent, "bitmap");
  }

  public static FontMeta common() {
    return new FontMeta(0, 0, 9, 8, "bitmap");
  }

}
