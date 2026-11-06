package com.huntapi;

/** An async job did not reach a terminal status before the deadline. */
public final class HuntApiJobTimeoutException extends HuntApiException {
  private static final long serialVersionUID = 1L;
  private final String jobId;
  private final transient ApiResponse lastResponse;

  /** Creates the exception. */
  public HuntApiJobTimeoutException(String jobId, ApiResponse lastResponse) {
    super("Job " + jobId + " did not finish before the deadline.", null);
    this.jobId = jobId;
    this.lastResponse = lastResponse;
  }

  /** The job id. */
  public String jobId() {
    return jobId;
  }

  /** The last job document received. */
  public ApiResponse lastResponse() {
    return lastResponse;
  }
}
