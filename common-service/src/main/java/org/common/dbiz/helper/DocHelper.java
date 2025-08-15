package org.common.dbiz.helper;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DocHelper {

    public static String generateDocNo(String prefix, Integer maxId) {
        // Format time as ssMM (seconds + minutes)
        String timePart = LocalTime.now().format(DateTimeFormatter.ofPattern("mmss"));

        // Concatenate prefix, timePart, and maxId
        return prefix + timePart + maxId;
    }

}
