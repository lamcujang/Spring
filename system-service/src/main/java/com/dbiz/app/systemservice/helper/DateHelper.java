package com.dbiz.app.systemservice.helper;



import com.dbiz.app.systemservice.constant.AppConstant;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateHelper {
  private static   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static DateTimeFormatter formatterDateTime = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));
  public static Instant toInstant(String date) {
      LocalDate localDate = LocalDate.parse(date, formatter);
      return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
  }

    public static Instant toInstantDateAndTime(String date) {
        LocalDateTime localDateTime = LocalDateTime.parse(date, formatterDateTime);
        return localDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();
    }

    public static String fromInstantDateAndTime(Instant instant) {
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        return zonedDateTime.format(formatterDateTime);
    }

    public static Instant toInstantDateAndTime() {
        LocalDateTime localDateTime = AppConstant.CURRENT_DATETIME;
        return localDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();
    }

    public static LocalDate toLocalDate(String date) {
        return LocalDate.parse(date, formatter);
    }

    public static String fromLocalDate(LocalDate date) {
        return formatter.format(date);
    }

    public static String fromInstant(Instant instant) {
        return formatterDateTime.format(instant);
    }


    public static String fromTimeStamp (Timestamp timestamp) {
        return formatterDateTime.format(timestamp.toInstant());
    }
}
