package com.huntapi;

/** The client configuration is missing or unusable. */
public final class HuntApiConfigurationException extends HuntApiException {
  private static final long serialVersionUID = 1L;

  /** Creates the exception. */
  public HuntApiConfigurationException(String message) {
    super(message, null);
  }

  /** Creates the exception with a cause. */
  public HuntApiConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }
}
