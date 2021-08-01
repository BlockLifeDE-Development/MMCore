package com.gestankbratwurst.core.mmcore.scoreboard;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.gestankbratwurst.core.mmcore.events.PlayerReceiveTeamEvent;
import com.gestankbratwurst.core.mmcore.protocol.packetwrapper.WrapperPlayServerScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TeamPacketListener extends PacketAdapter {

  private final JavaPlugin plugin;

  public TeamPacketListener(final JavaPlugin plugin, final ScoreboardManager manager) {
    super(plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.SCOREBOARD_TEAM);
    this.plugin = plugin;
  }

  @Override
  public void onPacketSending(final PacketEvent event) {
    if (!event.getPacketType().equals(PacketType.Play.Server.SCOREBOARD_TEAM)) {
      return;
    }
    final PacketContainer packet = event.getPacket();
    final WrapperPlayServerScoreboardTeam wrapper = new WrapperPlayServerScoreboardTeam(packet);

    final PlayerReceiveTeamEvent teamEvent = new PlayerReceiveTeamEvent(event.getPlayer(), wrapper.getPrefix(), wrapper.getSuffix(),
        wrapper.getColor(), wrapper);

    Bukkit.getScheduler().runTask(this.plugin, () -> Bukkit.getPluginManager().callEvent(teamEvent));

    wrapper.setColor(teamEvent.getColor());
    wrapper.setPrefix(WrappedChatComponent.fromJson(teamEvent.getPrefix().getJson()));
    wrapper.setSuffix(teamEvent.getSuffix());
  }
}