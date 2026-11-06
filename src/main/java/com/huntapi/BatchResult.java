package com.huntapi;

/**
 * Outcome of one {@code batch} item.
 *
 * @param index position of the item in the input
 * @param value the task result when {@link #ok()}
 * @param error what the task threw, or null
 * @param <T> task result type
 */
public record BatchResult<T>(int index, T value, Throwable error) {
  /** True when the task succeeded. */
  public boolean ok() {
    return error == null;
  }
}
