package com.example.schedule;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Utils {
    private static final String[] daysOfWeekNames = new String[] {"Понедельник", "Вторник", "Среда",
            "Четверг", "Пятница", "Суббота", "Воскресенье"};
    private static final String[] monthsNames = new String[] {"Января", "Февраля", "Марта",
            "Апреля", "Мая", "Июня", "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"};

    @NonNull
    @Contract(pure = true)
    public static String getTimeByLesson(int lesson) {
        switch (lesson) {
            case 1:
                return "8:00 - 9:35";
            case 2:
                return "9:45 - 11:20";
            case 3:
                return "11:30 - 13:05";
            case 4:
                return "13:25 - 15:00";
            case 5:
                return "15:10 - 16:45";
            case 6:
                return "16:55 - 18:30";
            case 7:
                return "18:40 - 20:00";
            case 8:
                return "20:10 - 21:30";
            default:
                return "";
        }
    }

    private static int getEndLessonTime(int lesson) {
        if (lesson > 7 || lesson < -9) return 0;
        switch (lesson) {
            case 0:
                return 575 * 60;
            case 1:
                return 680 * 60;
            case 2:
                return 785 * 60;
            case 3:
                return 900 * 60;
            case 4:
                return 1005 * 60;
            case 5:
                return 1110 * 60;
            case 6:
                return 1200 * 60;
            case 7:
                return 1290 * 60;
            case -1:
                return 480 * 60;
            case -2:
                return 585 * 60;
            case -3:
                return 690 * 60;
            case -4:
                return 805 * 60;
            case -5:
                return 910 * 60;
            case -6:
                return 1015 * 60;
            case -7:
                return 1120 * 60;
            case -8:
                return 1210 * 60;
            default:
                return 1440 * 60;
        }
    }

    public static int getDayOfWeek(int year, int month, int day) {
        Calendar calendar = new GregorianCalendar(year, month - 1, day);
        return (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1;
    }

    public static boolean isNumerator(int year, int month, int day) {
        Calendar from = SettingsStorage.getCountdownBeginning();
        Calendar to = new GregorianCalendar(year, month - 1, day);
        long diff = Math.abs(to.getTime().getTime() - from.getTime().getTime());
        long daysBetween = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        return daysBetween / 7 % 2 == 0;
    }

    public static String dayOfWeekToStr(int dayOfWeek) {
        return dayOfWeek < 1 || dayOfWeek > 7 ? "" : daysOfWeekNames[dayOfWeek - 1];
    }

    public static String monthToStr(int month) {
        return month < 1 || month > 12 ? "" : monthsNames[month - 1];
    }

    public static String generateStr() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append((char) ('a' + Math.abs(random.nextInt()) % 26));
        }
        return sb.toString();
    }

    public static int getLesson() {
        Calendar calendar = Calendar.getInstance();
        int time = calendar.get(Calendar.HOUR_OF_DAY) * 3600 + calendar.get(Calendar.MINUTE) * 60
                + calendar.get(Calendar.SECOND);
        if (time < 28800) {
            return -1;
        } else if (time <= 34500) {
            return 0;
        } else if (time <= 35100) {
            return -2;
        } else if (time <= 40800) {
            return 1;
        } else if (time <= 41400) {
            return -3;
        } else if (time <= 47100) {
            return 2;
        } else if (time <= 48300) {
            return -4;
        } else if (time <= 54000) {
            return 3;
        } else if (time <= 54600) {
            return -5;
        } else if (time <= 60300) {
            return 4;
        } else if (time <= 60900) {
            return -6;
        } else if (time <= 66600) {
            return 5;
        } else if (time <= 67200) {
            return -7;
        } else if (time <= 72000) {
            return 6;
        } else if (time <= 72600) {
            return -8;
        } else if (time <= 77400) {
            return 7;
        } else return -9;
    }

    public static int getTimeToNextLesson() {
        int lesson = getLesson();
        Calendar calendar = Calendar.getInstance();
        if (lesson > -9) return getEndLessonTime(lesson) -
                calendar.get(Calendar.HOUR_OF_DAY) * 3600 -
                calendar.get(Calendar.MINUTE) * 60 - calendar.get(Calendar.SECOND) + 1;
        else return getEndLessonTime(lesson)
                - calendar.get(Calendar.HOUR_OF_DAY) * 3600
                - calendar.get(Calendar.MINUTE) * 60 - calendar.get(Calendar.SECOND)
                + getEndLessonTime(-1) + 1;
    }
}
