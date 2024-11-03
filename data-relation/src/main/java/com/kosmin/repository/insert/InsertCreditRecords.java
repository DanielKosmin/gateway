package com.kosmin.repository.insert;

import com.kosmin.config.SqlQueriesConfig;
import com.kosmin.model.repository.CreditModel;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class InsertCreditRecords {

  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final SqlQueriesConfig sqlQueriesConfig;

  public void insertCreditRecords(CreditModel creditModel) {
    final Map<String, Object> params = new HashMap<>();
    params.put("transactionDate", creditModel.getTransactionDate());
    params.put("transactionDescription", creditModel.getTransactionDescription());
    params.put("transactionCategory", creditModel.getTransactionCategory());
    params.put("transactionType", creditModel.getTransactionType());
    params.put("transactionAmount", creditModel.getTransactionAmount());
    final int insertionResponse =
        jdbcTemplate.update(sqlQueriesConfig.getMap().get("insert-credit-records"), params);
    if (insertionResponse != 1) log.error("Insert credit Records Failed for {}", creditModel);
  }
}
