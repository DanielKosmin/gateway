package com.kosmin.unit.test;

import com.kosmin.exception.PrimaryKeyNotFoundException;
import com.kosmin.repository.query.QueryPrimaryKey;
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
  @DisplayName("test querying valid parent request id")
  void testValidParentRequestId() {
    String startDate = "2024-11-01";
    String endDate = "2024-11-30";
    Map<String, Object> params =
        Map.of(
            "checkingStartDate",
            java.sql.Date.valueOf(startDate),
            "checkingEndDate",
            java.sql.Date.valueOf(endDate));
    Mockito.doReturn(List.of(1))
        .when(namedParameterJdbcTemplate)
        .query(Mockito.eq(query), Mockito.eq(params), Mockito.<RowMapper<Integer>>any());
    var res = queryPrimaryKey.queryPrimaryKey(startDate, endDate);
    Assertions.assertEquals(res, 1);
  }

  @Test
  @DisplayName("test querying invalid parent request id")
  void testInvalidParentRequestId() {
    String startDate = "2024-11-01";
    String endDate = "2024-11-30";
    Map<String, Object> params =
        Map.of(
            "checkingStartDate",
            java.sql.Date.valueOf(startDate),
            "checkingEndDate",
            java.sql.Date.valueOf(endDate));
    Mockito.doReturn(Collections.EMPTY_LIST)
        .when(namedParameterJdbcTemplate)
        .query(Mockito.eq(query), Mockito.eq(params), Mockito.<RowMapper<Integer>>any());
    Assertions.assertThrows(
        PrimaryKeyNotFoundException.class,
        () -> queryPrimaryKey.queryPrimaryKey(startDate, endDate));
  }
}
