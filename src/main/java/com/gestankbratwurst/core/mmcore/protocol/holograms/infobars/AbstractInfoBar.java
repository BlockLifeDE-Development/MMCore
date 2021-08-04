package com.gestankbratwurst.core.mmcore.protocol.holograms.infobars;

import com.gestankbratwurst.core.mmcore.protocol.holograms.impl.infobar.InfoBar.InfoLineSpacing;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.function.Function;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class AbstractInfoBar {

  public AbstractInfoBar(final Entity entity, final InfoBarManager infoBarManager) {
    this.viewingPlayer = Sets.newHashSet();
    this.entity = entity;
    this.infoBarManager = infoBarManager;
  }

  protected final Entity entity;
  //TODO use map <Player, PlayerConnection>
  protected final Set<Player> viewingPlayer;
  protected final InfoBarManager infoBarManager;

  protected abstract void showTo(Player player);

  protected abstract void hideFrom(Player player);

  public abstract boolean isInLineOfSight(Player player);

  public abstract void editLine(int index, Function<String, String> lineEdit);

  public abstract void setLine(int index, String newLine);

  public abstract void addLine(String newLine, InfoLineSpacing spacing);

  public abstract int getSize();

  public Entity getEntity() {
    return this.entity;
  }

}