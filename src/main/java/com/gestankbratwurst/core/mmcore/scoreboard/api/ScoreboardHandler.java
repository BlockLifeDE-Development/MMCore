package com.gestankbratwurst.core.mmcore.scoreboard.api;

import java.util.List;
import org.bukkit.entity.Player;

/**
 * Represents the handler to determine the title and entries of a scoreboard.
 */
public interface ScoreboardHandler {

  /**
   * Determines the title to display for this player. If null returned, title automatically becomes a blank line.
   *
   * @param player player
   * @return title
   */
  String getTitle(Player player);

  /**
   * Determines the entries to display for this player. If null returned, the entries are not updated.
   *
   * @param player player
   * @return entries
   */
  List<Entry> getEntries(Player player);

}