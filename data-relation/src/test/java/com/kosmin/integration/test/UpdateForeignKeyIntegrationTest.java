package com.kosmin.integration.test;

import com.kosmin.exception.PrimaryKeyNotFoundException;
import com.kosmin.model.ForeignKeyMappingPayload;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UpdateForeignKeyIntegrationTest extends BaseIntegrationTest {

  @Test
  @DisplayName("test updating foreign key")
  public void testUpdateForeignKey() {
    var res =
        dataRelationService.updateTableRecords(
            ForeignKeyMappingPayload.builder()
                .checkingStartDate("2024-10-01")
                .checkingEndDate("2024-10-31")
                .checkingTransactionType("Debit")
                .checkingTransactionDescription("Credit Card Payment")
                .creditTransactionType("Sale")
                .build());
    Assertions.assertNotNull(res);
    Assertions.assertNotNull(res.getBody());
    Assertions.assertEquals(res.getBody().getMessage(), "Updated foreign key values for 1 records");
  }

  @Test
  @DisplayName("test updating foreign key, invalid payload")
  public void testUpdateForeignKeyInvalid() {
    var exc =
        Assertions.assertThrows(
            PrimaryKeyNotFoundException.class,
            () ->
                dataRelationService.updateTableRecords(
                    ForeignKeyMappingPayload.builder()
                        .checkingStartDate("2024-11-01")
                        .checkingEndDate("2024-11-30")
                        .checkingTransactionType("Debit")
                        .checkingTransactionDescription("Credit Card Payment")
                        .creditTransactionType("Sale")
                        .build()));
    Assertions.assertEquals(
        exc.getMessage(),
        "Primary Key not found for payload combo: Checking Transaction Type: Debit; Checking Transaction Description: Credit Card Payment; Checking Start Date: 2024-11-01; Checking End Date: 2024-11-30");
  }
}
