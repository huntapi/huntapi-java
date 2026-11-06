package com.huntapi;

import java.util.Map;

/** Parameters of one endpoint, in wire order. Implemented by every class in {@code com.huntapi.params}. */
public interface HuntApiParams {
  /** Wire name to value; null values are omitted from requests. */
  Map<String, Object> toMap();
}
