package com.example.schedule.entity;

import java.time.LocalDate;

public class TempSchedule {
    private long id;
    private long flow;
    private long lesson;
    private LocalDate lessonDate;
    private int lessonNum;
    private boolean willLessonBe;

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

    public boolean isWillLessonBe() {
        return willLessonBe;
    }

    public void setWillLessonBe(boolean willLessonBe) {
        this.willLessonBe = willLessonBe;
    }
}
