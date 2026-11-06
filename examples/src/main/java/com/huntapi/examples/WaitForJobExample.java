package com.huntapi.examples;

import com.huntapi.ApiResponse;
import com.huntapi.HuntApiClient;
import com.huntapi.params.VideoDownloadParams;
import java.time.Duration;

/** Run with HUNTAPI_API_KEY set. */
public final class WaitForJobExample {
  private WaitForJobExample() {}

  public static void main(String[] args) throws Exception {
    HuntApiClient client = HuntApiClient.fromEnv();
    ApiResponse submitted = client.videoDownload(VideoDownloadParams.builder().query("https://www.youtube.com/watch?v=Z5YP8kznWZs").videoQuality("720p").build());
    ApiResponse job = client.waitForJob(
        submitted.json().get("job_id").asText(),
        Duration.ofSeconds(3),
        Duration.ofHours(12),
        progress -> System.out.println(progress.path("status").asText() + " " + progress.path("progress").asInt()));
    System.out.println(job.json().toPrettyString());
  }
}
