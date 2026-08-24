package util;

import java.time.format.DateTimeFormatter;

public class DatetimeHelper {
    public static DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");

    public static DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy, HHmm 'hrs'");

    public static DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
}