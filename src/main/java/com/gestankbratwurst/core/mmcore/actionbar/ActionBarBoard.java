package com.gestankbratwurst.core.mmcore.actionbar;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of avarioncore and was created at the 23.03.2020
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ActionBarBoard {

  protected static final int MIN_SECTION_LENGTH = 20;

  protected ActionBarBoard(final UUID playerID, final ActionBarManager actionBarManager) {
    this.playerID = playerID;
    this.sections = new ActionBarSection[3];
    this.sections[0] = new ActionBarSection(actionBarManager);
    this.sections[1] = new ActionBarSection(actionBarManager);
    this.sections[2] = new ActionBarSection(actionBarManager);
  }

  private final UUID playerID;

  private final ActionBarSection[] sections;
  @Getter
  private String currentDisplay = "";

  public void update() {
    final String left = this.sections[0].getMostSignificant().getLineSupplier().get();
    String middle = this.sections[1].getMostSignificant().getLineSupplier().get();
    final String right = this.sections[2].getMostSignificant().getLineSupplier().get();
    final int pad = Math.max(1, (MIN_SECTION_LENGTH - middle.length()) / 2);
    if (left.isEmpty() && middle.isEmpty() && right.isEmpty()) {
      this.currentDisplay = "";
    } else {
      middle = Strings.repeat(" ", pad) + middle + Strings.repeat(" ", pad);
      this.currentDisplay = "§f" + left + "§7 | §f" + middle + "§7 | §f" + right;
    }
  }

  public ActionBarSection getSection(final Section section) {
    return this.getSection(section.index);
  }

  public ActionBarSection getSection(final int index) {
    Preconditions.checkArgument(index < 3, "Index must be below 3.");
    return this.sections[index];
  }

  @AllArgsConstructor
  public enum Section {
    LEFT(0), MIDDLE(1), RIGHT(2);
    @Getter
    private final int index;
  }

}
