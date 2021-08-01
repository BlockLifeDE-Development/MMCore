package com.gestankbratwurst.core.mmcore.util.common;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 25.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UtilText {

  private static final int UNICODE_START = 36864;

  private static final char COLOR_CHAR = '§';

  public static int getUnicode(final int page, final int row, final int column) {
    return UNICODE_START + (((page - 90) * 256) + (row * 16) + column);
  }

  public static char getUnicodeChar(final int page, final int row, final int column) {
    return (char) (UNICODE_START + (((page - 90) * 256) + (row * 16) + column));
  }

  public static String unicodeEscaped(final char ch) {
    if (ch < 0x10) {
      return "\\u000" + Integer.toHexString(ch);
    } else if (ch < 0x100) {
      return "\\u00" + Integer.toHexString(ch);
    } else if (ch < 0x1000) {
      return "\\u0" + Integer.toHexString(ch);
    }
    return "\\u" + Integer.toHexString(ch);
  }

  public static UUID uuidFromShortString(final String idWithoutDashes) {
    return UUID.fromString(
        idWithoutDashes.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"));
  }

  public static String translateHexColorCodes(final String startTag, final String endTag, final String message) {
    final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
    final Matcher matcher = hexPattern.matcher(message);
    final StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
    while (matcher.find()) {
      final String group = matcher.group(1);
      matcher.appendReplacement(buffer, COLOR_CHAR + "x"
          + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
          + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
          + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
      );
    }
    return matcher.appendTail(buffer).toString();
  }

}
