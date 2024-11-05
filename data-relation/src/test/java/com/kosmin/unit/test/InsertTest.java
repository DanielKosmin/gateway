package com.kosmin.unit.test;

import com.kosmin.model.repository.CheckingModel;
import com.kosmin.model.repository.CreditModel;
import com.kosmin.repository.insert.InsertCheckingRecords;
import com.kosmin.repository.insert.InsertCreditRecords;
import com.kosmin.repository.query.QueryPrimaryKey;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class InsertTest extends BaseUnitTest {

  @Mock private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Mock private QueryPrimaryKey queryPrimaryKey;
  private InsertCheckingRecords insertCheckingRecords;
  private InsertCreditRecords insertCreditRecords;
  private String insertCheckingQuery;
  private String insertCreditQuery;

  @BeforeEach
  void setUp() {
    super.setUp();
    insertCheckingQuery = sqlQueriesConfig.getMap().get(INSERT_CHECKING_RECORDS);
    insertCreditQuery = sqlQueriesConfig.getMap().get(INSERT_INTO_CREDIT_TABLE);
    insertCheckingRecords = new InsertCheckingRecords(namedParameterJdbcTemplate, sqlQueriesConfig);
    insertCreditRecords =
        new InsertCreditRecords(namedParameterJdbcTemplate, sqlQueriesConfig, queryPrimaryKey);
  }

  @Test
  @DisplayName("insert records into checking table")
  void insertRecordsIntoCheckingTable() {
    CheckingModel bankingAccountModel =
        CheckingModel.builder()
            .transactionDescription("")
            .transactionDate(Date.valueOf("2024-10-10"))
            .transactionType("")
            .transactionAmount(BigDecimal.valueOf(0.0))
            .balance(BigDecimal.valueOf(0.0))
            .build();
    final Map<String, Object> params = new HashMap<>();
    params.put("transactionDescription", bankingAccountModel.getTransactionDescription());
    params.put("transactionDate", bankingAccountModel.getTransactionDate());
    params.put("transactionType", bankingAccountModel.getTransactionType());
    params.put("transactionAmount", bankingAccountModel.getTransactionAmount());
    params.put("balance", bankingAccountModel.getBalance());
    Mockito.when(namedParameterJdbcTemplate.update(insertCheckingQuery, params)).thenReturn(1);
    Assertions.assertDoesNotThrow(
        () -> insertCheckingRecords.insertCheckingRecords(bankingAccountModel));
    Mockito.verify(namedParameterJdbcTemplate, Mockito.times(1))
        .update(insertCheckingQuery, params);
  }

  @Test
  @DisplayName("insert records into credit table")
  void insertRecordsIntoCreditTable() {
    CreditModel creditCardRecordsModel =
        CreditModel.builder()
            .checkingRecordId(19)
            .transactionAmount(BigDecimal.valueOf(0.0))
            .transactionType("")
            .transactionCategory("")
            .transactionDescription("")
            .transactionDate(Date.valueOf("2024-10-10"))
            .build();
    final Map<String, Object> params = new HashMap<>();
    params.put("foreignKey", creditCardRecordsModel.getCheckingRecordId());
    params.put("transactionDate", creditCardRecordsModel.getTransactionDate());
    params.put("transactionDescription", creditCardRecordsModel.getTransactionDescription());
    params.put("transactionCategory", creditCardRecordsModel.getTransactionCategory());
    params.put("transactionType", creditCardRecordsModel.getTransactionType());
    params.put("transactionAmount", creditCardRecordsModel.getTransactionAmount());
    Mockito.when(queryPrimaryKey.queryPrimaryKey(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(19);
    Mockito.when(namedParameterJdbcTemplate.update(insertCreditQuery, params)).thenReturn(1);
    Assertions.assertDoesNotThrow(
        () -> insertCreditRecords.insertCreditRecords(creditCardRecordsModel));
    Mockito.verify(namedParameterJdbcTemplate, Mockito.times(1)).update(insertCreditQuery, params);
  }
}
