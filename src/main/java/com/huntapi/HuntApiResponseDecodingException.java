package com.huntapi;

/** A successful response could not be decoded as the documented representation. */
public final class HuntApiResponseDecodingException extends HuntApiException {
  private static final long serialVersionUID = 1L;
  private final transient RawResponse rawResponse;

  /** Creates the exception. */
  public HuntApiResponseDecodingException(String message, RawResponse rawResponse, Throwable cause) {
    super(message, cause);
    this.rawResponse = rawResponse;
  }

  /** The buffered response. */
  public RawResponse rawResponse() {
    return rawResponse;
  }
}
