package com.kosmin.exception;

public class PrimaryKeyNotFoundException extends RuntimeException {
  public PrimaryKeyNotFoundException(
      String message, String checkingStartDate, String checkingEndDate) {
    super(
        String.format(
            "%s: Checking Start Date: %s; Checking End Date: %s",
            message, checkingStartDate, checkingEndDate));
  }
}
