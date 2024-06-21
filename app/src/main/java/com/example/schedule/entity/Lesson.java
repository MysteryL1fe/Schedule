package com.example.schedule.entity;

import java.util.Objects;

public class Lesson {
    private long id;
    private String name;
    private String teacher;
    private String cabinet;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getCabinet() {
        return cabinet;
    }

    public void setCabinet(String cabinet) {
        this.cabinet = cabinet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson lesson = (Lesson) o;

        if (!name.equals(lesson.name)) return false;
        if (!Objects.equals(teacher, lesson.teacher))
            return false;
        return Objects.equals(cabinet, lesson.cabinet);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (teacher != null ? teacher.hashCode() : 0);
        result = 31 * result + (cabinet != null ? cabinet.hashCode() : 0);
        return result;
    }
}
