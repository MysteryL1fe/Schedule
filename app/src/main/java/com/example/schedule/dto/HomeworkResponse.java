package com.example.schedule.dto;

import java.time.LocalDate;

public class HomeworkResponse {
    FlowResponse flow;
    String lessonName, homework;
    LocalDate lessonDate;
    int lessonNum;

    public FlowResponse getFlow() {
        return flow;
    }

    public void setFlow(FlowResponse flow) {
        this.flow = flow;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
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
}
