package com.kosmin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class IntegrationTest extends BaseIntegrationTest {
  @Test
  @DisplayName("Test inserting records into checking table")
  @Order(1)
  public void postCheckingTableRow() {
    MockMultipartFile multipartFile =
        new MockMultipartFile(
            "file",
            "checking_records.csv",
            String.valueOf(MediaType.MULTIPART_FORM_DATA),
            """
            Account Number,Transaction Description,Transaction Date,Transaction Type,Transaction Amount,Balance
            0000,Deposit from Payroll,10/10/24,Credit,2938.91,4145.19
            0000,Money received,10/09/24,Credit,47,1206.28"""
                .getBytes());
    var res = dataRelationService.insertTableRecords(multipartFile, null);
    Assertions.assertNotNull(res);
    Assertions.assertNotNull(res.getBody());
    Assertions.assertEquals(
        res.getBody().getMessage(), "CSV File Successfully received and processing");
  }

  @Test
  @DisplayName("Test inserting into credit table")
  @Order(2)
  public void postCreditTableRow() {
    MockMultipartFile multipartFile =
        new MockMultipartFile(
            "file",
            "credit_records.csv",
            String.valueOf(MediaType.MULTIPART_FORM_DATA),
            """
            Transaction Date,Post Date,Description,Category,Type,Amount
            09/10/2024,10/11/2024,APPLE.COM/BILL,Shopping,Sale,-0.99"""
                .getBytes());
    var res = dataRelationService.insertTableRecords(multipartFile, null);
    Assertions.assertNotNull(res);
    Assertions.assertNotNull(res.getBody());
    Assertions.assertEquals(
        res.getBody().getMessage(), "CSV File Successfully received and processing");
  }
}
