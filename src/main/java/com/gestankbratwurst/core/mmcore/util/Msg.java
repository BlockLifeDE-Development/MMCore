package com.gestankbratwurst.core.mmcore.util;


import lombok.Setter;
import org.bukkit.command.CommandSender;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class Msg {

  private static final char ELEMENT_START = '{';
  private static final char ELEMENT_END = '}';

  @Setter
  private static String errorPrefix = "§9Error §f>> ";
  @Setter
  private static String normalPrefix = "§9BlockLife §f>> ";
  @Setter
  private static String errorMessagePrefix = "§c";
  @Setter
  private static String messagePrefix = "§7";
  @Setter
  private static String elementPrefix = "§e";

  public static void sendInfo(final CommandSender target, final String message, final Object... elements) {
    sendFormatMessage(target, message, normalPrefix, messagePrefix, elements);
  }

  public static void sendError(final CommandSender target, final String message, final Object... elements) {
    sendFormatMessage(target, message, errorPrefix, errorMessagePrefix, elements);
  }

  private static void sendFormatMessage(final CommandSender target, final String message, final String prefix,
      final String messagePrefix, final Object[] elements) {
    final StringBuilder messageBuilder = new StringBuilder(prefix);
    messageBuilder.append(messagePrefix);
    int elementIndex = 0;
    for (int index = 0; index < message.length(); index++) {
      final char currentChar = message.charAt(index);
      if (index + 1 < message.length() && currentChar == ELEMENT_START && message.charAt(index + 1) == ELEMENT_END) {
        messageBuilder.append(elementPrefix);
        messageBuilder.append(elements[elementIndex++].toString());
        messageBuilder.append(messagePrefix);
        index++;
      } else {
        messageBuilder.append(currentChar);
      }
    }
    target.sendMessage(messageBuilder.toString());
  }

}