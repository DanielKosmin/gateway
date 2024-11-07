package com.kosmin.util;

import com.kosmin.model.Type;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public class DataRelationUtil {

  public static boolean isValidCsvFile(MultipartFile file) {
    return Optional.ofNullable(file)
        .filter(f -> !f.isEmpty())
        .map(MultipartFile::getOriginalFilename)
        .filter(
            fileName -> {
              final boolean isFileCsv = fileName.endsWith(".csv");
              final boolean filenameContainsCredit =
                  fileName.toLowerCase().contains(Type.CREDIT.getValue().toLowerCase());
              final boolean filenameContainsChecking =
                  fileName.toLowerCase().contains(Type.CHECKING.getValue().toLowerCase());
              return isFileCsv && (filenameContainsCredit || filenameContainsChecking);
            })
        .isPresent();
  }

  public static Type fileType(MultipartFile file) {
    return Optional.ofNullable(file)
        .map(MultipartFile::getOriginalFilename)
        .flatMap(
            fileName ->
                Arrays.stream(Type.values())
                    .filter(type -> fileName.toLowerCase().contains(type.getValue().toLowerCase()))
                    .findFirst())
        .orElse(null);
  }

  public static Date parseTransactionDate(String dateString) {
    final String[] formats = {"MM/dd/yy", "MM/dd/yyyy", "yyyy-MM-dd"};
    for (String format : formats) {
      try {
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        final java.util.Date parsedDate = sdf.parse(dateString);
        return new Date(parsedDate.getTime());
      } catch (ParseException e) {
        // Continue trying other formats
      }
    }
    return null;
  }

  public static String[] getNextMonthRange(java.util.Date transactionDate) {
    // Convert java.util.Date to LocalDate using java.sql.Date
    LocalDate localDate = new java.sql.Date(transactionDate.getTime()).toLocalDate();

    // Calculate the first day of the next month
    LocalDate firstDayOfNextMonth = localDate.plusMonths(1).withDayOfMonth(1);

    // Calculate the last day of the next month
    YearMonth yearMonth = YearMonth.from(firstDayOfNextMonth);
    LocalDate lastDayOfNextMonth = yearMonth.atEndOfMonth();

    // Define date formatter
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Format dates to strings
    String startDate = firstDayOfNextMonth.format(formatter);
    String endDate = lastDayOfNextMonth.format(formatter);

    // Return the interval as an array of strings
    return new String[] {startDate, endDate};
  }
}
