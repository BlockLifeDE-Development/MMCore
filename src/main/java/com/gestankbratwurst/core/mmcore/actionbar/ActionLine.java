package com.gestankbratwurst.core.mmcore.actionbar;

import com.google.common.base.Strings;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.Setter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 28.07.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ActionLine implements Comparable<ActionLine> {

  public static final int MIN_PRIORITY = 1000000;
  public static final int LOW_PRIORITY = 100000;
  public static final int MID_PRIORITY = 10000;
  public static final int HIGH_PRIORITY = 1000;
  public static final int VERY_HIGH_PRIORITY = 100;
  public static final int MAX_PRIORITY = 10;

  public static ActionLine empty() {
    return new ActionLine(MIN_PRIORITY, () -> Strings.repeat(" ", ActionBarBoard.MIN_SECTION_LENGTH));
  }

  public ActionLine(final int priority, final String simpleLine) {
    this(priority, () -> simpleLine);
  }

  public ActionLine(final int priority, final Supplier<String> lineSupplier) {
    this.priority = priority;
    this.lineSupplier = lineSupplier;
  }

  @Getter
  @Setter
  private int priority;
  @Getter
  @Setter
  private Supplier<String> lineSupplier;

  @Override
  public int compareTo(final ActionLine other) {
    return this.priority - other.priority;
  }

}