package com.kosmin.unit.test;

import com.kosmin.model.ForeignKeyMappingPayload;
import jakarta.validation.ConstraintViolation;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PayloadTest extends BaseUnitTest {

  @Test
  @DisplayName("test no payload violation")
  public void test1() {
    ForeignKeyMappingPayload payload =
        ForeignKeyMappingPayload.builder()
            .checkingStartDate("2024-11-01")
            .checkingEndDate("2024-11-30")
            .checkingTransactionType("Debit")
            .checkingTransactionDescription("Credit Card Payment")
            .creditTransactionType("Sale")
            .build();

    Set<ConstraintViolation<ForeignKeyMappingPayload>> violations = validator.validate(payload);
    Assertions.assertTrue(violations.isEmpty(), "Payload should be valid");
  }

  @Test
  @DisplayName("test invalid dates format")
  public void test2() {
    ForeignKeyMappingPayload payload =
        ForeignKeyMappingPayload.builder()
            .checkingStartDate("2024-11-0")
            .checkingEndDate("2024-11-3")
            .checkingTransactionType("Debit")
            .checkingTransactionDescription("Credit Card Payment")
            .creditTransactionType("Sale")
            .build();

    Set<ConstraintViolation<ForeignKeyMappingPayload>> violations = validator.validate(payload);
    Assertions.assertEquals(1, violations.size(), "Invalid Parse Exception is thrown");
  }

  @Test
  @DisplayName("test empty payload")
  public void test3() {
    ForeignKeyMappingPayload payload = ForeignKeyMappingPayload.builder().build();

    Set<ConstraintViolation<ForeignKeyMappingPayload>> violations = validator.validate(payload);
    Assertions.assertEquals(5, violations.size());
  }

  @Test
  @DisplayName("test end date less than start")
  public void test4() {
    ForeignKeyMappingPayload payload =
        ForeignKeyMappingPayload.builder()
            .checkingStartDate("2024-11-30")
            .checkingEndDate("2024-11-29")
            .checkingTransactionType("Debit")
            .checkingTransactionDescription("Credit Card Payment")
            .creditTransactionType("Sale")
            .build();

    Set<ConstraintViolation<ForeignKeyMappingPayload>> violations = validator.validate(payload);
    Assertions.assertEquals(1, violations.size(), "Invalid Parse Exception is thrown");
  }

  @Test
  @DisplayName("test end date equal to start")
  public void test5() {
    ForeignKeyMappingPayload payload =
        ForeignKeyMappingPayload.builder()
            .checkingStartDate("2024-11-30")
            .checkingEndDate("2024-11-30")
            .checkingTransactionType("Debit")
            .checkingTransactionDescription("Credit Card Payment")
            .creditTransactionType("Sale")
            .build();

    Set<ConstraintViolation<ForeignKeyMappingPayload>> violations = validator.validate(payload);
    Assertions.assertEquals(1, violations.size(), "Invalid Parse Exception is thrown");
  }
}
