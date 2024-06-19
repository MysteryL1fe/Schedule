package com.example.schedule.entity;

import java.time.LocalDate;

public class Homework {
    private long id;
    private String homework;
    private LocalDate lessonDate;
    private int lessonNum;
    private long flow;
    private String lessonName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

    public LocalDate getLessonDate() {
        return lessonDate;
    }

    public void setLessonDate(LocalDate lessonDate) {
        this.lessonDate = lessonDate;
    }

    public int getLessonNum() {
        return lessonNum;
    }

    public void setLessonNum(int lessonNum) {
        this.lessonNum = lessonNum;
    }

    public long getFlow() {
        return flow;
    }

    public void setFlow(long flow) {
        this.flow = flow;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }
}
