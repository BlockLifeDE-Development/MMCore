package com.gestankbratwurst.core.mmcore.scoreboard;

import com.comphenix.protocol.ProtocolLibrary;
import com.gestankbratwurst.core.mmcore.scoreboard.api.PlayerBoardManager;
import com.gestankbratwurst.core.mmcore.scoreboard.api.ScoreboardHandler;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.server.ServerLoadEvent.LoadType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardManager implements Listener, Runnable, PlayerBoardManager {

  private final Map<UUID, Team> users = new Object2ObjectOpenHashMap<>();
  private final Map<UUID, PlayerBoard> boards = new Object2ObjectOpenHashMap<>();

  private final Scoreboard titleBoard;
  private final ScoreboardAPI api;

  protected ScoreboardManager(final ScoreboardAPI api) {
    this.api = api;
    ProtocolLibrary.getProtocolManager().addPacketListener(new TeamPacketListener(api.getHost(), this));

    this.titleBoard = Bukkit.getScoreboardManager().getNewScoreboard();
    Bukkit.getPluginManager().registerEvents(this, api.getHost());

    final Objective obj = this.titleBoard.registerNewObjective("sidebar", "dummy", " ");
    obj.setDisplaySlot(DisplaySlot.SIDEBAR);

    Bukkit.getScheduler().runTaskTimerAsynchronously(api.getHost(), this, 20, 5);
  }

  @Override
  public void setPriority(final Player player, final int priority) {
    this.unregisterPlayer(player);
    Bukkit.getScheduler().runTaskLater(this.api.getHost(), () -> this.addPlayer(player, priority), 1L);
  }

  private void addPlayer(final Player player, final int priority) {
    if (this.users.containsKey(player.getUniqueId())) {
      this.unregisterPlayer(player);
    }

    String name = player.getName();

    if (name.length() > 14) {
      name = name.substring(0, 14);
    }

    if (priority > 100) {
      throw new IllegalArgumentException("Priority cannot be higher than 99");
    }

    final Team team = this.titleBoard.registerNewTeam(priority + name);

    team.setPrefix("");
    team.setSuffix("");
    team.setColor(ChatColor.WHITE);
    team.addEntry(player.getName());
    this.users.put(player.getUniqueId(), team);
    
    player.setScoreboard(this.titleBoard);
    this.boards.put(player.getUniqueId(), new PlayerBoard(player));
  }

  private void unregisterPlayer(final Player player) {
    final Team team = this.users.remove(player.getUniqueId());
    final PlayerBoard board = this.boards.remove(player.getUniqueId());

    if (team == null || board == null) {
      this.api.getHost().getLogger().severe(" (ScoreboardAPI) Failed to unregister player " + player.getName());
      return;
    }

    team.unregister();
    board.deactivate();
  }

  @Override
  public void setBoard(final Player player, final ScoreboardHandler handler) {
    final PlayerBoard board = this.getBoard(player);
    if (board.isActivated()) {
      board.deactivate();
    }
    board.setHandler(handler);
    board.activate();
  }


  @Override
  public PlayerBoard getBoard(final Player player) {
    return this.boards.get(player.getUniqueId());
  }

  @Override
  public void resetBoard(final Player player) {
    this.getBoard(player).deactivate();
  }

  @Override
  public void setPrefix(final Player player, final String prefix) {
    this.users.get(player.getUniqueId()).setPrefix(prefix);
  }

  @Override
  public void setSuffix(final Player player, final String suffix) {
    this.users.get(player.getUniqueId()).setSuffix(suffix);
  }

  private Scoreboard getScoreboard() {
    return this.titleBoard;
  }

  @EventHandler
  public void handleReload(final ServerLoadEvent event) {
    if (event.getType() == LoadType.RELOAD) {
      throw new IllegalStateException("Reloads are not supported, please restart the server. Unable to inject PacketHandler!");
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void unregisterOnQuit(final PlayerQuitEvent event) {
    this.unregisterPlayer(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void registerOnJoin(final PlayerJoinEvent event) {
    this.addPlayer(event.getPlayer(), 0);
  }

  @Override
  public void run() {
    for (final PlayerBoard board : this.boards.values()) {
      if (!board.isActivated()) {
        continue;
      }
      board.update();
    }
  }
}