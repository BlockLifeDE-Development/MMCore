package com.gestankbratwurst.core.mmcore.resourcepack.distribution;

import com.gestankbratwurst.core.mmcore.util.Msg;
import com.gestankbratwurst.core.mmcore.util.tasks.TaskManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;

public class ResourcePackListener implements Listener {

  public ResourcePackListener(final ResourcepackManager manager) {
    this.manager = manager;
    this.taskManager = TaskManager.getInstance();
  }

  private final ResourcepackManager manager;
  private final TaskManager taskManager;

  @EventHandler(priority = EventPriority.HIGH)
  public void onJoin(final PlayerJoinEvent event) {
    final Player player = event.getPlayer();
    this.taskManager.runBukkitSyncDelayed(() -> this.sendResourcePack(player), 20L);
  }

  @EventHandler
  public void resourceStatusEvent(final PlayerResourcePackStatusEvent event) {
    final Player player = event.getPlayer();
    final Status status = event.getStatus();
    if (status == Status.SUCCESSFULLY_LOADED) {
      Msg.sendInfo(player, "Das Resourcepack wurde akzeptiert.");
    } else if (status == Status.FAILED_DOWNLOAD) {
      // TODO enable on production server
//      if (attempts.contains(id)) {
//        attempts.remove(id);
//        player.kickPlayer("Bitte akzeptiere das Resourcepack.");
//      } else {
//        attempts.add(id);
//        plugin.getTaskManager().runBukkitSyncDelayed(() -> sendResourcepack(player), 100L);
//      }
    } else if (status == Status.DECLINED) {
      player.kickPlayer("Bitte akzeptiere das Resourcepack.");
    }
  }

  private void sendResourcePack(final Player player) {
    player.setResourcePack(this.manager.getDownloadURL(), this.manager.getResourceHash());
  }

}