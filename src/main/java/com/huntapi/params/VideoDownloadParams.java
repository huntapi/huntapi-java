package com.huntapi.params;

import com.huntapi.HuntApiParams;
import com.huntapi.HuntApiSerializationException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Parameters for {@code GET / POST /v1/video/download}.
 *
 * <p>Generated from the HuntAPI OpenAPI document. Do not edit by hand.
 */
public final class VideoDownloadParams implements HuntApiParams {
  private final Map<String, Object> values;

  private VideoDownloadParams(Map<String, Object> values) {
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

  /** Builder for {@link VideoDownloadParams}. */
  public static final class Builder {
    private final Map<String, Object> values = new LinkedHashMap<>();

    private Builder() {}

    /** Required. Video URL to download. */
    public Builder query(String value) {
      values.put("query", value);
      return this;
    }

    /** Desired download type. You have three options: Default: "audio_video". */
    public Builder downloadType(String value) {
      values.put("download_type", value);
      return this;
    }

    /** Maximum duration of the downloaded content in seconds. If the video/audio is longer than this value, only the content up to this duration will be downloaded. */
    public Builder maxDuration(Number value) {
      values.put("max_duration", value);
      return this;
    }

    /** Desired video quality. Available options: Default: "best". */
    public Builder videoQuality(String value) {
      values.put("video_quality", value);
      return this;
    }

    /** Desired output format for the video. Available options: Default: "mp4". */
    public Builder videoFormat(String value) {
      values.put("video_format", value);
      return this;
    }

    /** URL of the webhook where results will be sent. When specified, the system will attempt to send the response to this URL up to 3 times with a 5-second delay between each attempt. The results will be sent as a POST request. */
    public Builder webhookUrl(String value) {
      values.put("webhook_url", value);
      return this;
    }

    /** Authorization header for the webhook. This parameter allows you to add authentication to your webhook endpoint. This value will be included in the Authorization header when making the webhook request to your server. */
    public Builder webhookAuthorization(String value) {
      values.put("webhook_authorization", value);
      return this;
    }

    /** Comma-separated list of fields to keep in the response, with dot notation for nested fields (e.g. name,location.city). Does not change the credit cost. */
    public Builder fields(String value) {
      values.put("fields", value);
      return this;
    }

    /** Process the request asynchronously: the response is a job_id to poll on /v1/jobs/{id} (see waitForJob). Default: false. */
    public Builder isAsync(Boolean value) {
      values.put("is_async", value);
      return this;
    }

    /** Return HTTP 200 even when an error occurs; the error is then in the response body. Default: false. */
    public Builder softFail(Boolean value) {
      values.put("soft_fail", value);
      return this;
    }

    /** Validates required parameters and returns the immutable {@link VideoDownloadParams}. */
    public VideoDownloadParams build() {
      if (values.get("query") == null) {
        throw new HuntApiSerializationException("The \"query\" parameter is required.", "query");
      }
      return new VideoDownloadParams(java.util.Collections.unmodifiableMap(new LinkedHashMap<>(values)));
    }
  }
}
