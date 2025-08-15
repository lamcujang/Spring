package com.dbiz.app.paymentservice.helper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateHelper {
  private static   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static   DateTimeFormatter formatterMMyyyy = DateTimeFormatter.ofPattern("MMyyyy");
    private static DateTimeFormatter formatterDateTime = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));

  public static Instant toInstant(String date) {
      LocalDate localDate = LocalDate.parse(date, formatter);
      return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
  }

    public static Instant toInstant2(String date) {
        try {
            // Attempt to parse with date-time format if the input includes time
            return Instant.from(formatterDateTime.parse(date));
        } catch (Exception e) {
            // Fallback to parsing only the date if no time component is included
            LocalDate localDate = LocalDate.parse(date, formatter);
            return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        }
    }

    public static LocalDate toLocalDate(String date) {
        return LocalDate.parse(date, formatter);
    }

    public static String fromLocalDate(LocalDate date) {
        return formatter.format(date);
    }

    public static String fromLocalDateTime(LocalDateTime date) {
        return formatterDateTime.format(date);
    }
    public static String fromInstant(Instant instant) {
        return formatterDateTime.format(instant);
    }
    public static String fromLocalDateMMyyyy(LocalDate date) {
        return formatterMMyyyy.format(date);
    }
}
