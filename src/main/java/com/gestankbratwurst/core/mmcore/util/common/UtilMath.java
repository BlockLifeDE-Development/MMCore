package com.gestankbratwurst.core.mmcore.util.common;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UtilMath {

  public static double cut(double value, final int decimalPoints) {
    final int decades = (int) Math.pow(10, decimalPoints);
    value = ((double) ((int) (value * decades))) / 10;
    return value;
  }

  public static int round(final double value) {
    return (int) (value + 0.5);
  }

  public static String getIconBar(final double current, final double max, final int size, final char emptyChar, final char halfChar,
      final char fullChar) {
    final int fulls = (int) (size * ((1D / max) * current));
    final int halfs = (int) (size * ((2D / max) * current));
    int empties = size - fulls;
    final boolean appendHalf = fulls * 2 != halfs;

    if (appendHalf) {
      empties--;
    }
    final StringBuilder builder = new StringBuilder();

    if (fulls > 0) {
      builder.append(("" + fullChar).repeat(fulls));
    }
    if (appendHalf) {
      builder.append(halfChar);
    }
    if (empties > 0) {
      builder.append(("" + emptyChar).repeat(empties));
    }

    return builder.toString();
  }

  public static String getPercentageBar(final double current, final double max, final int size, final String segment) {
    final StringBuilder builder = new StringBuilder();
    final int lows = (int) (size * ((1D / max) * current));
    final int highs = size - lows;
    builder.append("§a");
    if (lows > 0) {
      builder.append(segment.repeat(lows));
    }
    builder.append("§c");
    if (highs > 0) {
      builder.append(segment.repeat(highs));
    }
    return builder.toString();
  }

  public static Double parseDouble(final String value) {
    try {
      return Double.parseDouble(value);
    } catch (final NumberFormatException exception) {
      return null;
    }
  }

  public static Integer parseInt(final String value) {
    try {
      return Integer.parseInt(value);
    } catch (final NumberFormatException exception) {
      return null;
    }
  }

  public static Long parseLong(final String value) {
    try {
      return Long.parseLong(value);
    } catch (final NumberFormatException exception) {
      return null;
    }
  }

}
