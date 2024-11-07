package com.kosmin.model;

import lombok.Getter;

@Getter
public enum Status {
  SUCCESS("Success"),
  FAILED("Failed"),
  PARTIALLY_COMPLETED("Partially Completed"),
  ;

  private final String value;

  Status(String value) {
    this.value = value;
  }
}
