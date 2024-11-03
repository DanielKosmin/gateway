package com.kosmin.unit.test;

import com.kosmin.exception.PrimaryKeyNotFoundException;
import com.kosmin.repository.query.QueryPrimaryKey;
import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class QueryTest extends BaseUnitTest {

  @Mock private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private QueryPrimaryKey queryPrimaryKey;
  private String query;

  @BeforeEach
  void setUp() {
    super.setUp();
    query = sqlQueriesConfig.getMap().get("query-primary-key");
    queryPrimaryKey = new QueryPrimaryKey(namedParameterJdbcTemplate, sqlQueriesConfig);
  }

  @Test
  @DisplayName("test querying parent id and throwing exception when result is not found")
  void testQueryThrows() {
    Map<String, Object> params =
        Map.of(
            "checkingTransactionDescription",
            "%transactionDescription%",
            "checkingTransactionType",
            "test",
            "checkingEndDate",
            Date.valueOf("2024-01-01"),
            "checkingStartDate",
            Date.valueOf("2024-12-31"));
    Mockito.doReturn(Collections.emptyList())
        .when(namedParameterJdbcTemplate)
        .query(Mockito.eq(query), Mockito.eq(params), Mockito.<RowMapper<Integer>>any());
    Assertions.assertThrows(
        PrimaryKeyNotFoundException.class,
        () ->
            queryPrimaryKey.queryPrimaryKey(
                "test", "2024-12-31", "2024-01-01", "transactionDescription"));
  }

  @Test
  @DisplayName("test querying parent id")
  void testQuery() {
    Map<String, Object> params =
        Map.of(
            "checkingTransactionDescription",
            "%transactionDescription%",
            "checkingTransactionType",
            "test",
            "checkingEndDate",
            Date.valueOf("2024-01-01"),
            "checkingStartDate",
            Date.valueOf("2024-12-31"));
    Mockito.doReturn(List.of(1))
        .when(namedParameterJdbcTemplate)
        .query(Mockito.eq(query), Mockito.eq(params), Mockito.<RowMapper<Integer>>any());
    Assertions.assertEquals(
        queryPrimaryKey.queryPrimaryKey(
            "test", "2024-12-31", "2024-01-01", "transactionDescription"),
        1);
  }
}
