package com.gestankbratwurst.core.mmcore.scoreboard.api.animation;

public class HighlightedString extends FrameAnimatedString {

  protected String context;
  protected String normalFormat;
  protected String highlightFormat;
  protected String prefix = "";
  protected String suffix = "";

  public HighlightedString(final String context, final String normalFormat, final String highlightFormat) {
    super();
    this.context = context;
    this.normalFormat = normalFormat;
    this.highlightFormat = highlightFormat;
    this.generateFrames();
  }

  public HighlightedString(final String context, final String normalFormat, final String highlightFormat, final String prefix,
      final String suffix) {
    super();
    this.context = context;
    this.normalFormat = normalFormat;
    this.highlightFormat = highlightFormat;
    this.prefix = prefix;
    this.suffix = suffix;
    this.generateFrames();
  }

  protected void generateFrames() {
    int index = 0;
    while (index < this.context.length()) {
      if (this.context.charAt(index) != ' ') {
        final String highlighted =
            this.normalFormat + this.context.substring(0, index) + this.highlightFormat + this.context.charAt(index) + this.normalFormat
                + this.context.substring(index + 1, this.context.length());
        final String whole = this.prefix + highlighted + this.suffix;
        this.addFrame(whole);
      } else {
        this.addFrame(this.prefix + this.normalFormat + this.context + this.suffix);
      }
      index++;
    }
  }

  public String getContext() {
    return this.context;
  }

  public String getNormalColor() {
    return this.normalFormat;
  }

  public String getHighlightColor() {
    return this.highlightFormat;
  }

  public String getPrefix() {
    return this.prefix;
  }

  public String getSuffix() {
    return this.suffix;
  }

}