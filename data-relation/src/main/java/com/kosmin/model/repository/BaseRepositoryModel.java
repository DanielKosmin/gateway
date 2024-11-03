package com.kosmin.model.repository;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BaseRepositoryModel {

  private String transactionDescription;
  private Date transactionDate;
  private String transactionType;
  private BigDecimal transactionAmount;
}
