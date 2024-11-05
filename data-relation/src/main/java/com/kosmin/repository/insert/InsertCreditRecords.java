package com.kosmin.repository.insert;

import com.kosmin.config.SqlQueriesConfig;
import com.kosmin.model.repository.CreditModel;
import com.kosmin.repository.query.QueryPrimaryKey;
import com.kosmin.util.DataRelationUtil;
import java.util.Date;
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
  private final QueryPrimaryKey queryPrimaryKey;

  public void insertCreditRecords(CreditModel creditModel) {
    final int insertionResponse =
        jdbcTemplate.update(
            sqlQueriesConfig.getMap().get("insert-credit-records"), getQueryMappings(creditModel));
    if (insertionResponse != 1) log.error("Insert credit Records Failed for {}", creditModel);
  }

  private Map<String, Object> getQueryMappings(CreditModel creditModel) {
    final Map<String, Object> params = new HashMap<>();
    params.put("foreignKey", queryForeignKey(creditModel.getTransactionDate()));
    params.put("transactionDate", creditModel.getTransactionDate());
    params.put("transactionDescription", creditModel.getTransactionDescription());
    params.put("transactionCategory", creditModel.getTransactionCategory());
    params.put("transactionType", creditModel.getTransactionType());
    params.put("transactionAmount", creditModel.getTransactionAmount());
    return params;
  }

  private int queryForeignKey(Date date) {
    String[] dateRange = DataRelationUtil.getNextMonthRange(date);
    return queryPrimaryKey.queryPrimaryKey(dateRange[0], dateRange[1]);
  }
}
