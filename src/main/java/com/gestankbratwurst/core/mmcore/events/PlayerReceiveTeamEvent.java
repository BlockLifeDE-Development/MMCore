/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.util.scoreboard.api.events.PlayerReceiveTeamEvent can not be copied and/or distributed without the express
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

package com.gestankbratwurst.core.mmcore.events;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.gestankbratwurst.core.mmcore.protocol.packetwrapper.WrapperPlayServerScoreboardTeam;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerReceiveTeamEvent extends Event {

  public PlayerReceiveTeamEvent(final Player receiver, final WrappedChatComponent prefix, final WrappedChatComponent suffix,
      final ChatColor color, final WrapperPlayServerScoreboardTeam wrapper) {
    this.prefix = prefix;
    this.suffix = suffix;
    this.color = color;
    this.receiver = receiver;
    this.wrapper = wrapper;
  }

  private final Player receiver;
  private WrappedChatComponent prefix;
  private WrappedChatComponent suffix;
  private ChatColor color;
  private final WrapperPlayServerScoreboardTeam wrapper;

  private static final HandlerList handlers = new HandlerList();

  // public Optional<Player> getTarget() {
  // if (wrapper.getPlayers().isEmpty()) {
  // Optional<Team> t =
  // BoardManager.get().getUsers().values().stream().filter(entry ->
  // entry.getName().equals(this.wrapper.getName())).findFirst();
  // if (t.isPresent()) {
  // Team team = t.get();
  //
  // if (team.getEntries().isEmpty()) return Optional.empty();
  //
  // Player target =
  // Bukkit.getPlayerExact(team.getEntries().iterator().next());
  // return (target == null) ? Optional.empty() : Optional.of(target);
  // } else {
  // return Optional.empty();
  // }
  // }
  //
  // Player target = Bukkit.getPlayerExact(wrapper.getPlayers().get(0));
  // return (target == null) ? Optional.empty() : Optional.of(target);
  // }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  public WrappedChatComponent getPrefix() {
    return this.prefix;
  }

  public void setPrefix(final WrappedChatComponent prefix) {
    this.prefix = prefix;
  }

  public WrappedChatComponent getSuffix() {
    return this.suffix;
  }

  public void setSuffix(final WrappedChatComponent suffix) {
    this.suffix = suffix;
  }

  public ChatColor getColor() {
    return this.color;
  }

  public void setColor(final ChatColor color) {
    this.color = color;
  }

  public Player getReceiver() {
    return this.receiver;
  }

  public WrapperPlayServerScoreboardTeam getWrapper() {
    return this.wrapper;
  }

}
