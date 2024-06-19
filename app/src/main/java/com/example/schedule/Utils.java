package com.example.schedule;

import androidx.annotation.IntRange;

import java.time.LocalDate;

public class Utils {
    public static final String[] daysOfWeekNames = new String[] {
            "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"
    };
    public static final String[] monthsNames = new String[] {
            "Января", "Февраля", "Марта", "Апреля", "Мая", "Июня", "Июля", "Августа", "Сентября",
            "Октября", "Ноября", "Декабря"
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

        LocalDate prevYear = LocalDate.of(year - 1, 9, 1);
        prevYear = prevYear.minusDays(prevYear.getDayOfWeek().getValue() - 1);

        LocalDate curYear = LocalDate.of(year, 9, 1);
        curYear = curYear.minusDays(curYear.getDayOfWeek().getValue() - 1);

        if (date.isAfter(curYear)) return (date.toEpochDay() - curYear.toEpochDay()) / 7 % 2 == 0;
        return (date.toEpochDay() - prevYear.toEpochDay()) / 7 % 2 == 0;
    }

    public static String dayOfWeekToStr(@IntRange(from = 1, to = 7) int dayOfWeek) {
        return dayOfWeek < 1 || dayOfWeek > 7 ? "" : daysOfWeekNames[dayOfWeek - 1];
    }

    public static String monthToStr(@IntRange(from = 1, to = 12) int month) {
        return month < 1 || month > 12 ? "" : monthsNames[month - 1];
    }
}
