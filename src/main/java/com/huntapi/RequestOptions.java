package com.huntapi;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/** Per-request options. */
public final class RequestOptions {
  private static final RequestOptions DEFAULTS = builder().build();

  private final String method;
  private final Duration timeout;
  private final Map<String, String> headers;
  private final Integer maxRetries;

  private RequestOptions(Builder builder) {
    this.method = builder.method;
    this.timeout = builder.timeout;
    this.maxRetries = builder.maxRetries;
    this.headers = Map.copyOf(builder.headers);
  }

  /** Options with no overrides. */
  public static RequestOptions defaults() {
    return DEFAULTS;
  }

  /** Shortcut for {@code builder().method(method).build()}. */
  public static RequestOptions method(String method) {
    return builder().method(method).build();
  }

  /** Returns a new builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** GET or POST, or null for the endpoint default. */
  public String method() {
    return method;
  }

  /** Deadline for this request, or null for the client default. */
  public Duration timeout() {
    return timeout;
  }

  /** Extra headers for this request. */
  public Map<String, String> headers() {
    return headers;
  }

  /** Retry count for this request, or null for the client default. */
  public Integer maxRetries() {
    return maxRetries;
  }

  /** Builder for {@link RequestOptions}. */
  public static final class Builder {
    private String method;
    private Duration timeout;
    private Integer maxRetries;
    private final Map<String, String> headers = new LinkedHashMap<>();

    private Builder() {}

    /** Overrides the client retry count (0 disables retries). */
    public Builder maxRetries(int maxRetries) {
      this.maxRetries = maxRetries;
      return this;
    }

    /** GET (default when supported) sends query parameters; POST sends a JSON body. */
    public Builder method(String method) {
      this.method = method == null ? null : method.toUpperCase(Locale.ROOT);
      return this;
    }

    /** Overrides the client deadline. */
    public Builder timeout(Duration timeout) {
      this.timeout = timeout;
      return this;
    }

    /** Adds a header to this request. */
    public Builder header(String name, String value) {
      headers.put(name, value);
      return this;
    }

    /** Creates the options. */
    public RequestOptions build() {
      return new RequestOptions(this);
    }
  }
}
