package com.example.schedule;

import java.util.Objects;

public class LessonStruct {
    public String name;
    public String teacher;
    public String cabinet;

    public LessonStruct(String name, String teacher, String cabinet) {
        this.name = name;
        this.teacher = teacher;
        this.cabinet = cabinet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonStruct that = (LessonStruct) o;
        return Objects.equals(name, that.name) && Objects.equals(teacher, that.teacher)
                && Objects.equals(cabinet, that.cabinet);
    }
}
