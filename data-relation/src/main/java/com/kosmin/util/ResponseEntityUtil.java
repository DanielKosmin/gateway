package com.kosmin.util;

import com.kosmin.model.Request;
import com.kosmin.model.Response;
import com.kosmin.model.Status;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityUtil {

  public static ResponseEntity<Response> createdResponse(String message) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(Response.builder().status(Status.SUCCESS.getValue()).message(message).build());
  }

  public static ResponseEntity<Response> acceptedResponse(String message) {
    return ResponseEntity.accepted()
        .body(Response.builder().status(Status.SUCCESS.getValue()).message(message).build());
  }

  public static ResponseEntity<Response> badRequestResponse(String errorMessage) {
    return ResponseEntity.badRequest()
        .body(
            Response.builder().status(Status.FAILED.getValue()).errorMessage(errorMessage).build());
  }

  public static ResponseEntity<Response> internalServerErrorResponse(String errorMessage) {
    return ResponseEntity.internalServerError()
        .body(
            Response.builder().status(Status.FAILED.getValue()).errorMessage(errorMessage).build());
  }

  public static ResponseEntity<Response> partiallyCompletedResponse(
      String message, List<Request.CreditRecordPayload> creditRecords) {
    return ResponseEntity.status(HttpStatus.MULTI_STATUS)
        .body(
            Response.builder()
                .status(Status.PARTIALLY_COMPLETED.getValue())
                .message(message)
                .failedCreditInsertions(creditRecords)
                .build());
  }
}
