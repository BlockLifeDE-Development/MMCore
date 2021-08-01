package com.gestankbratwurst.core.mmcore.scoreboard.api;

import java.util.LinkedList;
import java.util.List;

public class EntryBuilder {

  private final LinkedList<Entry> entries = new LinkedList<>();

  /**
   * Append a blank line.
   *
   * @return this
   */
  public EntryBuilder blank() {
    return this.next("");
  }

  /**
   * Append a new line with specified text.
   *
   * @param string text
   * @return this
   */
  public EntryBuilder next(final String string) {
    this.entries.add(new Entry(string, this.entries.size()));
    return this;
  }

  /**
   * Returns a map of entries.
   *
   * @return map
   */
  public List<Entry> build() {
    for (final Entry entry : this.entries) {
      entry.setPosition(this.entries.size() - entry.getPosition());
    }
    return this.entries;
  }
}