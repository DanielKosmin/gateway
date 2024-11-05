package com.kosmin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kosmin.validator.RequestValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequestValidator
public class Request {
  @NotEmpty(message = "creditRecords must not be empty")
  List<CreditRecordPayload> creditRecords;

  @NotBlank(message = "checkingTransactionDescription must not be empty")
  private String checkingTransactionDescription;

  private String checkingTransactionDate;

  @NotBlank(message = "checkingTransactionType must not be empty")
  @Pattern(
      regexp = "^(?i)(credit|debit)$",
      message = "checkingTransactionType must be either 'credit' or 'debit'")
  private String checkingTransactionType;

  @NotNull(message = "checkingTransactionAmount must not be null")
  private Double checkingTransactionAmount;

  @NotNull(message = "checkingBalance must not be null")
  private Double checkingBalance;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder(toBuilder = true)
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class CreditRecordPayload {
    @NotBlank(message = "creditTransactionDescription must not be empty")
    private String creditTransactionDescription;

    private String creditTransactionDate;

    @NotBlank(message = "creditTransactionType must not be empty")
    @Pattern(
        regexp = "^(?i)(sale|payment)$",
        message = "creditTransactionType must be either 'sale' or 'payment'")
    private String creditTransactionType;

    @NotBlank(message = "creditTransactionCategory must not be empty")
    private String creditTransactionCategory;

    @NotNull(message = "creditTransactionAmount must not be null")
    private Double creditTransactionAmount;
  }
}
