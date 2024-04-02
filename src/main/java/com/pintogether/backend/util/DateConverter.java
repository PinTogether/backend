package com.pintogether.backend.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public final class DateConverter {

    public static String convert(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        long minutesDiff = ChronoUnit.MINUTES.between(dateTime, now);
        long weekDiff = ChronoUnit.WEEKS.between(dateTime, now);
        long hoursDiff = ChronoUnit.HOURS.between(dateTime, now);
        long daysDiff = ChronoUnit.DAYS.between(dateTime, now);

        if (minutesDiff < 1) {
            return "방금 전";
        } else if (minutesDiff < 60) {
            return minutesDiff + "분 전";
        } else if (daysDiff < 1) {
            return hoursDiff + "시간 전";
        } else if (daysDiff < 14) {
            return daysDiff + "일 전";
        } else {
            return weekDiff + "주 전";
        }
//        } else if (now.getYear() == dateTime.getYear()) {
//            return dateTime.format(DateTimeFormatter.ofPattern("M/d"));
//        } else {
//            return dateTime.format(DateTimeFormatter.ofPattern("y/M/d"));
//        }
    }

}
