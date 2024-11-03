package com.kosmin.unit.test;

import com.kosmin.repository.create.CreateTables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;

public class CreateTest extends BaseUnitTest {

  @Mock private JdbcTemplate jdbcTemplate;
  private CreateTables createTables;
  private String createCheckingTableQuery;
  private String createCreditTableQuery;

  @BeforeEach
  void setUp() {
    super.setUp();
    createCheckingTableQuery = tableInsertConfig.getCreateBankingTable();
    createCreditTableQuery = tableInsertConfig.getCreateCreditTable();
    createTables = new CreateTables(jdbcTemplate, tableInsertConfig);
  }

  @Test
  @DisplayName("test table creation")
  void testTableCreation() {
    createTables.createTables();
    Mockito.verify(jdbcTemplate, Mockito.times(1)).execute(createCheckingTableQuery);
    Mockito.verify(jdbcTemplate, Mockito.times(1)).execute(createCreditTableQuery);
  }
}
