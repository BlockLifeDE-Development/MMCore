package com.gestankbratwurst.core.mmcore.actionbar;

import com.google.common.collect.Queues;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.PriorityQueue;
import java.util.function.Supplier;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 28.07.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ActionBarSection {

  protected ActionBarSection(final ActionBarManager actionBarManager) {
    this.sectionLines = Queues.newPriorityQueue();
    this.sectionLines.add(ActionLine.empty());
    this.actionBarManager = actionBarManager;
    this.tokenLines = new Object2ObjectOpenHashMap<>();
  }

  @Getter
  private final PriorityQueue<ActionLine> sectionLines;
  private final ActionBarManager actionBarManager;
  private final Object2ObjectMap<String, ActionLine> tokenLines;

  public ActionLine addLayer(final int priority, final Supplier<String> lineSupplier) {
    final ActionLine line = new ActionLine(priority, lineSupplier);
    this.addLayer(line);
    return line;
  }

  public void removeLayer(final ActionLine line) {
    this.sectionLines.remove(line);
  }

  public void removeToken(final String key) {
    final ActionLine line = this.tokenLines.get(key);
    if (line != null) {
      this.removeLayer(line);
      this.tokenLines.remove(key);
    }
  }

  private void removeToken(final String key, final ActionLine line) {
    if (this.tokenLines.get(key) == line) {
      this.removeLayer(line);
      this.tokenLines.remove(key);
    }
  }

  public void addTempLayer(final long lifeTicks, final ActionLine line) {
    this.sectionLines.add(line);
    this.actionBarManager.getTaskManager().runBukkitSyncDelayed(() -> this.removeLayer(line), lifeTicks);
  }

  public void setTokenLayer(final String key, final ActionLine line) {
    if (this.tokenLines.containsKey(key)) {
      this.sectionLines.remove(this.tokenLines.get(key));
    }
    this.tokenLines.put(key, line);
    this.sectionLines.add(line);
  }

  public void addTempTokenLayer(final long lifeTicks, final String key, final ActionLine line) {
    this.setTokenLayer(key, line);
    this.actionBarManager.getTaskManager().runBukkitSyncDelayed(() -> this.removeToken(key, line), lifeTicks);
  }

  public ActionLine setTokenLayer(final long lifeTicks, final String key, final int priority, final Supplier<String> lineSupplier) {
    final ActionLine line = new ActionLine(priority, lineSupplier);
    this.addTempLayer(lifeTicks, line);
    return line;
  }

  public ActionLine addTempLayer(final long lifeTicks, final int priority, final Supplier<String> lineSupplier) {
    final ActionLine line = new ActionLine(priority, lineSupplier);
    this.addTempLayer(lifeTicks, line);
    return line;
  }

  public void addLayer(final ActionLine line) {
    this.sectionLines.add(line);
  }

  public int getHighestPriority() {
    return this.sectionLines.peek().getPriority();
  }

  public ActionLine getMostSignificant() {
    return this.sectionLines.peek();
  }

}
