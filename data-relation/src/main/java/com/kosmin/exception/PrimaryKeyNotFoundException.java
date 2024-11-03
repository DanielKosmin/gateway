package com.kosmin.exception;

public class PrimaryKeyNotFoundException extends RuntimeException {
  public PrimaryKeyNotFoundException(
      String message,
      String checkingTransactionType,
      String checkingStartDate,
      String checkingEndDate,
      String checkingTransactionDescription) {
    super(
        String.format(
            "%s: Checking Transaction Type: %s; Checking Transaction Description: %s; Checking Start Date: %s; Checking End Date: %s",
            message,
            checkingTransactionType,
            checkingTransactionDescription,
            checkingStartDate,
            checkingEndDate));
  }
}
