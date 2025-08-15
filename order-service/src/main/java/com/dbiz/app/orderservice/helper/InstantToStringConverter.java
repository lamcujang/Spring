package com.dbiz.app.orderservice.helper;//package com.selimhorri.app.helper;
//
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//import java.time.Instant;
//import java.time.format.DateTimeFormatter;
//
//@Converter(autoApply = true)
//public class InstantToStringConverter implements AttributeConverter<String, Instant> {
//
//    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//    @Override
//    public Instant convertToDatabaseColumn(String dateString) {
//        if (dateString == null || dateString.isEmpty()) {
//            return null;
//        }
//        return Instant.parse(dateString);
//    }
//
//    @Override
//    public String convertToEntityAttribute(Instant instant) {
//        if (instant == null) {
//            return null;
//        }
//        return FORMATTER.format(instant);
//    }
//}
