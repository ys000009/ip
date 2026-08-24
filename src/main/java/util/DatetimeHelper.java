package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatetimeHelper {

    public static DateTimeFormatter inputFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");

    public static LocalDateTime parseInput(String s) {
        return LocalDateTime.parse(s, inputFormatter);
    }

    public static String getIso(LocalDateTime datetime) {
        return datetime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static LocalDateTime parseIso(String s) {
        return LocalDateTime.parse(s, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}