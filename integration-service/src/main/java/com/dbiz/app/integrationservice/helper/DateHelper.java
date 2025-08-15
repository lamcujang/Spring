package com.dbiz.app.integrationservice.helper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateHelper {
  private static   DateTimeFormatter patternYYYYMMDD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static   DateTimeFormatter patternYYYYDDMM = DateTimeFormatter.ofPattern("yyyy-dd-MM");
    private static DateTimeFormatter patternYYYYMMDDHHMMSS = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));

    private static DateTimeFormatter patternddMMyyyy = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private  static DateTimeFormatter patternyyyyMMdd = DateTimeFormatter.ofPattern("yyyy-MM-dd");


  public static Instant toInstant(String date) {
      LocalDate localDate = LocalDate.parse(date, patternYYYYMMDD);
      return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
  }

    public static Instant toInstantDateAndTime(String date) {
        LocalDateTime localDateTime = LocalDateTime.parse(date, patternYYYYMMDDHHMMSS);
        return localDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();
    }

    public static LocalDate toLocalDate(String date) {
        return LocalDate.parse(date, patternYYYYMMDD);
    }

    public static String fromLocalDate(LocalDate date) {
        return patternYYYYMMDD.format(date);
    }

    public static String fromInstant(Instant instant) {
        return patternYYYYMMDDHHMMSS.format(instant);
    }

    public static String fromDateTimeToDate(String param) { // yyyy-MM-dd HH:mm:ss -> dd-MM-yyyy
        LocalDateTime dateTime = LocalDateTime.parse(param, patternYYYYMMDDHHMMSS);
        StringBuilder formattedDate = new StringBuilder(dateTime.format(patternddMMyyyy));
        return formattedDate.toString();
    }

    public static String fromDateTimeToDate2(String instant) {
        LocalDateTime localDateTime = LocalDateTime.parse(instant, patternYYYYMMDDHHMMSS);
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate.format(patternyyyyMMdd);
    }

    public static String toYYYYDDMM(String instant) {
        LocalDateTime localDateTime = LocalDateTime.parse(instant, patternYYYYMMDDHHMMSS);
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate.format(patternYYYYDDMM);
    }

    public static String toYYYYDDMM(Instant instant) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate.format(patternYYYYDDMM);
    }

    public static String fromDateTimeToDate2(Instant instant) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate.format(patternyyyyMMdd);
    }

    public static String fromInstantToDate(Instant instant) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate.format(patternyyyyMMdd);
    }




}
