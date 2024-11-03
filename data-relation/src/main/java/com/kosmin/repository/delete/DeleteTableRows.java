package com.kosmin.repository.delete;

import com.kosmin.config.SqlQueriesConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DeleteTableRows {

  private final JdbcTemplate jdbcTemplate;
  private final SqlQueriesConfig sqlQueriesConfig;

  public void clearTableRows(
      boolean clearCreditTable, boolean clearCheckingTable, boolean isDropTablesRequest) {
    if (clearCreditTable) {
      jdbcTemplate.execute(
          isDropTablesRequest
              ? sqlQueriesConfig.getMap().get("drop-credit-records")
              : sqlQueriesConfig.getMap().get("clear-credit-records"));
      log.info("Credit Table {} Successfully", (isDropTablesRequest ? "dropped" : "cleared"));
    }
    if (clearCheckingTable) {
      jdbcTemplate.execute(
          isDropTablesRequest
              ? sqlQueriesConfig.getMap().get("drop-checking-records")
              : sqlQueriesConfig.getMap().get("clear-checking-records"));
      log.info("Checking Table {} Successfully", (isDropTablesRequest ? "dropped" : "cleared"));
    }
  }
}
