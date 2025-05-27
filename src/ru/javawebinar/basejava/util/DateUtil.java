package ru.javawebinar.basejava.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static String format(LocalDate ld) {
        return DateTimeFormatter.ISO_DATE.format(ld);
    }

    public static LocalDate parse(String strDate) {
        return LocalDate.parse(strDate, DateTimeFormatter.ISO_DATE);
    }
}
