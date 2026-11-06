package com.huntapi;

/** Base class for every SDK-defined exception. */
public class HuntApiException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /** Creates the exception. */
  public HuntApiException(String message, Throwable cause) {
    super(message, cause);
  }
}
