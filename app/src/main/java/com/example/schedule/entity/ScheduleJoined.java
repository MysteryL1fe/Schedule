package com.example.schedule.entity;

public class ScheduleJoined {
    private Flow flow;
    private Lesson lesson;
    private int dayOfWeek;
    private int lessonNum;
    private boolean numerator;

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
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
