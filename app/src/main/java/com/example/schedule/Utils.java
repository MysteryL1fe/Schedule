package com.example.schedule;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import java.util.Calendar;

public class Utils {
    public static void checkDonations(@NonNull Context context) {
        SharedPreferences saves = context.getSharedPreferences("ScheduleSaves", MODE_PRIVATE);
        String lastTimeDonationShowed = saves.getString("lastTimeDonationShowed", "");
        Calendar calendar = Calendar.getInstance();
        String curDay = String.format("%s.%s.%s", calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
        /*if (!lastTimeDonationShowed.equals(curDay)) {
            SharedPreferences.Editor editor = saves.edit();
            editor.putString("lastTimeDonationShowed", curDay);
            editor.apply();
            Intent intent = new Intent(context, DonationActivity.class);
            context.startActivity(intent);
        }*/
    }

    @NonNull
    public static Schedule initSchedule(int subgroup) {
        Schedule schedule;
        switch (subgroup) {
            case 1012:
                schedule = new Schedule_1012();
                break;
            case 1021:
                schedule = new Schedule_1021();
                break;
            case 1022:
                schedule = new Schedule_1022();
                break;
            case 1031:
                schedule = new Schedule_1031();
                break;
            case 1032:
                schedule = new Schedule_1032();
                break;
            case 1041:
                schedule = new Schedule_1041();
                break;
            case 1042:
                schedule = new Schedule_1042();
                break;
            case 1051:
                schedule = new Schedule_1051();
                break;
            case 1052:
                schedule = new Schedule_1052();
                break;
            case 1061:
                schedule = new Schedule_1061();
                break;
            case 1062:
                schedule = new Schedule_1062();
                break;
            case 1071:
                schedule = new Schedule_1071();
                break;
            case 1072:
                schedule = new Schedule_1072();
                break;
            case 1081:
                schedule = new Schedule_1081();
                break;
            case 1082:
                schedule = new Schedule_1082();
                break;
            case 1091:
                schedule = new Schedule_1091();
                break;
            case 1092:
                schedule = new Schedule_1092();
                break;
            case 1093:
                schedule = new Schedule_1093();
                break;
            case 1101:
                schedule = new Schedule_1101();
                break;
            case 1102:
                schedule = new Schedule_1102();
                break;
            case 1111:
                schedule = new Schedule_1111();
                break;
            case 1112:
                schedule = new Schedule_1112();
                break;
            case 1121:
                schedule = new Schedule_1121();
                break;
            case 1131:
                schedule = new Schedule_1131();
                break;
            case 1132:
                schedule = new Schedule_1132();
                break;
            case 1141:
                schedule = new Schedule_1141();
                break;
            case 1151:
                schedule = new Schedule_1151();
                break;
            case 1152:
                schedule = new Schedule_1152();
                break;
            case 1161:
                schedule = new Schedule_1161();
                break;
            case 1162:
                schedule = new Schedule_1162();
                break;
            default:
                schedule = new Schedule_1011();
                break;
        }
        schedule.Init();
        return schedule;
    }

    public static int getLessonByTime(int hour, int minute) {
        int time = hour * 60 + minute;
        if (time < 480) {
            return -1;
        } else if (time <= 575) {
            return 0;
        } else if (time <= 585) {
            return -2;
        } else if (time <= 680) {
            return 1;
        } else if (time <= 690) {
            return -3;
        } else if (time <= 785) {
            return 2;
        } else if (time <= 805) {
            return -4;
        } else if (time <= 900) {
            return 3;
        } else if (time <= 910) {
            return -5;
        } else if (time <= 1005) {
            return 4;
        } else if (time <= 1015) {
            return -6;
        } else if (time <= 1110) {
            return 5;
        } else if (time <= 1120) {
            return -7;
        } else if (time <= 1200) {
            return 6;
        } else if (time <= 1210) {
            return -8;
        } else if (time <= 1290) {
            return 7;
        } else return -9;
    }

    @NonNull
    @Contract(pure = true)
    public static String getTimeByLesson(int lesson) {
        switch (lesson) {
            case 0:
                return "8:00 - 9:35";
            case 1:
                return "9:45 - 11:20";
            case 2:
                return "11:30 - 13:05";
            case 3:
                return "13:25 - 15:00";
            case 4:
                return "15:10 - 16:45";
            case 5:
                return "16:55 - 18:30";
            case 6:
                return "18:40 - 20:00";
            case 7:
                return "20:10 - 21:30";
            default:
                return "";
        }
    }

    @Nullable
    public static LessonStruct getLessonBySchedule(int scheduleDayOfWeek, int scheduleLessonNum, boolean schedulenumerator, Schedule schedule) {
        if (schedulenumerator) {
            switch (scheduleDayOfWeek) {
                case 0:
                    return StrToLessonStruct(schedule.Monday_1[scheduleLessonNum]);
                case 1:
                    return StrToLessonStruct(schedule.Tuesday_1[scheduleLessonNum]);
                case 2:
                    return StrToLessonStruct(schedule.Wednesday_1[scheduleLessonNum]);
                case 3:
                    return StrToLessonStruct(schedule.Thursday_1[scheduleLessonNum]);
                case 4:
                    return StrToLessonStruct(schedule.Friday_1[scheduleLessonNum]);
                case 5:
                    return StrToLessonStruct(schedule.Saturday_1[scheduleLessonNum]);
                default:
                    return null;
            }
        } else {
            switch (scheduleDayOfWeek) {
                case 0:
                    return StrToLessonStruct(schedule.Monday_0[scheduleLessonNum]);
                case 1:
                    return StrToLessonStruct(schedule.Tuesday_0[scheduleLessonNum]);
                case 2:
                    return StrToLessonStruct(schedule.Wednesday_0[scheduleLessonNum]);
                case 3:
                    return StrToLessonStruct(schedule.Thursday_0[scheduleLessonNum]);
                case 4:
                    return StrToLessonStruct(schedule.Friday_0[scheduleLessonNum]);
                case 5:
                    return StrToLessonStruct(schedule.Saturday_0[scheduleLessonNum]);
                default:
                    return null;
            }
        }
    }

    public static LessonStruct StrToLessonStruct (String lesson) {
        if (lesson == null) return null;

        LessonStruct lessonStruct = new LessonStruct();
        switch (lesson) {
            case "Физическая культура и спорт":
                lessonStruct.name = "Физкультура";
                lessonStruct.teacher = "-";
                lessonStruct.cabinet = "-";
                break;
            case "Военная подготовка":
                lessonStruct.name = "Военная подготовка";
                lessonStruct.teacher = "-";
                lessonStruct.cabinet = "-";
                break;
            case "УЦ":
                lessonStruct.name = "УЦ";
                lessonStruct.teacher = "-";
                lessonStruct.cabinet = "-";
                break;
            default:
                String[] lesson_split = lesson.split(" ");
                if (lesson_split.length > 4) {
                    String lessonCabinet = lesson_split[lesson_split.length - 1];
                    String lessonTeacher = lesson_split[lesson_split.length - 3] + " " + lesson_split[lesson_split.length - 2];
                    StringBuilder lessonName = new StringBuilder();
                    for (int i = 0; i < lesson_split.length - 4; i++) {
                        if (i != 0) lessonName.append(" ");
                        lessonName.append(lesson_split[i]);
                    }
                    lessonStruct.name = lessonName.toString();
                    lessonStruct.teacher = lessonTeacher;
                    lessonStruct.cabinet = lessonCabinet;
                } else {
                    return null;
                }
                break;
        }
        return lessonStruct;
    }
}
