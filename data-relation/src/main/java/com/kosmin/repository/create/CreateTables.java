package com.kosmin.repository.create;

import com.kosmin.config.TableInsertConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CreateTables {

  private final JdbcTemplate jdbcTemplate;
  private final TableInsertConfig tableInsertConfig;

  public void createTables() {
    jdbcTemplate.execute(tableInsertConfig.getCreateBankingTable());
    jdbcTemplate.execute(tableInsertConfig.getCreateCreditTable());
  }
}
