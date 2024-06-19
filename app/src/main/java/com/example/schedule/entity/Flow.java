package com.example.schedule.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Flow {
    private long id;
    private int flowLvl;
    private int course;
    private int flow;
    private int subgroup;
    private LocalDateTime lastEdit;
    private LocalDate lessonsStartDate;
    private LocalDate sessionStartDate;
    private LocalDate sessionEndDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFlowLvl() {
        return flowLvl;
    }

    public void setFlowLvl(int flowLvl) {
        this.flowLvl = flowLvl;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    public int getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(int subgroup) {
        this.subgroup = subgroup;
    }

    public LocalDateTime getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(LocalDateTime lastEdit) {
        this.lastEdit = lastEdit;
    }

    public LocalDate getLessonsStartDate() {
        return lessonsStartDate;
    }

    public void setLessonsStartDate(LocalDate lessonsStartDate) {
        this.lessonsStartDate = lessonsStartDate;
    }

    public LocalDate getSessionStartDate() {
        return sessionStartDate;
    }

    public void setSessionStartDate(LocalDate sessionStartDate) {
        this.sessionStartDate = sessionStartDate;
    }

    public LocalDate getSessionEndDate() {
        return sessionEndDate;
    }

    public void setSessionEndDate(LocalDate sessionEndDate) {
        this.sessionEndDate = sessionEndDate;
    }
}
