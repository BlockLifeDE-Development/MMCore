package com.gestankbratwurst.core.mmcore.skinclient.mineskin.data;

public class MineskinException extends RuntimeException {

  public MineskinException() {
  }

  public MineskinException(final String message) {
    super(message);
  }

  public MineskinException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public MineskinException(final Throwable cause) {
    super(cause);
  }

  public MineskinException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
