package com.gestankbratwurst.core.mmcore.data.model;

import java.util.function.Function;
import lombok.Builder;
import lombok.Data;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 07.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@Data
@Builder
public class BinarySwap<T> {

  private final Function<T, String> keyToString;
  private final Function<String, T> stringToKey;

}
