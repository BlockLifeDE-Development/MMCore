package com.gestankbratwurst.core.mmcore.util.common;

import com.google.common.collect.ImmutableMap;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 06.11.2020
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UtilTime {

  private static final ImmutableMap<Character, ChronoUnit> CHRONO_UNIT_SHORTS = ImmutableMap.<Character, ChronoUnit>builder()
      .put('Y', ChronoUnit.YEARS)
      .put('M', ChronoUnit.MONTHS)
      .put('W', ChronoUnit.WEEKS)
      .put('D', ChronoUnit.DAYS)
      .put('h', ChronoUnit.HOURS)
      .put('m', ChronoUnit.MINUTES)
      .put('s', ChronoUnit.SECONDS)
      .build();

  private static final long SECOND = 1000L;
  private static final long MINUTE = 60 * UtilTime.SECOND;
  private static final long HOUR = 60 * UtilTime.MINUTE;
  private static final long DAY = 24 * UtilTime.HOUR;
  private static final long WEEK = 7 * UtilTime.DAY;
  private static final long MONTH = 4 * UtilTime.WEEK;

  public static ChronoUnit toChrono(final Character character) {
    return UtilTime.CHRONO_UNIT_SHORTS.get(character);
  }

  public static Duration toDuration(final String format) {
    final String[] values = format.contains(":") ? format.split(":") : new String[]{format};
    if (values.length == 0) {
      return null;
    }
    long seconds = 0L;
    for (String entry : values) {
      final ChronoUnit unit = UtilTime.toChrono(entry.charAt(entry.length() - 1));
      if (unit == null) {
        return null;
      }
      entry = entry.replace("0", "");
      try {
        final int unitVal = Integer.parseInt(entry.substring(0, entry.length() - 1));
        seconds += unitVal * unit.getDuration().getSeconds();
      } catch (final NumberFormatException e) {
        return null;
      }
    }
    return Duration.ofSeconds(seconds);
  }

  public static String format(final Duration duration) {
    long millis = duration.toMillis();
    final StringBuilder builder = new StringBuilder();

    final long months = millis / UtilTime.MONTH;
    millis %= UtilTime.MONTH;

    final long weeks = millis / UtilTime.WEEK;
    millis %= UtilTime.WEEK;

    final long days = millis / UtilTime.DAY;
    millis %= UtilTime.DAY;

    final long hours = millis / UtilTime.HOUR;
    millis %= UtilTime.HOUR;

    final long minutes = millis / UtilTime.MINUTE;
    millis %= UtilTime.MINUTE;

    final long seconds = millis / UtilTime.SECOND;

    if (months + weeks + days > 0) {
      if (months > 0) {
        builder.append(months + " " + "Monat" + (months > 1 ? "e" : "") + " ");
      }
      if (weeks > 0) {
        builder.append(weeks + " " + "Woche" + (weeks > 1 ? "n" : "") + " ");
      }
      if (days > 0) {
        builder.append(days + " " + "Tag" + (days > 1 ? "e" : "") + " ");
      }
    } else {
      builder.append(String.format("%02dh:%02dm:%02ds", hours, minutes, seconds));
    }

    return builder.toString();
  }

}
