package com.kosmin.repository.query;

import com.kosmin.config.SqlQueriesConfig;
import com.kosmin.exception.PrimaryKeyNotFoundException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class QueryPrimaryKey {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final SqlQueriesConfig sqlQueriesConfig;

  public int queryPrimaryKey(String checkingStartDate, String checkingEndDate) {
    final Map<String, Object> params = new HashMap<>();
    params.put("checkingStartDate", Date.valueOf(checkingStartDate));
    params.put("checkingEndDate", Date.valueOf(checkingEndDate));
    final Optional<Integer> results =
        namedParameterJdbcTemplate
            .query(
                sqlQueriesConfig.getMap().get("query-primary-key"),
                params,
                (rs, rowNum) -> rs.getInt(1))
            .stream()
            .findFirst();
    return results.orElseThrow(
        () ->
            new PrimaryKeyNotFoundException(
                "Primary Key not found for payload combo", checkingStartDate, checkingEndDate));
  }
}
