# HuntAPI Java SDK

The official Java SDK for the [HuntAPI](https://www.huntapi.com) media download API.

Download video and audio from public media URLs, or extract their metadata, with one async REST API.

The SDK exposes **6 typed methods**, one per documented endpoint, each with a parameter builder, plus a generic `request()`. It uses `java.net.http.HttpClient` and Jackson.

## Requirements

- Java 17 or newer

## Installation

Maven:

```xml
<dependency>
  <groupId>com.huntapi</groupId>
  <artifactId>huntapi</artifactId>
  <version>0.1.0</version>
</dependency>
```

Gradle:

```kotlin
implementation("com.huntapi:huntapi:0.1.0")
```

## Get your API key

[Sign up for HuntAPI](https://app.huntapi.com/register) or sign in, then copy your API key from the [dashboard](https://app.huntapi.com). `HuntApiClient.fromEnv()` reads the `HUNTAPI_API_KEY` environment variable.

## Quick start

```java
import com.huntapi.ApiResponse;
import com.huntapi.HuntApiClient;
import com.huntapi.params.MediaMetadataParams;

HuntApiClient client = HuntApiClient.fromEnv();
ApiResponse response = client.mediaMetadata(MediaMetadataParams.builder().query("https://www.youtube.com/watch?v=Z5YP8kznWZs").build());
System.out.println(response.json().toPrettyString());
```

Parameter builders mirror the [API reference](https://docs.huntapi.com); `build()` checks required parameters. Use `response.as(MyType.class)` to map the JSON onto your own types.

## Async jobs

Download endpoints are asynchronous: they return a `job_id`, and jobs can run for up to 12 hours. `waitForJob` polls `GET /v1/jobs/{id}` until a terminal status (`CompletedJob`, `Error`, `NotFound`, `BadRequest`):

```java
ApiResponse submitted = client.videoDownload(VideoDownloadParams.builder().query("https://www.youtube.com/watch?v=Z5YP8kznWZs").videoQuality("720p").build());
ApiResponse job = client.waitForJob(
    submitted.json().get("job_id").asText(),
    Duration.ofSeconds(3),
    Duration.ofHours(12),
    progress -> System.out.println(progress.path("status").asText() + " " + progress.path("progress").asInt()));
```

## GET or POST

Methods use GET when the endpoint supports it (query string). POST sends the same parameters as JSON:

```java
client.audioDownload(AudioDownloadParams.builder().query("test").build(), RequestOptions.method("POST"));
```

## Any endpoint

```java
ApiResponse response = client.request("GET", "/v1/usage", Map.of());
```

## Errors

```java
try {
  client.mediaMetadata(MediaMetadataParams.builder().query("https://www.youtube.com/watch?v=Z5YP8kznWZs").build());
} catch (HuntApiApiException e) {
  System.err.println(e.status() + ": " + e.getMessage());
}
```

| Exception | When |
| --- | --- |
| `HuntApiConfigurationException` | Missing API key, invalid options or unsupported method. |
| `HuntApiSerializationException` | A required parameter is missing or a value can't be sent. |
| `HuntApiTransportException` | Network failure, timeout or interruption. |
| `HuntApiApiException` | Non-2xx status (`status()`, `errorMessage()`, `responseBody()`, `rawResponse()`). |
| `HuntApiResponseDecodingException` | A success response could not be decoded. |

All of them extend the unchecked `HuntApiException`.

## Retries

Requests are retried up to 3 times after a network error, a `408`, `500`, `502`, `503` or `504`, or a rate limit — which the API returns as `401` (or `429`) with a `Rate limit exceeded` message. Per-key quota errors (`…: quota daily`) are not retried, nor are `400`, `402`, `404` or client-side timeouts. The delay starts at 1 second and grows 1.5× per retry (capped at 30 seconds); a `Retry-After` header wins.

```java
HuntApiClient client = HuntApiClient.builder().maxRetries(5).retryDelay(Duration.ofMillis(500)).build();
client.mediaMetadata(MediaMetadataParams.builder().query("https://www.youtube.com/watch?v=Z5YP8kznWZs").build(), RequestOptions.builder().maxRetries(0).build()); // one request without retries
```

## Batch requests

`batch()` runs a function per item on 10 threads (`concurrency`) and returns one `BatchResult` per item, in input order. A failing item never stops the others.

```java
List<String> inputs = List.of("https://www.youtube.com/watch?v=Z5YP8kznWZs", "https://www.youtube.com/watch?v=jNQXAC9IVRw");
List<BatchResult<ApiResponse>> results = client.batch(inputs, 5, value ->
    client.mediaMetadata(MediaMetadataParams.builder().query(value).build()));
for (BatchResult<ApiResponse> result : results) {
  if (result.ok()) {
    System.out.println(result.value().json());
  } else {
    System.err.println(inputs.get(result.index()) + ": " + result.error().getMessage());
  }
}
```

| Constant | Default | Description |
| --- | --- | --- |
| `HuntApiClient.DEFAULT_CONCURRENCY` | `10` | Tasks run at the same time by batch |
| `HuntApiClient.DEFAULT_TIMEOUT` | `Duration.ofSeconds(120)` | Request timeout |
| `HuntApiClient.MAX_RETRIES` | `3` | Retry attempts after a network error, a 408/5xx or a rate limit |
| `HuntApiClient.RETRY_BACKOFF_FACTOR` | `1.5` | Exponential backoff multiplier |
| `HuntApiClient.DEFAULT_RETRY_DELAY` | `Duration.ofSeconds(1)` | Delay before the first retry |

## Configuration

```java
HuntApiClient client = HuntApiClient.builder()
    .apiKey(System.getenv("HUNTAPI_API_KEY"))
    .timeout(Duration.ofSeconds(60)) // default 120 s, null disables
    .header("X-Trace", "abc")
    .transport(HuntApiTransport.fromHttpClient(myHttpClient)) // optional
    .build();
```

## Endpoints

6 methods, generated from the [OpenAPI document](https://app.huntapi.com/openapi).

| Method | HTTP | Description |
| --- | --- | --- |
| `audioDownload()` | GET / POST `/v1/audio/download` | Download audio from a URL via HuntAPI and receive a job id. |
| `jobs()` | GET `/v1/jobs/{id}` | Poll status, progress (0–100), and result of an async HuntAPI job. |
| `mediaMetadata()` | GET `/v1/media/metadata` | Extract metadata from a media URL without downloading the file. |
| `stats()` | GET `/v1/stats` | Infrastructure health for an engine or endpoint over the last hour. Not scoped to your account or tenant. Enabled per account: HTTP 403 mea… |
| `usage()` | GET `/v1/usage` | Retrieve real-time usage statistics and credit balance for your Huntapi account. |
| `videoDownload()` | GET / POST `/v1/video/download` | Video Download |

## License

MIT — see [LICENSE](LICENSE).
