/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.scoreboard.api.common.animation.ScrollableString can not be copied and/or distributed without the express
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

package com.gestankbratwurst.core.mmcore.scoreboard.api.animation;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;

public class ScrollableString extends FrameAnimatedString {

  private int position;
  private final List<String> list;
  private ChatColor color = ChatColor.RESET;

  public ScrollableString(String message, int width, int spaceBetween) {
    this.list = new ArrayList<>();
    // String is too short for window?
    if (message.length() < width) {
      final StringBuilder sb = new StringBuilder(message);
      while (sb.length() < width) {
        sb.append(" ");
      }
      message = sb.toString();
    }
    // Allow for colours which add 2 to the width
    width -= 2;
    // Invalid width/space size
    if (width < 1) {
      width = 1;
    }
    if (spaceBetween < 0) {
      spaceBetween = 0;
    }
    // Add substrings
    for (int i = 0; i < message.length() - width; i++) {
      this.list.add(message.substring(i, i + width));
    }
    // Add space between repeats
    final StringBuilder space = new StringBuilder();
    for (int i = 0; i < spaceBetween; ++i) {
      this.list.add(message.substring(message.length() - width + (Math.min(i, width)), message.length()) + space);
      if (space.length() < width) {
        space.append(" ");
      }
    }
    // Wrap
    for (int i = 0; i < width - spaceBetween; ++i) {
      this.list.add(message.substring(message.length() - width + spaceBetween + i, message.length()) + space + message.substring(0, i));
    }
    // Join up
    for (int i = 0; i < spaceBetween; i++) {
      if (i > space.length()) {
        break;
      }
      this.list.add(space.substring(0, space.length() - i) + message.substring(0, width - (Math.min(spaceBetween, width)) + i));
    }
  }

  @Override
  public String next() {
    StringBuilder sb = this.getNext();
    if (sb.charAt(sb.length() - 1) == ChatColor.COLOR_CHAR) {
      sb.setCharAt(sb.length() - 1, ' ');
    }
    if (sb.charAt(0) == ChatColor.COLOR_CHAR) {
      final ChatColor c = ChatColor.getByChar(sb.charAt(1));
      if (c != null) {
        this.color = c;
        sb = this.getNext();
        if (sb.charAt(0) != ' ') {
          sb.setCharAt(0, ' ');
        }
      }
    }
    return this.color + sb.toString();
  }

  private StringBuilder getNext() {
    return new StringBuilder(this.list.get(this.position++ % this.list.size()));
  }

}