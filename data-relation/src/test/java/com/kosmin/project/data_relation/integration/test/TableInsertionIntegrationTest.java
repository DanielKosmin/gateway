package com.kosmin.project.data_relation.integration.test;

import com.kosmin.model.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class TableInsertionIntegrationTest extends BaseIntegrationTest {

  @Test
  @DisplayName("Test inserting records into checking table")
  public void postCheckingTableRow() {
    final String csvContent =
        """
    Account Number,Transaction Description,Transaction Date,Transaction Type,Transaction Amount,Balance
    0000,Deposit from payroll,10/10/24,Credit,500.00,1205.98
    0000,Withdrawal from card,10/10/24,Debit,56.09,1358.08""";
    final var res = makePostRequest(simulateCsvFile(csvContent, "checking_records.csv"));
    Assertions.assertNotNull(res);
    Assertions.assertEquals(res.getMessage(), "CSV File Successfully received and processing");
  }

  @Test
  @DisplayName("Test inserting into credit table")
  public void postCreditTableRow() {
    final String csvContent =
        """
    Transaction Date,Post Date,Description,Category,Type,Amount
    10/10/2024,10/11/2024,Eating Out,Food & Drink,Sale,-47.18""";
    final var res = makePostRequest(simulateCsvFile(csvContent, "credit_records.csv"));
    Assertions.assertNotNull(res);
    Assertions.assertEquals(res.getMessage(), "CSV File Successfully received and processing");
  }

  private MultiValueMap<String, Object> simulateCsvFile(String csvContent, String fileName) {
    final ByteArrayResource resource =
        new ByteArrayResource(csvContent.getBytes()) {
          @Override
          public String getFilename() {
            return fileName;
          }
        };
    return new LinkedMultiValueMap<>() {
      {
        add("file", resource);
      }
    };
  }

  private Response makePostRequest(MultiValueMap<String, Object> csvFile) {
    return webTestClient
        .post()
        .uri(BaseIntegrationTest.COMMON_POST_URL)
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .bodyValue(csvFile)
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.ACCEPTED)
        .expectBody(Response.class)
        .returnResult()
        .getResponseBody();
  }
}
