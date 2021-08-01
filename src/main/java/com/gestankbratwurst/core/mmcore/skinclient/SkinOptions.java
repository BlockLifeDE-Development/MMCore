package com.gestankbratwurst.core.mmcore.skinclient;

public class SkinOptions {

  private static final String URL_FORMAT = "name=%s&model=%s&visibility=%s";

  private final String name;
  private final Model model;
  private final Visibility visibility;

  private SkinOptions(final String name, final Model model, final Visibility visibility) {
    this.name = name;
    this.model = model;
    this.visibility = visibility;
  }

  protected String toUrlParam() {
    return String.format(URL_FORMAT, this.name, this.model.getName(), this.visibility.getCode());
  }

  public static SkinOptions create(final String name, final Model model, final Visibility visibility) {
    return new SkinOptions(name, model, visibility);
  }

  public static SkinOptions name(final String name) {
    return new SkinOptions(name, Model.DEFAULT, Visibility.PUBLIC);
  }

  public static SkinOptions none() {
    return new SkinOptions("", Model.DEFAULT, Visibility.PUBLIC);
  }

}
