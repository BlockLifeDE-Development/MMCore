package com.gestankbratwurst.core.mmcore.data.model;

import java.io.Serializable;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 06.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public interface IndexableObject<T> extends Serializable {

  String getIndexedField();

  T getFieldValue();

}