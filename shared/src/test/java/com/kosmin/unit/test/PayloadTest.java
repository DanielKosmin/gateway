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
}
