package com.huntapi;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Sends one HTTP request and buffers the response. Implement it to plug in another HTTP stack. */
@FunctionalInterface
public interface HuntApiTransport {
  /** Sends the request. */
  RawResponse send(Request request) throws IOException, InterruptedException;

  /**
   * One outgoing request.
   *
   * @param method GET or POST
   * @param uri absolute URL with the query string
   * @param headers request headers
   * @param body JSON body for POST, else null
   * @param timeout deadline, or null
   */
  record Request(String method, URI uri, Map<String, String> headers, byte[] body, Duration timeout) {}

  /** The default transport: {@link HttpClient} that never follows redirects. */
  static HuntApiTransport defaultTransport() {
    return fromHttpClient(HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NEVER).build());
  }

  /** Adapts an existing {@link HttpClient}. */
  static HuntApiTransport fromHttpClient(HttpClient client) {
    return request -> {
      HttpRequest.Builder builder = HttpRequest.newBuilder(request.uri());
      if (request.timeout() != null) {
        builder.timeout(request.timeout());
      }
      request.headers().forEach(builder::setHeader);
      builder.method(
          request.method(),
          request.body() == null
              ? HttpRequest.BodyPublishers.noBody()
              : HttpRequest.BodyPublishers.ofByteArray(request.body()));
      HttpResponse<byte[]> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofByteArray());
      Map<String, List<String>> headers = new LinkedHashMap<>();
      response.headers().map().forEach((name, values) -> headers.put(name.toLowerCase(Locale.ROOT), List.copyOf(values)));
      return new RawResponse(response.statusCode(), headers, response.body());
    };
  }
}
