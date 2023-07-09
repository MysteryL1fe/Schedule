package com.example.schedule;

public class Homework {
    public final int year, month, day, lessonNum;
    public final String homework, lessonName;

    public Homework(int year, int month, int day, int lessonNum,
                    String homework, String lessonName) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.lessonNum = lessonNum;
        this.homework = homework;
        this.lessonName = lessonName;
    }
}
