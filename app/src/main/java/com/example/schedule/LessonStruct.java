package com.example.schedule;

import java.util.Objects;

public class LessonStruct {
    public String name;
    public String teacher;
    public String cabinet;

    public String toExport() {
        return String.format("%s*%s*%s", name.isEmpty() ? "_" : name,
                teacher.isEmpty() ? "_" : teacher, cabinet.isEmpty() ? "_" : cabinet);
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
