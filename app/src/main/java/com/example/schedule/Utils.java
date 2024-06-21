package com.example.schedule;

import androidx.annotation.IntRange;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Utils {
    public static final String[] daysOfWeekNames = new String[] {
            "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"
    };
    private static final int[] lessonBeginningHours = new int[] {
            8, 9, 11, 13, 15, 16, 18, 20
    };
    private static final int[] lessonBeginningMinutes = new int[] {
            0, 45, 30, 25, 10, 55, 40, 10
    };
    private static final int[] lessonEndingHours = new int[] {
            9, 11, 13, 15, 16, 18, 20, 21
    };
    private static final int[] lessonEndingMinutes = new int[] {
            35, 20, 5, 0, 45, 30, 0, 30
    };

    public static String getTimeByLesson(@IntRange(from = 1, to = 8) int lessonNum) {
        if (lessonNum < 1 || lessonNum > 8) return "";
        return String.format(
                "%s:%2s-%s:%2s", lessonBeginningHours[lessonNum - 1],
                lessonBeginningMinutes[lessonNum - 1], lessonEndingHours[lessonNum - 1],
                lessonEndingMinutes[lessonNum - 1]
        ).replace(' ', '0').replace("-", " - ");
    }

    public static int getLessonBeginningHour(@IntRange(from = 1, to = 8) int lessonNum) {
        if (lessonNum < 1 || lessonNum > 8) return -1;
        return lessonBeginningHours[lessonNum - 1];
    }

    public static int getLessonBeginningMinute(@IntRange(from = 1, to = 8) int lessonNum) {
        if (lessonNum < 1 || lessonNum > 8) return -1;
        return lessonBeginningMinutes[lessonNum - 1];
    }

    public static int getLessonEndingHour(@IntRange(from = 1, to = 8) int lessonNum) {
        if (lessonNum < 1 || lessonNum > 8) return -1;
        return lessonEndingHours[lessonNum - 1];
    }

    public static int getLessonEndingMinute(@IntRange(from = 1, to = 8) int lessonNum) {
        if (lessonNum < 1 || lessonNum > 8) return -1;
        return lessonEndingMinutes[lessonNum - 1];
    }

    public static boolean isNumerator(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        return isNumerator(date);
    }

    public static boolean isNumerator(LocalDate date) {
        LocalDate prevYear = LocalDate.of(date.getYear() - 1, 9, 1);
        prevYear = prevYear.minusDays(prevYear.getDayOfWeek().getValue() - 1);

        LocalDate curYear = LocalDate.of(date.getYear(), 9, 1);
        curYear = curYear.minusDays(curYear.getDayOfWeek().getValue() - 1);

        if (date.isAfter(curYear)) return (date.toEpochDay() - curYear.toEpochDay()) / 7 % 2 == 0;
        return (date.toEpochDay() - prevYear.toEpochDay()) / 7 % 2 == 0;
    }

    public static String dayOfWeekToStr(@IntRange(from = 1, to = 7) int dayOfWeek) {
        return dayOfWeek < 1 || dayOfWeek > 7 ? "" : daysOfWeekNames[dayOfWeek - 1];
    }

    public static LocalDateTime getNearestLesson(int dayOfWeek, int lessonNum, boolean isNumerator) {
        return getNearestLesson(dayOfWeek, lessonNum, isNumerator, LocalDateTime.now());
    }

    public static LocalDateTime getNearestLesson(
            int dayOfWeek, int lessonNum, boolean isNumerator, LocalDateTime from
    ) {
        LocalDate fromDate = from.toLocalDate();
        boolean thisWeek = fromDate.getDayOfWeek().getValue() < dayOfWeek
                || fromDate.getDayOfWeek().getValue() == dayOfWeek
                && from.toLocalTime().isBefore(LocalTime.of(
                        lessonEndingHours[lessonNum - 1], lessonEndingMinutes[lessonNum - 1]
                ));
        LocalDateTime result = LocalDateTime.of(
                LocalDate.ofEpochDay(
                        fromDate.toEpochDay() + dayOfWeek - from.getDayOfWeek().getValue()
                                + (thisWeek ? 0 : 7)
                ),
                LocalTime.of(
                        lessonBeginningHours[lessonNum - 1],
                        lessonBeginningMinutes[lessonNum - 1]
                )
        );
        if (isNumerator(result.toLocalDate()) != isNumerator) result = result.plusDays(7);
        return result;
    }
}
