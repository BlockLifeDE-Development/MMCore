package com.gestankbratwurst.core.mmcore.scoreboard.api;

import com.gestankbratwurst.core.mmcore.scoreboard.PlayerBoard;
import org.bukkit.entity.Player;

public interface PlayerBoardManager {

  void setBoard(final Player player, final ScoreboardHandler handler);

  PlayerBoard getBoard(final Player player);

  void resetBoard(final Player player);

  void setPrefix(final Player player, final String prefix);

  void setSuffix(final Player player, final String suffix);

  void setPriority(Player player, int priority);
  
}
