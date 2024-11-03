package com.kosmin.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CsvModel {

  // common
  @CsvBindByName(column = "Transaction Date")
  private String transactionDate;

  // Checking Models
  @CsvBindByName(column = "Transaction Description")
  private String checkingTransactionDescription;

  @CsvBindByName(column = "Transaction Type")
  private String checkingTransactionType;

  @CsvBindByName(column = "Transaction Amount")
  private Double checkingTransactionAmount;

  @CsvBindByName(column = "Balance")
  private Double checkingBalance;

  // Credit Card Models
  @CsvBindByName(column = "Description")
  private String creditTransactionDescription;

  @CsvBindByName(column = "Category")
  private String creditTransactionCategory;

  @CsvBindByName(column = "Type")
  private String creditTransactionType;

  @CsvBindByName(column = "Amount")
  private Double creditTransactionAmount;
}
