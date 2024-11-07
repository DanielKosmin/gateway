package com.kosmin;

import com.kosmin.model.Request;
import com.kosmin.model.Response;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class SyncIntegrationTest extends BaseIntegrationTest {

  @Test
  @DisplayName("Synchronously insert table records")
  @Order(1)
  void insertRecords() {
    Response response =
        postRequest(
            Request.builder()
                .creditRecords(
                    List.of(
                        Request.CreditRecordPayload.builder()
                            .creditTransactionDescription("test description")
                            .creditTransactionDate("2024-10-05")
                            .creditTransactionType("sale")
                            .creditTransactionCategory("test")
                            .creditTransactionAmount(200.00)
                            .build(),
                        Request.CreditRecordPayload.builder()
                            .creditTransactionDescription("test description")
                            .creditTransactionDate("2024-10-05")
                            .creditTransactionType("sale")
                            .creditTransactionCategory("test")
                            .creditTransactionAmount(200.00)
                            .build()))
                .checkingTransactionDescription("withdrawal")
                .checkingTransactionDate("2024-11-06")
                .checkingTransactionType("credit")
                .checkingTransactionAmount(200.00)
                .checkingBalance(200.00)
                .build(),
            HttpStatus.CREATED);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(response.getMessage(), "Table Records inserted successfully");
    Assertions.assertEquals(response.getStatus(), "Success");
  }

  @Test
  @DisplayName("Synchronously insert table records invalid paylod")
  @Order(2)
  void insertRecordsInvalidPaylod() {
    Response response =
        postRequest(
            Request.builder()
                .creditRecords(
                    List.of(
                        Request.CreditRecordPayload.builder()
                            .creditTransactionDate("2024-10-05")
                            .creditTransactionType("sale")
                            .creditTransactionCategory("test")
                            .creditTransactionAmount(200.00)
                            .build(),
                        Request.CreditRecordPayload.builder()
                            .creditTransactionDate("2024-10-05")
                            .creditTransactionType("sale")
                            .creditTransactionCategory("test")
                            .creditTransactionAmount(200.00)
                            .build()))
                .checkingTransactionDescription("withdrawal")
                .checkingTransactionDate("2024-11-06")
                .checkingTransactionAmount(200.00)
                .checkingBalance(200.00)
                .build(),
            HttpStatus.BAD_REQUEST);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(response.getValidationErrors().size(), 3);
    Assertions.assertEquals(response.getStatus(), "Failed");
  }

  private Response postRequest(Object body, HttpStatus status) {
    return webTestClient
        .post()
        .uri(BaseIntegrationTest.POST_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body)
        .exchange()
        .expectStatus()
        .isEqualTo(status)
        .expectBody(Response.class)
        .returnResult()
        .getResponseBody();
  }
}
