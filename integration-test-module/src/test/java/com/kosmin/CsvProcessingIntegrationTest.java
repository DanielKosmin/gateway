package com.kosmin;

import com.kosmin.exception.AsyncProcessingException;
import com.kosmin.model.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class CsvProcessingIntegrationTest extends BaseIntegrationTest {
  @Test
  @DisplayName("Test inserting records into checking table")
  @Order(1)
  void postCheckingTableRow() {
    String csvContent =
        """
    Account Number,Transaction Description,Transaction Date,Transaction Type,Transaction Amount,Balance
    0000,Deposit from Payroll,10/10/24,Credit,2938.91,4145.19
    0000,Money received,10/09/24,Credit,47,1206.28
    """;
    Response response = postRequest(simulateCsvFile(csvContent, "checking_records.csv"));
    Assertions.assertNotNull(response);
    Assertions.assertEquals(response.getMessage(), "CSV File Successfully received and processing");
  }

  @Test
  @DisplayName("Test inserting into credit table")
  @Order(2)
  void postCreditTableRow() {
    String csvContent =
        """
    Transaction Date,Post Date,Description,Category,Type,Amount
    09/10/2024,10/11/2024,APPLE.COM/BILL,Shopping,Sale,-0.99
    """;
    Response response = postRequest(simulateCsvFile(csvContent, "credit_records.csv"));
    Assertions.assertNotNull(response);
    Assertions.assertEquals(response.getMessage(), "CSV File Successfully received and processing");
  }

  @Test
  @DisplayName("Test inserting into credit table with no parent key found")
  @Order(3)
  void postCreditTableRowInvalid() {
    MockMultipartFile multipartFile =
        new MockMultipartFile(
            "file",
            "credit_records.csv",
            String.valueOf(MediaType.MULTIPART_FORM_DATA),
            """
        Transaction Date,Post Date,Description,Category,Type,Amount
        09/10/2025,10/11/2025,APPLE.COM/BILL,Shopping,Sale,-0.99"""
                .getBytes());
    CompletableFuture<Void> future = asyncCsvProcessingService.handleCsvProcessing(multipartFile);
    try {
      future.join();
      Assertions.fail("Exception should be thrown");
    } catch (CompletionException e) {
      Throwable cause = e.getCause();
      Assertions.assertInstanceOf(AsyncProcessingException.class, cause);
      Assertions.assertTrue(
          e.getMessage()
              .contains(
                  "Primary Key not found for payload combo: Checking Start Date: 2025-10-01; Checking End Date: 2025-10-31"));
    }
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

  private Response postRequest(Object body) {
    return webTestClient
        .post()
        .uri(BaseIntegrationTest.POST_URL)
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .bodyValue(body)
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.ACCEPTED)
        .expectBody(Response.class)
        .returnResult()
        .getResponseBody();
  }
}
