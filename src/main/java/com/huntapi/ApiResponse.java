package com.huntapi;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/** A successful API response: JSON or a file (image, PDF…). */
public final class ApiResponse {
  private final RawResponse raw;
  private JsonNode json;

  ApiResponse(RawResponse raw) {
    this.raw = raw;
  }

  /** True when the Content-Type is application/json or +json. */
  public boolean isJson() {
    return raw.contentType()
        .map(type -> type.split(";", 2)[0].trim().toLowerCase(Locale.ROOT))
        .map(type -> type.equals("application/json") || type.endsWith("+json"))
        .orElse(false);
  }

  /** The parsed JSON body. */
  public synchronized JsonNode json() {
    if (json == null) {
      if (!isJson()) {
        throw new HuntApiResponseDecodingException("The response is not JSON.", raw, null);
      }
      try {
        json = Serialization.MAPPER.readTree(raw.body());
      } catch (IOException e) {
        throw new HuntApiResponseDecodingException("The JSON response could not be decoded.", raw, e);
      }
    }
    return json;
  }

  /** Decodes the JSON body into {@code type} with Jackson. */
  public <T> T as(Class<T> type) {
    try {
      return Serialization.MAPPER.treeToValue(json(), type);
    } catch (IOException e) {
      throw new HuntApiResponseDecodingException("The JSON response could not be mapped to " + type.getName() + ".", raw, e);
    }
  }

  /** The body bytes. */
  public byte[] bytes() {
    return raw.body().clone();
  }

  /** The body decoded as UTF-8 text. */
  public String text() {
    return new String(raw.body(), StandardCharsets.UTF_8);
  }

  /** The Content-Type header, or null. */
  public String contentType() {
    return raw.contentType().orElse(null);
  }

  /** HTTP status code. */
  public int status() {
    return raw.status();
  }

  /** The buffered response. */
  public RawResponse rawResponse() {
    return raw;
  }
}
