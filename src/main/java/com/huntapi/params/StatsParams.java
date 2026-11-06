package com.huntapi.params;

import com.huntapi.HuntApiParams;
import com.huntapi.HuntApiSerializationException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Parameters for {@code GET /v1/stats}.
 *
 * <p>Generated from the HuntAPI OpenAPI document. Do not edit by hand.
 */
public final class StatsParams implements HuntApiParams {
  private final Map<String, Object> values;

  private StatsParams(Map<String, Object> values) {
    this.values = values;
  }

  /** Returns a new builder. */
  public static Builder builder() {
    return new Builder();
  }

  @Override
  public Map<String, Object> toMap() {
    return values;
  }

  /** Builder for {@link StatsParams}. */
  public static final class Builder {
    private final Map<String, Object> values = new LinkedHashMap<>();

    private Builder() {}

    /** API family to check, for example video. Provide at least engine or path. */
    public Builder engine(String value) {
      values.put("engine", value);
      return this;
    }

    /** Specific endpoint to check, for example /v1/video/download. If you also pass engine, use an endpoint from that API. */
    public Builder path(String value) {
      values.put("path", value);
      return this;
    }

    /** Validates required parameters and returns the immutable {@link StatsParams}. */
    public StatsParams build() {
      return new StatsParams(java.util.Collections.unmodifiableMap(new LinkedHashMap<>(values)));
    }
  }
}
