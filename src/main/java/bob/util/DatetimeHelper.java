package bob.util;

import java.time.format.DateTimeFormatter;

/**
 * Provides date-time formatters used across the Bob application.
 */
public class DatetimeHelper {

    /**
     * Prevents instantiation of this utility class.
     */
    private DatetimeHelper() {
    }

    /** Formatter for parsing user input date-time strings (dd/MM/yy HH:mm). */
    public static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");

    /** Formatter for displaying date-time strings to the user (dd MMMM yyyy, HHmm 'hrs'). */
    public static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy, HHmm 'hrs'");

    /** Formatter for ISO-8601 storage format. */
    public static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
}