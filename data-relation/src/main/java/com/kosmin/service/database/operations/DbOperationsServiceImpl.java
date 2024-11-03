package com.kosmin.service.database.operations;

import com.kosmin.model.ForeignKeyMappingPayload;
import com.kosmin.model.Response;
import com.kosmin.model.repository.CheckingModel;
import com.kosmin.model.repository.CreditModel;
import com.kosmin.repository.create.CreateTables;
import com.kosmin.repository.delete.DeleteTableRows;
import com.kosmin.repository.insert.InsertCheckingRecords;
import com.kosmin.repository.insert.InsertCreditRecords;
import com.kosmin.repository.update.UpdateForeignKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DbOperationsServiceImpl implements DbOperationsService {

  private final CreateTables createTables;
  private final InsertCheckingRecords insertCheckingRecords;
  private final InsertCreditRecords insertCreditRecords;
  private final DeleteTableRows deleteTableRows;
  private final UpdateForeignKeys updateForeignKeys;

  @Override
  public void createTables() {
    createTables.createTables();
  }

  @Override
  public void insertCheckingInformation(CheckingModel checkingModel) {
    insertCheckingRecords.insertCheckingRecords(checkingModel);
  }

  @Override
  public void insertCreditInformation(CreditModel creditModel) {
    insertCreditRecords.insertCreditRecords(creditModel);
  }

  @Override
  public void clearTablesRecords(
      boolean clearCreditTable, boolean clearCheckingTable, boolean isDropTablesRequest) {
    deleteTableRows.clearTableRows(clearCreditTable, clearCheckingTable, isDropTablesRequest);
  }

  @Override
  public Response updateForeignKeys(ForeignKeyMappingPayload payload) {
    return updateForeignKeys.updateForeignKeys(payload);
  }
}
