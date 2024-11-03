package com.kosmin.repository.update;

import static com.kosmin.util.DataRelationUtil.getPreviousMonthRange;

import com.kosmin.config.SqlQueriesConfig;
import com.kosmin.model.ForeignKeyMappingPayload;
import com.kosmin.model.Response;
import com.kosmin.model.Status;
import com.kosmin.repository.query.QueryPrimaryKey;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UpdateForeignKeys {

  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final SqlQueriesConfig sqlQueriesConfig;
  private final QueryPrimaryKey queryPrimaryKey;

  public Response updateForeignKeys(ForeignKeyMappingPayload payload) {
    final Map<String, Object> params = new HashMap<>();
    String[] creditDates =
        getPreviousMonthRange(payload.getCheckingStartDate(), payload.getCheckingEndDate());
    params.put(
        "primaryKey",
        queryPrimaryKey.queryPrimaryKey(
            payload.getCheckingTransactionType(),
            payload.getCheckingStartDate(),
            payload.getCheckingEndDate(),
            payload.getCheckingTransactionDescription()));
    params.put("creditTransactionType", payload.getCreditTransactionType());
    params.put("creditStartDate", Date.valueOf(creditDates[0]));
    params.put("creditEndDate", Date.valueOf(creditDates[1]));
    int numUpdatedRecords =
        jdbcTemplate.update(sqlQueriesConfig.getMap().get("update-foreign-key"), params);
    return Response.builder()
        .status(Status.SUCCESS.getValue())
        .message(String.format("Updated foreign key values for %s records", numUpdatedRecords))
        .build();
  }
}
