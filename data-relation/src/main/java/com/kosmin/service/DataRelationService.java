package com.kosmin.service;

import static com.kosmin.util.DataRelationUtil.isValidCsvFile;
import static com.kosmin.util.ResponseEntityUtil.acceptedResponse;
import static com.kosmin.util.ResponseEntityUtil.badRequestResponse;
import static com.kosmin.util.ResponseEntityUtil.createdResponse;

import com.kosmin.exception.InvalidQueryParamException;
import com.kosmin.model.Request;
import com.kosmin.model.Response;
import com.kosmin.repository.create.CreateTables;
import com.kosmin.repository.delete.DeleteTableRows;
import com.kosmin.service.async.service.AsyncCsvProcessingService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataRelationService {

  private final AsyncCsvProcessingService asyncCsvProcessingService;
  private final CreateTables createTables;
  private final DeleteTableRows deleteTableRows;

  public ResponseEntity<Response> createTables() {
    createTables.createTables();
    return createdResponse("Tables(s) Created Successfully");
  }

  public ResponseEntity<Response> insertTableRecords(MultipartFile file, Request request) {
    if (file != null && isValidCsvFile(file)) {
      asyncCsvProcessingService
          .handleCsvProcessing(file)
          .exceptionally(
              e -> {
                log.error(e.getCause().getMessage());
                return null;
              });
      return acceptedResponse("CSV File Successfully received and processing");
    } else if (request != null) {
      return createdResponse("Table Records inserted successfully");
    } else {
      return badRequestResponse(
          "Input must be a non empty csv file with filename including either "
              + "'credit' or 'checking' to indicate which table to insert");
    }
  }

  public ResponseEntity<Response> deleteTableRecords(
      Boolean credit, Boolean checking, Boolean dropTables) {
    boolean clearCreditTable = Optional.ofNullable(credit).orElse(false);
    boolean clearCheckingTable = Optional.ofNullable(checking).orElse(false);
    boolean isDropTablesRequest = Optional.ofNullable(dropTables).orElse(false);
    if (clearCreditTable || clearCheckingTable) {
      deleteTableRows.clearTableRows(clearCreditTable, clearCheckingTable, isDropTablesRequest);
      return ResponseEntity.noContent().build();
    } else {
      throw new InvalidQueryParamException("Invalid Query Param Combo", credit, checking);
    }
  }
}
