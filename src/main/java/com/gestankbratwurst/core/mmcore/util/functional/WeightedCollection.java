package com.gestankbratwurst.core.mmcore.util.functional;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of avarioncore and was created at the 26.04.2020
 *
 * avarioncore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class WeightedCollection<E> {

  private final NavigableMap<Double, E> binarySearchableMap = new TreeMap<>();
  private final Random random;
  private double weightSum = 0;

  public WeightedCollection() {
    this(new Random());
  }

  public WeightedCollection(final Random random) {
    this.random = random;
  }

  public WeightedCollection<E> add(final double weight, final E element) {
    if (weight <= 0) {
      return this;
    }
    this.weightSum += weight;
    this.binarySearchableMap.put(this.weightSum, element);
    return this;
  }

  public E poll() {
    return this.binarySearchableMap.higherEntry(this.createRandomScaledWeight()).getValue();
  }

  private double createRandomScaledWeight() {
    return this.random.nextDouble() * this.weightSum;
  }

}
