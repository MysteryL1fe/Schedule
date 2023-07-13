package com.example.schedule;

public class LessonStruct {
    public final String lessonName;
    public final String cabinet;
    public final String surname;
    public final String teacherName;
    public final String patronymic;
    public final boolean willLessonBe;

    public LessonStruct(String lessonName, String cabinet, String surname, String teacherName,
                        String patronymic) {
        this(lessonName, cabinet, surname, teacherName, patronymic, true);
    }

    public LessonStruct(String lessonName, String cabinet, String surname, String teacherName,
                        String patronymic, boolean willLessonBe) {
        this.lessonName = lessonName;
        this.cabinet = cabinet;
        this.surname = surname;
        this.teacherName = teacherName;
        this.patronymic = patronymic;
        this.willLessonBe = willLessonBe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonStruct that = (LessonStruct) o;

        return lessonName.equals(that.lessonName) && cabinet.equals(that.cabinet)
                && surname.equals(that.surname) && teacherName.equals(that.teacherName)
                && patronymic.equals(that.patronymic) && willLessonBe == that.willLessonBe;
    }
}
