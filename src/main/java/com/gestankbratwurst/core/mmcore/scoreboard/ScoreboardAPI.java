package com.gestankbratwurst.core.mmcore.scoreboard;

import org.bukkit.plugin.java.JavaPlugin;

public class ScoreboardAPI {

  private final JavaPlugin host;
  private final ScoreboardManager boardManager;

  public ScoreboardAPI(final JavaPlugin host) {
    this.host = host;
    this.boardManager = new ScoreboardManager(this);
  }

  public ScoreboardManager getBoardManager() {
    return this.boardManager;
  }

  protected JavaPlugin getHost() {
    return this.host;
  }

}
