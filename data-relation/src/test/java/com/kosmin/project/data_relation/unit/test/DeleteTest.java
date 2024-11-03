package com.kosmin.project.data_relation.unit.test;

import com.kosmin.repository.delete.DeleteTableRows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

@ExtendWith(MockitoExtension.class)
public class DeleteTest extends BaseUnitTest {

  @Mock private JdbcTemplate jdbcTemplate;
  private DeleteTableRows deleteTableRows;
  private String dropCreditTableQuery;
  private String clearCreditTableQuery;
  private String dropCheckingTableQuery;
  private String clearCheckingTableQuery;

  @BeforeEach
  void setUp() {
    super.setUp();
    dropCheckingTableQuery = sqlQueriesConfig.getMap().get(DROP_CHECKING_RECORDS);
    clearCheckingTableQuery = sqlQueriesConfig.getMap().get(CLEAR_CHECKING_RECORDS);
    dropCreditTableQuery = sqlQueriesConfig.getMap().get(DROP_CREDIT_RECORDS);
    clearCreditTableQuery = sqlQueriesConfig.getMap().get(CLEAR_CREDIT_RECORDS);
    deleteTableRows = new DeleteTableRows(jdbcTemplate, sqlQueriesConfig);
  }

  @Test
  @DisplayName("test clearing the credit table")
  void clearCreditTable() {
    deleteTableRows.clearTableRows(true, false, false);
    Mockito.verify(jdbcTemplate, Mockito.times(1)).execute(clearCreditTableQuery);
    Mockito.verify(jdbcTemplate, Mockito.times(0)).execute(clearCheckingTableQuery);
  }

  @Test
  @DisplayName("test clearing the checking table")
  void clearCheckingTable() {
    deleteTableRows.clearTableRows(false, true, false);
    Mockito.verify(jdbcTemplate, Mockito.times(0)).execute(clearCreditTableQuery);
    Mockito.verify(jdbcTemplate, Mockito.times(1)).execute(clearCheckingTableQuery);
  }

  @Test
  @DisplayName("test clearing the checking and credit table")
  void clearTables() {
    deleteTableRows.clearTableRows(true, true, false);
    Mockito.verify(jdbcTemplate, Mockito.times(1)).execute(clearCreditTableQuery);
    Mockito.verify(jdbcTemplate, Mockito.times(1)).execute(clearCheckingTableQuery);
  }

  @Test
  @DisplayName("test dropping the credit table")
  void dropCreditTable() {
    deleteTableRows.clearTableRows(true, false, true);
    Mockito.verify(jdbcTemplate, Mockito.times(1)).execute(dropCreditTableQuery);
    Mockito.verify(jdbcTemplate, Mockito.times(0)).execute(dropCheckingTableQuery);
  }

  @Test
  @DisplayName("test dropping the checking table")
  void dropCheckingTable() {
    deleteTableRows.clearTableRows(false, true, true);
    Mockito.verify(jdbcTemplate, Mockito.times(0)).execute(dropCreditTableQuery);
    Mockito.verify(jdbcTemplate, Mockito.times(1)).execute(dropCheckingTableQuery);
  }

  @Test
  @DisplayName("test dropping both tables")
  void dropTables() {
    deleteTableRows.clearTableRows(true, true, true);
    Mockito.verify(jdbcTemplate, Mockito.times(1)).execute(dropCreditTableQuery);
    Mockito.verify(jdbcTemplate, Mockito.times(1)).execute(dropCheckingTableQuery);
  }
}
