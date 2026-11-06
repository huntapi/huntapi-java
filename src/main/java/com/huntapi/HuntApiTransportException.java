package com.huntapi;

/** The request failed before a complete HTTP response was received (network error, timeout). */
public final class HuntApiTransportException extends HuntApiException {
  private static final long serialVersionUID = 1L;

  /** Creates the exception. */
  public HuntApiTransportException(String message, Throwable cause) {
    super(message, cause);
  }
}
