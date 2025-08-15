package org.common.dbiz.helper;




import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;


public class DateHelper {
  private static   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static   DateTimeFormatter formatterddMMyyyy = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static   DateTimeFormatter formatterddMMyyyyHHmmss = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm:ss")
            .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));

    private static   DateTimeFormatter formatterMMyyyy = DateTimeFormatter.ofPattern("MMyyyy");
    private static DateTimeFormatter formatterDateTime = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));
    public static Instant currentInstant = LocalDateTime.now().toInstant(ZoneOffset.UTC);
    private static DateTimeFormatter formatterDateTime2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static DateTimeFormatter formatterDateAndMonth = DateTimeFormatter.ofPattern("dd-MM HH:mm");

    public static Instant toInstantUTC(String date) {
        LocalDateTime localDate = LocalDateTime.parse(date, formatterDateTime2);
        return localDate.toInstant(ZoneOffset.UTC);
    }

    public static Instant toInstantNowUTC() {
        return LocalDateTime.now().toInstant(ZoneOffset.UTC);
    }

    public static String fromInstantUTC(Instant instant) {
        return formatterDateTime2
                .withZone(ZoneOffset.UTC)  // Ensure UTC timezone
                .format(instant);
    }

    public static String castInstantToDateAndMonth(Instant instant) {
        return formatterDateAndMonth
                .withZone(ZoneOffset.UTC)  // Ensure UTC timezone
                .format(instant);
    }

    public static String fromTimestampStd(Timestamp timestamp) {
        return timestamp.toLocalDateTime().format(formatterDateTime2);
    }

    public static Instant toInstantFromDateTimeString(String datetime) {
        return Instant.parse(datetime);
    }

    public static Instant toInstant(String date) {
      LocalDate localDate = LocalDate.parse(date, formatter);
      return localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
  }
    public static Instant toInstant2(String date) {
        try {
            return Instant.from(formatterDateTime.parse(date));
        } catch (Exception e) {
            LocalDate localDate = LocalDate.parse(date, formatter);
            return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        }
    }
    public static Instant convertLocalTimeToInstant(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localTime = LocalTime.parse(time, formatter);
        return localTime.atDate(LocalDate.of(1970, 1, 1))
                .toInstant(ZoneOffset.UTC);
    }
    public static LocalTime convertInstantToLocalTime(Instant instant) {
        return instant.atZone(ZoneOffset.UTC).toLocalTime();
    }

    public static Instant toInstantDateAndTime(String date) {
        LocalDateTime localDateTime = LocalDateTime.parse(date, formatterDateTime);
        return localDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();
    }

    public static String fromInstantDateAndTime1(Instant instant) {
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        return zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    public static Instant toInstantDateAndTime1(String date) {
        LocalDateTime localDateTime = LocalDateTime.parse(date, formatterDateTime);

        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        return zonedDateTime.toInstant();
    }


    public static String fromInstantDateAndTime(Instant instant) {
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        return zonedDateTime.format(formatterDateTime);
    }

    public static Instant toInstantDateAndTime() {
        LocalDateTime localDateTime =  LocalDateTime.now();
        return localDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();
    }

    public static LocalDate toLocalDate(String date) {
        return LocalDate.parse(date, formatter);
    }
    public static LocalDateTime toLocalDateTime(String date) {
        return LocalDateTime.parse(date, formatterDateTime);
    }

    public static Date toDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String fromLocalDate(LocalDate date) {
        return formatter.format(date);
    }

    public static String fromLocalDateMMyyyy(LocalDate date) {
        return formatterMMyyyy.format(date);
    }

    public static String    fromInstant(Instant instant) {
        return formatterDateTime.format(instant);
    }

    public static String fromLocalDateddMMyyyy(LocalDate date) {
        return formatterddMMyyyy.format(date);
    }

    public static String fromInstantddMMyyyyHHmmss(Instant instant) {
        return formatterddMMyyyyHHmmss.format(instant);
    }


    public static String fromTimeStamp (Timestamp timestamp) {
        return formatterDateTime.format(timestamp.toInstant());
    }


    public static String convertDate(Instant date)
    {
        var dateTime = date.atZone(ZoneId.systemDefault());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTime.format(dateFormatter);
    }

    public static String convertTime(Instant date)
    {
        var dateTime = date.atZone(ZoneId.systemDefault());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return dateTime.format(timeFormatter);
    }

    public static String convertInstantToPattern(Instant date, String pattern)
    {
        var dateTime = date.atZone(ZoneId.systemDefault());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(timeFormatter);
    }


    public static String convertTimeNZtoTimeZ(String date){
        // 1. Parse chuỗi từ DB

        LocalDateTime localDateTime;
        if (date == null || date.isEmpty()) {
            localDateTime = LocalDateTime.now(); // Sử dụng thời gian hiện tại
        } else {
            localDateTime = LocalDateTime.parse(date, formatterDateTime2);
        }

        // 2. Gán timezone
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);

        // 3. Format ISO có offset và không có microseconds
        DateTimeFormatter isoNoNano = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        String formatted = zonedDateTime.format(isoNoNano);

        System.out.println("ISO Format (no microseconds): " + formatted);
        return formatted;
    }

    public static String generateTimeId() {
        // Thời gian hiện tại
        LocalDateTime now = LocalDateTime.now();

        // Format yyyyMMddHHmmssSSS (28 chữ số nếu thêm nano)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String timestampPart = now.format(formatter);

        // Thêm nanoTime để làm chuỗi dài & gần như duy nhất
        long nanoPart = System.nanoTime(); // Hoặc use currentTimeMillis nếu bạn thích

        return timestampPart + String.valueOf(nanoPart).substring(0, 10); // cắt cho đủ ~28 ký tự
    }


    public static LocalTime parseStringToLocalTime(String timeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.parse(timeStr, formatter);
    }

    // Chuyển từ LocalTime sang String
    public static String    formatLocalTimeToString(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return time.format(formatter);
    }
    public static List<Instant> fromDateToInstantRangeUTC(String fromDate, String toDate) {
        LocalDate startDate = LocalDate.parse(fromDate, formatter);
        LocalDate endDate = ( toDate == null || toDate.isEmpty() )
                ? startDate
                : LocalDate.parse(toDate, formatter);

        Instant startInstant = startDate
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();
        Instant endInstant = endDate
                .plusDays(1)
                .atStartOfDay(ZoneOffset.UTC)
                .minusNanos(1)
                .toInstant();

        return List.of(startInstant, endInstant);
    }
}
