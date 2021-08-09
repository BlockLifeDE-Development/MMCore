package com.gestankbratwurst.core.mmcore.skinclient.mineskin.data;

@Deprecated
public interface SkinCallback {

  void done(Skin skin);

  default void waiting(final long delay) {
  }

  default void uploading() {
  }

  default void error(final String errorMessage) {
  }

  default void exception(final Exception exception) {
  }

  default void parseException(final Exception exception, final String body) {
  }

}
