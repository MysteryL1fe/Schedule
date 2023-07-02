package com.example.schedule;

import com.example.schedule.exceptions.ScheduleException;

import java.util.Arrays;
import java.util.Objects;

public class Schedule {
    private LessonStruct[][] numerator = new LessonStruct[7][8];
    private LessonStruct[][] denominator = new LessonStruct[7][8];
    private final int flowLvl, course, group, subgroup;

    public Schedule() throws ScheduleException {
        this(0, 0, 0, 0);
    }

    public Schedule(int flowLvl, int course, int group, int subgroup) throws ScheduleException {
        if (flowLvl < 0 || flowLvl > 2)
            throw new ScheduleException("Неверно указан уровень обучения");
        if (course < 0) throw new ScheduleException("Неверно указанный курс");
        if (group < 0) throw new ScheduleException("Неверно указанная группа");
        if (subgroup < 0) throw new ScheduleException("Неверно указанная подгруппа");
        this.flowLvl = flowLvl;
        this.course = course;
        this.group = group;
        this.subgroup = subgroup;
    }

    public LessonStruct getLesson(int dayOfWeek, int lessonNum, boolean isNumerator)
            throws ScheduleException {
        if (dayOfWeek < 1 || dayOfWeek > 7) throw new ScheduleException("Неверный день недели");
        if (lessonNum < 1 || lessonNum > 8) throw new ScheduleException("Неверный номер пары");
        return isNumerator ? numerator[dayOfWeek - 1][lessonNum - 1]
                : denominator[dayOfWeek - 1][lessonNum - 1];
    }

    public void setLesson(int dayOfWeek, int lessonNum, boolean isNumerator,
                          LessonStruct newLesson) throws ScheduleException {
        if (dayOfWeek < 1 || dayOfWeek > 7) throw new ScheduleException("Неверный день недели");
        if (lessonNum < 1 || lessonNum > 8) throw new ScheduleException("Неверный номер пары");
        if (isNumerator) numerator[dayOfWeek - 1][lessonNum - 1] = newLesson;
        else denominator[dayOfWeek - 1][lessonNum - 1] = newLesson;
    }

    public int getFlowLvl() {
        return flowLvl;
    }

    public int getCourse() {
        return course;
    }

    public int getGroup() {
        return group;
    }

    public int getSubgroup() {
        return subgroup;
    }

    public String toExport() {
        StringBuilder sb = new StringBuilder();
        for (LessonStruct[] lessonStructs : numerator) {
            for (LessonStruct lessonStruct : lessonStructs) {
                if (lessonStruct == null) sb.append("null&");
                else sb.append(String.format("%s&", lessonStruct.toExport()));
            }
        }
        sb.append(";");
        for (LessonStruct[] lessonStructs : denominator) {
            for (LessonStruct lessonStruct : lessonStructs) {
                if (lessonStruct == null) sb.append("_&");
                else sb.append(String.format("%s&", lessonStruct.toExport()));
            }
        }
        return sb.toString();
    }

    public LessonStruct[][] getNumerator() {
        return numerator;
    }

    public void setNumerator(LessonStruct[][] numerator) {
        this.numerator = numerator;
    }

    public LessonStruct[][] getDenominator() {
        return denominator;
    }

    public void setDenominator(LessonStruct[][] denominator) {
        this.denominator = denominator;
    }
}
