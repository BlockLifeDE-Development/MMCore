/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.scoreboard.api.common.Strings can not be copied and/or distributed without the express
 *  permission of crysis992
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of AvarionCraft.de and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to AvarionCraft.de
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from AvarionCraft.de.
 *
 */

package com.gestankbratwurst.core.mmcore.scoreboard.api;

import org.bukkit.ChatColor;

public final class Strings {

  private Strings() {
  }

  public static String format(final String string) {
    return ChatColor.translateAlternateColorCodes('&', string);
  }

  public static String repeat(final String string, final int count) {
    if (count <= 1) {
      return count == 0 ? "" : string;
    } else {
      final int len = string.length();
      final long longSize = (long) len * (long) count;
      final int size = (int) longSize;
      if ((long) size != longSize) {
        throw new ArrayIndexOutOfBoundsException("Required array size too large: " + longSize);
      } else {
        final char[] array = new char[size];
        string.getChars(0, len, array, 0);
        int n;
        for (n = len; n < size - n; n <<= 1) {
          System.arraycopy(array, 0, array, n, n);
        }
        System.arraycopy(array, 0, array, n, size - n);
        return new String(array);
      }
    }
  }

}
