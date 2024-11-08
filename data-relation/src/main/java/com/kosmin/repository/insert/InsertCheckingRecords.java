package com.kosmin.repository.insert;

import com.kosmin.config.SqlQueriesConfig;
import com.kosmin.exception.CheckingInsertionException;
import com.kosmin.model.repository.CheckingModel;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class InsertCheckingRecords {

  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final SqlQueriesConfig sqlQueriesConfig;

  public void insertCheckingRecords(CheckingModel checkingModel, boolean synchronous) {
    final int insertionResponse =
        jdbcTemplate.update(
            sqlQueriesConfig.getMap().get("insert-checking-records"), getParams(checkingModel));
    if (insertionResponse != 1) {
      log.error("Insert banking Records Failed for {}", checkingModel);
      if (synchronous)
        throw new CheckingInsertionException("Checking Record Insertion Failed", checkingModel);
    }
  }

  private Map<String, Object> getParams(CheckingModel checkingModel) {
    final Map<String, Object> params = new HashMap<>();
    params.put("transactionDescription", checkingModel.getTransactionDescription());
    params.put("transactionDate", checkingModel.getTransactionDate());
    params.put("transactionType", checkingModel.getTransactionType());
    params.put("transactionAmount", checkingModel.getTransactionAmount());
    params.put("balance", checkingModel.getBalance());
    return params;
  }
}
