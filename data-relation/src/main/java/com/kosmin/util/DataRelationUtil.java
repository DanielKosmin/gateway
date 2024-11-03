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
    final String[] formats = {"MM/dd/yy", "MM/dd/yyyy"};
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

  public static String[] getPreviousMonthRange(String startDate, String endDate) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Parse the input start and end dates
    LocalDate start = LocalDate.parse(startDate, formatter);
    LocalDate end = LocalDate.parse(endDate, formatter);

    // Get the previous month from the start date
    YearMonth previousMonth = YearMonth.from(start).minusMonths(1);

    // Calculate the start and end of the previous month
    LocalDate previousMonthStart = previousMonth.atDay(1);
    LocalDate previousMonthEnd = previousMonth.atEndOfMonth();

    // Format output dates as strings
    String formattedStart = previousMonthStart.format(formatter);
    String formattedEnd = previousMonthEnd.format(formatter);

    return new String[] {formattedStart, formattedEnd};
  }
}
