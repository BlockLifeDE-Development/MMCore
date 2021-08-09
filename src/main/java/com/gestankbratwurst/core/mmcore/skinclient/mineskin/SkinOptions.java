package com.gestankbratwurst.core.mmcore.skinclient.mineskin;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import org.jsoup.Connection;

public class SkinOptions {

  private static final String URL_FORMAT = "name=%s&model=%s&visibility=%s";

  private final String name;
  private final Variant variant;
  private final Visibility visibility;

  @Deprecated
  private SkinOptions(final String name, final Model model, final Visibility visibility) {
    this.name = name;
    this.variant = model.toVariant();
    this.visibility = visibility;
  }

  private SkinOptions(final String name, final Variant variant, final Visibility visibility) {
    this.name = name;
    this.variant = variant;
    this.visibility = visibility;
  }

  @Deprecated
  protected String toUrlParam() {
    return String.format(URL_FORMAT, this.name, this.variant.getName(), this.visibility.getCode());
  }

  protected JsonObject toJson() {
    final JsonObject json = new JsonObject();
    if (!Strings.isNullOrEmpty(this.name)) {
      json.addProperty("name", this.name);
    }
    if (this.variant != null && this.variant != Variant.AUTO) {
      json.addProperty("variant", this.variant.getName());
    }
    if (this.visibility != null) {
      json.addProperty("visibility", this.visibility.getCode());
    }
    return json;
  }

  protected void addAsData(final Connection connection) {
    if (!Strings.isNullOrEmpty(this.name)) {
      connection.data("name", this.name);
    }
    if (this.variant != null && this.variant != Variant.AUTO) {
      connection.data("variant", this.variant.getName());
    }
    if (this.visibility != null) {
      connection.data("visibility", String.valueOf(this.visibility.getCode()));
    }
  }


  @Deprecated
  public static SkinOptions create(final String name, final Model model, final Visibility visibility) {
    return new SkinOptions(name, model, visibility);
  }

  public static SkinOptions create(final String name, final Variant variant, final Visibility visibility) {
    return new SkinOptions(name, variant, visibility);
  }

  public static SkinOptions name(final String name) {
    return new SkinOptions(name, Variant.AUTO, Visibility.PUBLIC);
  }


  public static SkinOptions none() {
    return new SkinOptions("", Variant.AUTO, Visibility.PUBLIC);
  }

}
