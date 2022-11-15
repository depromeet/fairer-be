package com.depromeet.fairer.global.util;

import org.hibernate.validator.cfg.context.Cascadable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {
    public static LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static LocalDate stringToLocalDate(String localDate) {
        return LocalDate.parse(localDate, DateTimeFormatter.ISO_DATE);
    }

    public static String localDateToString(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ISO_DATE);
    }

    public static String convertDayOfWeekToEng(DayOfWeek dayOfWeek) {
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.US);
    }
}
