package com.dbiz.app.productservice.helper;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateHelper {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Instant toInstant(String date) {
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    public static Instant toInstantDateUTC(String date) {
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
    }

    private static final DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static Instant toInstantDateTime(String date) {
        LocalDate localDate = LocalDate.parse(date, formatterDateTime);
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }



}
