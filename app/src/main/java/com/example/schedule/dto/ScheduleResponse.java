package com.example.schedule.dto;

public class ScheduleResponse {
    private FlowResponse flow;
    private LessonResponse lesson;
    private int dayOfWeek, lessonNum;
    private boolean numerator;

    public FlowResponse getFlow() {
        return flow;
    }

    public void setFlow(FlowResponse flow) {
        this.flow = flow;
    }

    public LessonResponse getLesson() {
        return lesson;
    }

    public void setLesson(LessonResponse lesson) {
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
