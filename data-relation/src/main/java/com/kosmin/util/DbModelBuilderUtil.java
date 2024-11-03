package com.kosmin.util;

import com.kosmin.model.CsvModel;
import com.kosmin.model.repository.CheckingModel;
import com.kosmin.model.repository.CreditModel;
import java.math.BigDecimal;
import java.util.Date;

public class DbModelBuilderUtil {

  public static CheckingModel buildCheckingDbModel(CsvModel csvModel, Date date) {
    return CheckingModel.builder()
        .transactionDescription(csvModel.getCheckingTransactionDescription())
        .transactionDate(date)
        .transactionType(csvModel.getCheckingTransactionType())
        .transactionAmount(BigDecimal.valueOf(csvModel.getCheckingTransactionAmount()))
        .balance(BigDecimal.valueOf(csvModel.getCheckingBalance()))
        .build();
  }

  public static CreditModel buildCreditDbModel(CsvModel csvModel, Date date) {
    return CreditModel.builder()
        .transactionDate(date)
        .transactionDescription(csvModel.getCreditTransactionDescription())
        .transactionCategory(csvModel.getCreditTransactionCategory())
        .transactionType(csvModel.getCreditTransactionType())
        .transactionAmount(BigDecimal.valueOf(csvModel.getCreditTransactionAmount()))
        .build();
  }
}
