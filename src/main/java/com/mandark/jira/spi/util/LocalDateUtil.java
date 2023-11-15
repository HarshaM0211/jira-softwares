package com.mandark.jira.spi.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public final class LocalDateUtil {


    // String --> LocalDateTime Conversion
    // ------------------------------------------------------------------------

    public static LocalDateTime toLocalDateTime(String dateTimeStr) {
        // Sanity Check
        Verify.hasText(dateTimeStr, "# ToLocalDateTime :: Input String is NULL");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return LocalDateTime.parse(dateTimeStr.trim(), dateTimeFormatter);

    }


    // LocalDateTime --> String Conversion
    // ------------------------------------------------------------------------

    public static String toDateTimeStr(LocalDateTime localDateTime) {
        // Sanity Check
        Verify.notNull(localDateTime, "# ToDateTimeStr :: Input LocalDateTime Object is NULL");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return localDateTime.format(dateTimeFormatter);

    }


    // String --> LocalDate Conversion
    // ------------------------------------------------------------------------

    public static LocalDate toLocalDate(String dateStr) {
        // Sanity Check
        Verify.hasText(dateStr, "# ToLocalDate :: Input String is NULL");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return LocalDate.parse(dateStr.trim(), dateTimeFormatter);

    }


    // LocalDate --> String Conversion
    // ------------------------------------------------------------------------

    public static String toDateStr(LocalDate localDate) {
        // Sanity Check
        Verify.notNull(localDate, "# ToDateStr :: Input LocalDate Object is NULL");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return localDate.format(dateTimeFormatter);

    }
}
