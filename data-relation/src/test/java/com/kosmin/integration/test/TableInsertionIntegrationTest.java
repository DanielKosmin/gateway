// package com.kosmin.integration.test;
//
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.http.MediaType;
// import org.springframework.mock.web.MockMultipartFile;
//
// public class TableInsertionIntegrationTest extends BaseIntegrationTest {
//
//  @Test
//  @DisplayName("Test inserting records into checking table")
//  public void postCheckingTableRow() {
//    MockMultipartFile multipartFile =
//        new MockMultipartFile(
//            "file",
//            "checking_records.csv",
//            String.valueOf(MediaType.MULTIPART_FORM_DATA),
//            """
//    Account Number,Transaction Description,Transaction Date,Transaction Type,Transaction
// Amount,Balance
//    0000,Deposit from payroll,10/10/24,Credit,500.00,1205.98
//    0000,Withdrawal from card,10/10/24,Debit,56.09,1358.08"""
//                .getBytes());
//    var res = dataRelationService.insertTableRecords(multipartFile, null);
//    Assertions.assertNotNull(res);
//    Assertions.assertNotNull(res.getBody());
//    Assertions.assertEquals(
//        res.getBody().getMessage(), "CSV File Successfully received and processing");
//  }
//
//  @Test
//  @DisplayName("Test inserting into credit table")
//  public void postCreditTableRow() {
//    MockMultipartFile multipartFile =
//        new MockMultipartFile(
//            "file",
//            "credit_records.csv",
//            String.valueOf(MediaType.MULTIPART_FORM_DATA),
//            """
//      Transaction Date,Post Date,Description,Category,Type,Amount
//      10/10/2024,10/11/2024,Eating Out,Food & Drink,Sale,-47.18"""
//                .getBytes());
//    var res = dataRelationService.insertTableRecords(multipartFile, null);
//    Assertions.assertNotNull(res);
//    Assertions.assertNotNull(res.getBody());
//    Assertions.assertEquals(
//        res.getBody().getMessage(), "CSV File Successfully received and processing");
//  }
// }
