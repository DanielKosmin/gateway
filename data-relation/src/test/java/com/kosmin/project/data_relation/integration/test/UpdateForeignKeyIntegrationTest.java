package com.kosmin.project.data_relation.integration.test;

import com.kosmin.model.ForeignKeyMappingPayload;
import com.kosmin.model.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class UpdateForeignKeyIntegrationTest extends BaseIntegrationTest {

  @Test
  @DisplayName("test updating foreign key")
  public void testUpdateForeignKey() {
    Response response =
        makePutRequest(
            ForeignKeyMappingPayload.builder()
                .checkingStartDate("2024-10-01")
                .checkingEndDate("2024-10-31")
                .checkingTransactionType("Debit")
                .checkingTransactionDescription("Credit Card Payment")
                .creditTransactionType("Sale")
                .build(),
            HttpStatus.CREATED);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(response.getMessage(), "Updated foreign key values for 1 records");
  }

  @Test
  @DisplayName("test updating foreign key, invalid payload")
  public void testUpdateForeignKeyInvalid() {
    Response response =
        makePutRequest(
            ForeignKeyMappingPayload.builder()
                .checkingStartDate("2024-11-01")
                .checkingEndDate("2024-11-30")
                .checkingTransactionType("Debit")
                .checkingTransactionDescription("Credit Card Payment")
                .creditTransactionType("Sale")
                .build(),
            HttpStatus.BAD_REQUEST);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(
        response.getErrorMessage(),
        "Primary Key not found for payload combo: Checking Transaction Type: Debit; Checking Transaction Description: Credit Card Payment; Checking Start Date: 2024-11-01; Checking End Date: 2024-11-30");
  }

  private Response makePutRequest(ForeignKeyMappingPayload payload, HttpStatus status) {
    return webTestClient
        .put()
        .uri(BaseIntegrationTest.COMMON_PUT_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(payload)
        .exchange()
        .expectStatus()
        .isEqualTo(status)
        .expectBody(Response.class)
        .returnResult()
        .getResponseBody();
  }
}
