package com.huntapi;

import java.util.List;
import java.util.Map;

/**
 * Path, methods, response kind and parameter names of every documented operation.
 *
 * <p>Generated from the HuntAPI OpenAPI document. Do not edit by hand.
 */
public final class Endpoints {
  private Endpoints() {}

  /** One documented operation. */
  public record Endpoint(String path, List<String> methods, boolean json, List<String> required) {}

  /** Every documented operation keyed by its snake_case name. */
  public static final Map<String, Endpoint> ALL = Map.ofEntries(
      Map.entry("audio_download", new Endpoint("/v1/audio/download", List.of("GET", "POST"), true, List.of("query"))),
      Map.entry("jobs", new Endpoint("/v1/jobs/{id}", List.of("GET"), true, List.of("id"))),
      Map.entry("media_metadata", new Endpoint("/v1/media/metadata", List.of("GET"), true, List.of("query"))),
      Map.entry("stats", new Endpoint("/v1/stats", List.of("GET"), true, List.of())),
      Map.entry("usage", new Endpoint("/v1/usage", List.of("GET"), true, List.of())),
      Map.entry("video_download", new Endpoint("/v1/video/download", List.of("GET", "POST"), true, List.of("query")))
  );
}
