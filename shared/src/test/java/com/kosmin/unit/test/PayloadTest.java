package com.kosmin.unit.test;

import com.kosmin.model.Request;
import jakarta.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PayloadTest extends BaseUnitTest {

  @Test
  @DisplayName("test no payload violation")
  public void test1() {
    Request payload =
        Request.builder()
            .creditRecords(
                List.of(
                    Request.CreditRecordPayload.builder()
                        .creditTransactionDescription("test description")
                        .creditTransactionDate("2024-10-05")
                        .creditTransactionType("test")
                        .creditTransactionCategory("test")
                        .creditTransactionAmount(200.0)
                        .build(),
                    Request.CreditRecordPayload.builder()
                        .creditTransactionDescription("test description")
                        .creditTransactionDate("2024-10-05")
                        .creditTransactionType("test")
                        .creditTransactionCategory("test")
                        .creditTransactionAmount(200.0)
                        .build()))
            .checkingTransactionType("credit")
            .checkingTransactionDescription("test description")
            .checkingTransactionDate("2024-11-05")
            .checkingTransactionAmount(200.0)
            .checkingBalance(200.0)
            .build();

    Set<ConstraintViolation<Request>> violations = validator.validate(payload);
    Assertions.assertTrue(violations.isEmpty(), "Payload should be valid");
  }

  @Test
  @DisplayName("test missing credit records")
  public void test2() {
    Request payload =
        Request.builder()
            .checkingTransactionType("credit")
            .checkingTransactionDescription("test description")
            .checkingTransactionDate("2024-11-05")
            .checkingTransactionAmount(200.0)
            .checkingBalance(200.0)
            .build();

    Set<ConstraintViolation<Request>> violations = validator.validate(payload);
    Assertions.assertEquals(2, violations.size());
  }

  @Test
  @DisplayName("test empty json payload")
  public void test3() {
    Set<ConstraintViolation<Request>> violations = validator.validate(new Request());
    Assertions.assertEquals(7, violations.size());
  }

  @Test
  @DisplayName("test missing fields from credit records")
  public void test4() {
    Request payload =
        Request.builder()
            .creditRecords(
                List.of(
                    Request.CreditRecordPayload.builder()
                        .creditTransactionType("test")
                        .creditTransactionCategory("test")
                        .creditTransactionAmount(200.0)
                        .build(),
                    Request.CreditRecordPayload.builder()
                        .creditTransactionDescription("test description")
                        .creditTransactionDate("2024-10-05")
                        .build()))
            .checkingTransactionType("credit")
            .checkingTransactionDescription("test description")
            .checkingTransactionDate("2024-11-05")
            .checkingTransactionAmount(200.0)
            .checkingBalance(200.0)
            .build();

    Set<ConstraintViolation<Request>> violations = validator.validate(payload);
    Assertions.assertEquals(5, violations.size());
  }

  @Test
  @DisplayName("test missing fields from checking and credit records")
  public void test5() {
    Request payload =
        Request.builder()
            .creditRecords(
                List.of(
                    Request.CreditRecordPayload.builder()
                        .creditTransactionType("test")
                        .creditTransactionCategory("test")
                        .creditTransactionAmount(200.0)
                        .build(),
                    Request.CreditRecordPayload.builder()
                        .creditTransactionDescription("test description")
                        .creditTransactionDate("2024-10-05")
                        .build()))
            .checkingTransactionType("credit")
            .checkingTransactionDescription("test description")
            .checkingTransactionAmount(200.0)
            .checkingBalance(200.0)
            .build();

    Set<ConstraintViolation<Request>> violations = validator.validate(payload);
    Assertions.assertEquals(
        1,
        violations.size(),
        "Checking payload violations for missing date will fail first regardless of if there are credit errors");
  }

  @Test
  @DisplayName("test missing fields from checking and credit records")
  public void test6() {
    Request payload =
        Request.builder()
            .creditRecords(
                List.of(
                    Request.CreditRecordPayload.builder()
                        .creditTransactionType("test")
                        .creditTransactionCategory("test")
                        .creditTransactionAmount(200.0)
                        .build(),
                    Request.CreditRecordPayload.builder()
                        .creditTransactionDescription("test description")
                        .creditTransactionDate("2024-10-05")
                        .build()))
            .checkingTransactionDate("2024-11-05")
            .build();

    Set<ConstraintViolation<Request>> violations = validator.validate(payload);
    Assertions.assertEquals(
        9,
        violations.size(),
        "Since checking transaction date is present, remaining validation checks will run");
  }
}
