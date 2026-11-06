package com.huntapi.examples;

import com.huntapi.ApiResponse;
import com.huntapi.HuntApiClient;
import com.huntapi.params.MediaMetadataParams;

/** Run with HUNTAPI_API_KEY set. */
public final class JsonExample {
  private JsonExample() {}

  public static void main(String[] args) throws Exception {
    HuntApiClient client = HuntApiClient.fromEnv();
    ApiResponse response = client.mediaMetadata(MediaMetadataParams.builder().query("https://www.youtube.com/watch?v=Z5YP8kznWZs").build());
    System.out.println(response.json().toPrettyString());
  }
}
