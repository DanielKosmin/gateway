package com.kosmin.exception;

import com.kosmin.model.repository.CheckingModel;

public class CheckingInsertionException extends RuntimeException {
  public CheckingInsertionException(String message, CheckingModel checkingModel) {
    super(String.format("%s: %s", message, checkingModel));
  }
}
