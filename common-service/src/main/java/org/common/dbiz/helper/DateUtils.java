package org.common.dbiz.helper;

import java.time.LocalDate;
import java.util.function.Consumer;

public class DateUtils {
        public static void safeSetDate(String dateStr, Consumer<LocalDate> setter) {
            if (dateStr != null) {
                setter.accept(DateHelper.toLocalDate(dateStr));
            }
        }


}
