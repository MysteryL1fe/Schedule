package com.example.schedule.repo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.schedule.ScheduleDBHelper;
import com.example.schedule.entity.Flow;
import com.example.schedule.entity.Lesson;
import com.example.schedule.entity.TempSchedule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TempScheduleRepo {
    private final ScheduleDBHelper dbHelper;
    private final FlowRepo flowRepo;
    private final LessonRepo lessonRepo;

    public TempScheduleRepo(Context context) {
        this.dbHelper = new ScheduleDBHelper(context);
        this.flowRepo = new FlowRepo(context);
        this.lessonRepo = new LessonRepo(context);
    }

    public TempScheduleRepo(ScheduleDBHelper dbHelper, FlowRepo flowRepo, LessonRepo lessonRepo) {
        this.dbHelper = dbHelper;
        this.flowRepo = flowRepo;
        this.lessonRepo = lessonRepo;
    }

    public long add(
            long flow, long lesson, LocalDate lessonDate, int lessonNum, boolean willLessonBe
    ) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("flow", flow);
        values.put("lesson", lesson);
        values.put(
                "lesson_date",
                lessonDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
        values.put("lesson_num", lessonNum);
        values.put("will_lesson_be", willLessonBe ? "1" : "0");

        long id = db.insert("temp_schedule", null, values);
        db.close();
        return id;
    }

    public long add(
            int flowLvl, int course, int flow, int subgroup,
            String name, String teacher, String cabinet,
            LocalDate lessonDate, int lessonNum, boolean willLessonBe
    ) {
        long flowId = -1;
        Flow foundFlow = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
                flowLvl, course, flow, subgroup
        );
        if (foundFlow != null) flowId = foundFlow.getId();
        else flowId = flowRepo.add(flowLvl, course, flow, subgroup);

        long lessonId = -1;
        Lesson foundLesson = lessonRepo.findByNameAndTeacherAndCabinet(
                name, teacher, cabinet
        );
        if (foundLesson != null) lessonId = foundLesson.getId();
        else lessonId = lessonRepo.add(name, teacher, cabinet);

        return add(flowId, lessonId, lessonDate, lessonNum, willLessonBe);
    }

    public int update(
            long flow, long lesson, LocalDate lessonDate, int lessonNum, boolean willLessonBe
    ) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("lesson", lesson);
        values.put("will_lesson_be", willLessonBe ? "1" : "0");

        String whereClause = "flow=? AND lesson_date=? AND lesson_num=?";
        String[] whereArgs = new String[] {
                String.valueOf(flow), lessonDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                String.valueOf(lessonNum)
        };

        int count = db.update("temp_lesson", values, whereClause, whereArgs);
        db.close();
        return count;
    }

    public int update(
            int flowLvl, int course, int flow, int subgroup,
            String name, String teacher, String cabinet,
            LocalDate lessonDate, int lessonNum, boolean willLessonBe
    ) {
        long flowId = -1;
        Flow foundFlow = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
                flowLvl, course, flow, subgroup
        );
        if (foundFlow != null) flowId = foundFlow.getId();
        else flowId = flowRepo.add(flowLvl, course, flow, subgroup);

        long lessonId = -1;
        Lesson foundLesson = lessonRepo.findByNameAndTeacherAndCabinet(
                name, teacher, cabinet
        );
        if (foundLesson != null) lessonId = foundLesson.getId();
        else lessonId = lessonRepo.add(name, teacher, cabinet);

        return update(flowId, lessonId, lessonDate, lessonNum, willLessonBe);
    }

    public void addOrUpdate(
            long flow, long lesson, LocalDate lessonDate, int lessonNum, boolean willLessonBe
    ) {
        TempSchedule foundSchedule = findByFlowAndLessonDateAndLessonNum(
                flow, lessonDate, lessonNum
        );
        if (foundSchedule != null) update(flow, lesson, lessonDate, lessonNum, willLessonBe);
        else add(flow, lesson, lessonDate, lessonNum, willLessonBe);
    }

    public void addOrUpdate(
            int flowLvl, int course, int flow, int subgroup,
            String name, String teacher, String cabinet,
            LocalDate lessonDate, int lessonNum, boolean willLessonBe
    ) {
        long flowId = -1;
        Flow foundFlow = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
                flowLvl, course, flow, subgroup
        );
        if (foundFlow != null) flowId = foundFlow.getId();
        else flowId = flowRepo.add(flowLvl, course, flow, subgroup);

        long lessonId = -1;
        Lesson foundLesson = lessonRepo.findByNameAndTeacherAndCabinet(
                name, teacher, cabinet
        );
        if (foundLesson != null) lessonId = foundLesson.getId();
        else lessonId = lessonRepo.add(name, teacher, cabinet);

        addOrUpdate(flowId, lessonId, lessonDate, lessonNum, willLessonBe);
    }

    @SuppressLint("Range")
    public TempSchedule findByFlowAndLessonDateAndLessonNum(
            long flow, LocalDate lessonDate, int lessonNum
    ) {
        TempSchedule result = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM temp_schedule " +
                "WHERE flow=? AND lesson_date=? AND lesson_num=?;";
        String[] selectionArgs = new String[] {
                String.valueOf(flow), lessonDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                String.valueOf(lessonNum)
        };
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            long lesson = cursor.getLong(cursor.getColumnIndex("lesson"));
            boolean willLessonBe = cursor.getInt(
                    cursor.getColumnIndex("will_lesson_be")
            ) == 1;

            result = new TempSchedule();
            result.setId(id);
            result.setFlow(flow);
            result.setLesson(lesson);
            result.setLessonDate(lessonDate);
            result.setLessonNum(lessonNum);
            result.setWillLessonBe(willLessonBe);
        }
        cursor.close();
        db.close();
        return result;
    }

    @SuppressLint("Range")
    public TempSchedule findByFlowAndLessonDateAndLessonNum(
            int flowLvl, int course, int flow, int subgroup, LocalDate lessonDate, int lessonNum
    ) {
        Flow foundFlow = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
                flowLvl, course, flow, subgroup
        );
        if (foundFlow == null) return null;

        long flowId = foundFlow.getId();

        return findByFlowAndLessonDateAndLessonNum(flowId, lessonDate, lessonNum);
    }

    public int deleteByFlowAndLessonDateAndLessonNum(
            long flow, LocalDate lessonDate, int lessonNum
    ) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = "flow=? AND lesson_date=? AND lesson_num=?";
        String[] whereArgs = new String[] {
                String.valueOf(flow), lessonDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                String.valueOf(lessonNum)
        };

        int count = db.delete("temp_schedule", whereClause, whereArgs);
        db.close();
        return count;
    }

    public int deleteByFlowAndLessonDateAndLessonNum(
            int flowLvl, int course, int flow, int subgroup, LocalDate lessonDate, int lessonNum
    ) {
        Flow foundFlow = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
                flowLvl, course, flow, subgroup
        );
        if (foundFlow == null) return 0;

        long flowId = foundFlow.getId();

        return deleteByFlowAndLessonDateAndLessonNum(flowId, lessonDate, lessonNum);
    }

    public int deleteAllBeforeDate(LocalDate date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = "lesson_date<?";
        String[] whereArgs = new String[] {
                date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        };

        int count = db.delete("temp_schedule", whereClause, whereArgs);
        db.close();
        return count;
    }
}