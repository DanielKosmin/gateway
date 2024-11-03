package com.kosmin.service.database.operations;

import com.kosmin.model.ForeignKeyMappingPayload;
import com.kosmin.model.Response;
import com.kosmin.model.repository.CheckingModel;
import com.kosmin.model.repository.CreditModel;

public interface DbOperationsService {

  void createTables();

  void insertCheckingInformation(CheckingModel checkingModel);

  void insertCreditInformation(CreditModel creditModel);

  void clearTablesRecords(
      boolean clearCreditTable, boolean clearCheckingTable, boolean isDropTablesRequest);

  Response updateForeignKeys(ForeignKeyMappingPayload payload);
}
