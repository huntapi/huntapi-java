package com.huntapi;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Buffered HTTP response.
 *
 * @param status HTTP status code
 * @param headers lower-cased header names mapped to every received value
 * @param body exact body bytes
 */
public record RawResponse(int status, Map<String, List<String>> headers, byte[] body) {
  /** Compact constructor copying the headers. */
  public RawResponse {
    headers = Map.copyOf(headers);
  }

  /** The Content-Type header, if present. */
  public Optional<String> contentType() {
    List<String> values = headers.get("content-type");
    return values == null || values.isEmpty() ? Optional.empty() : Optional.of(values.get(0));
  }
}
