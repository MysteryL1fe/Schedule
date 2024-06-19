package com.example.schedule.entity;

public class Schedule {
    private long id;
    private long flow;
    private long lesson;
    private int dayOfWeek;
    private int lessonNum;
    private boolean numerator;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFlow() {
        return flow;
    }

    public void setFlow(long flow) {
        this.flow = flow;
    }

    public long getLesson() {
        return lesson;
    }

    public void setLesson(long lesson) {
        this.lesson = lesson;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getLessonNum() {
        return lessonNum;
    }

    public void setLessonNum(int lessonNum) {
        this.lessonNum = lessonNum;
    }

    public boolean isNumerator() {
        return numerator;
    }

    public void setNumerator(boolean numerator) {
        this.numerator = numerator;
    }
}
