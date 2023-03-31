package com.example.schedule;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public class Utils {
    private static int[] daysInMonths = new int[] {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static int[] daysInMonthsLeap = new int[] {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static String[] daysOfWeekNames = new String[] {"Понедельник", "Вторник", "Среда",
            "Четверг", "Пятница", "Суббота", "Воскресенье"};
    private static String[] monthsNames = new String[] {"Января", "Февраля", "Марта", "Апреля", "Мая", "Июня",
            "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"};

    /*public static void checkDonations(@NonNull Context context) {
        SharedPreferences saves = context.getSharedPreferences("ScheduleSaves", MODE_PRIVATE);
        String lastTimeDonationShowed = saves.getString("lastTimeDonationShowed", "");
        Calendar calendar = Calendar.getInstance();
        String curDay = String.format("%s.%s.%s", calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
if (!lastTimeDonationShowed.equals(curDay)) {
            SharedPreferences.Editor editor = saves.edit();
            editor.putString("lastTimeDonationShowed", curDay);
            editor.apply();
            Intent intent = new Intent(context, DonationActivity.class);
            context.startActivity(intent);
        }

    }*/

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

    /*@Nullable
    public static LessonStruct getLessonBySchedule(int scheduleDayOfWeek, int scheduleLessonNum,
                                                   boolean scheduleNumerator, Schedule schedule) {
        if (scheduleNumerator) {
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
    }*/

    public static int getDaysFrom2022(int year, int month, int day) {
        int deltaYear = year - 2022;
        int days = 0;
        for (int i = 0; i < deltaYear; i++) {
            if ((2023 + i) % 4 == 0) {
                for (int j = 1; j < 13; j++) {
                    days += daysInMonthsLeap[j - 1];
                }
            } else {
                for (int j = 1; j < 13; j++) {
                    days += daysInMonths[j - 1];
                }
            }
        }

        if (year % 4 == 0) {
            for (int j = 1; j < month; j++) {
                days += daysInMonthsLeap[j - 1];
            }
        } else {
            for (int j = 1; j < month; j++) {
                days += daysInMonths[j - 1];
            }
        }

        return days + day - 1;
    }

    public static int getDayOfWeek(int year, int month, int day) {
        return (getDaysFrom2022(year, month, day) + 5) % 7 + 1;
    }

    public static boolean isNumerator(int year, int month, int day) {
        if (month < 9) {
            return ((getDaysFrom2022(year, month, day) + 5) / 7) % 2 ==
                    ((getDaysFrom2022(year - 1, 9, 1) + 5) / 7) % 2;
        } else {
            return ((getDaysFrom2022(year, month, day) + 5) / 7) % 2 ==
                    ((getDaysFrom2022(year, 9, 1) + 5) / 7) % 2;
        }
    }

    public static String dayOfWeekToStr(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return "Понедельник";
            case 2:
                return "Вторник";
            case 3:
                return "Среда";
            case 4:
                return "Четверг";
            case 5:
                return "Пятница";
            case 6:
                return "Суббота";
            case 7:
                return "Воскресенье";
            default:
                return "";
        }
    }

    public static String monthToStr(int month) {
        switch (month) {
            case 1:
                return "январь";
            case 2:
                return "февраль";
            case 3:
                return "март";
            case 4:
                return "апрель";
            case 5:
                return "май";
            case 6:
                return "июнь";
            case 7:
                return "июль";
            case 8:
                return "август";
            case 9:
                return "сентябрь";
            case 10:
                return "октябрь";
            case 11:
                return "ноябрь";
            case 12:
                return "декабрь";
            default:
                return "";
        }
    }

    /*public static LessonStruct StrToLessonStruct (String lesson) {
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
    }*/
}
