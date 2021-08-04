package com.gestankbratwurst.core.mmcore.protocol.holograms.infobars;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class InfoBarRunnable implements Runnable {

  public InfoBarRunnable(final JavaPlugin host, final InfoBarManager manager) {
    this.tickQueues = Lists.newArrayListWithExpectedSize(20);
    for (int index = 0; index < 20; index++) {
      this.tickQueues.add(Sets.newHashSet());
    }
    this.positionMapping = Maps.newHashMap();
    this.manager = manager;
    Bukkit.getScheduler().runTaskTimer(host, this, 1L, 1L);
  }

  private final InfoBarManager manager;
  private final ArrayList<Set<Player>> tickQueues;
  private final Map<Player, Integer> positionMapping;
  private int queueTick = 0;

  private int getSmallestSetIndex() {

    Set<Player> smallest = this.tickQueues.get(0);
    int currentIndex = 0;

    for (int index = 1; index < this.tickQueues.size(); index++) {
      if (smallest.size() == 0) {
        break;
      }
      final Set<Player> next = this.tickQueues.get(index);
      if (next.size() < smallest.size()) {
        smallest = next;
        currentIndex = index;
      }
    }

    return currentIndex;
  }

  public void addPlayer(final Player player) {
    final int smallest = this.getSmallestSetIndex();
    this.tickQueues.get(smallest).add(player);
    this.positionMapping.put(player, smallest);
  }

  public void removePlayer(final Player player) {
    this.tickQueues.get(this.positionMapping.get(player)).remove(player);
    this.positionMapping.remove(player);
  }

  private void checkPlayer(final Player player) {
    for (final AbstractInfoBar info : this.manager.playerViews.get(player)) {
      if (info.isInLineOfSight(player)) {
        if (!info.viewingPlayer.contains(player)) {
          info.showTo(player);
        }
      } else {
        info.hideFrom(player);
      }
    }
  }

  @Override
  public void run() {

    for (final Player player : this.tickQueues.get(this.queueTick++)) {
      this.checkPlayer(player);
    }

    if (this.queueTick == 20) {
      this.queueTick = 0;
    }
  }

}
