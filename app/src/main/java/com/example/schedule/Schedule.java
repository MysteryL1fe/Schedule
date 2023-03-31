package com.example.schedule;

import com.example.schedule.exceptions.ScheduleException;

public class Schedule {
    private final LessonStruct[][] numerator = new LessonStruct[7][8];
    private final LessonStruct[][] denumerator = new LessonStruct[7][8];
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
                : denumerator[dayOfWeek - 1][lessonNum - 1];
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
}
