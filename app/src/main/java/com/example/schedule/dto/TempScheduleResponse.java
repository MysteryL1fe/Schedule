package com.example.schedule.dto;

import java.time.LocalDate;

public class TempScheduleResponse {
    FlowResponse flow;
    LessonResponse lesson;
    LocalDate lessonDate;
    int lessonNum;
    boolean willLessonBe;

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
