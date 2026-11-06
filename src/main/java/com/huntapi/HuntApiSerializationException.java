package com.huntapi;

/** A request value cannot be represented safely on the wire, or a required parameter is missing. */
public final class HuntApiSerializationException extends HuntApiException {
  private static final long serialVersionUID = 1L;
  private final String param;

  /** Creates the exception. */
  public HuntApiSerializationException(String message, String param) {
    this(message, param, null);
  }

  /** Creates the exception with a cause. */
  public HuntApiSerializationException(String message, String param, Throwable cause) {
    super(message, cause);
    this.param = param;
  }

  /** The parameter at fault, or null. */
  public String param() {
    return param;
  }
}
