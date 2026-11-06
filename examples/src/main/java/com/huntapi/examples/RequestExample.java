package com.huntapi.examples;

import com.huntapi.ApiResponse;
import com.huntapi.HuntApiApiException;
import com.huntapi.HuntApiClient;
import java.util.Map;

/** Run with HUNTAPI_API_KEY set. */
public final class RequestExample {
  private RequestExample() {}

  public static void main(String[] args) throws Exception {
    HuntApiClient client = HuntApiClient.fromEnv();
    try {
      ApiResponse response = client.request("GET", "/v1/usage", Map.of());
      System.out.println(response.text());
    } catch (HuntApiApiException e) {
      System.err.println("HTTP " + e.status() + ": " + e.getMessage());
    }
  }
}
