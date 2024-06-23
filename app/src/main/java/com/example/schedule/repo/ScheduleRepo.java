package com.example.schedule.repo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.schedule.ScheduleDBHelper;
import com.example.schedule.entity.Flow;
import com.example.schedule.entity.Lesson;
import com.example.schedule.entity.Schedule;

import java.util.ArrayList;
import java.util.List;

public class ScheduleRepo {
    private final ScheduleDBHelper dbHelper;
    private final FlowRepo flowRepo;
    private final LessonRepo lessonRepo;

    public ScheduleRepo(Context context) {
        this.dbHelper = new ScheduleDBHelper(context);
        this.flowRepo = new FlowRepo(context);
        this.lessonRepo = new LessonRepo(context);
    }

    public ScheduleRepo(ScheduleDBHelper dbHelper, FlowRepo flowRepo, LessonRepo lessonRepo) {
        this.dbHelper = dbHelper;
        this.flowRepo = flowRepo;
        this.lessonRepo = lessonRepo;
    }

    public long add(
            long flow, long lesson, int dayOfWeek, int lessonNum, boolean numerator
    ) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("flow", flow);
        values.put("lesson", lesson);
        values.put("day_of_week", dayOfWeek);
        values.put("lesson_num", lessonNum);
        values.put("numerator", numerator ? "1" : "0");

        long id = db.insert("schedule", null, values);
        db.close();
        return id;
    }

    public long add(
            int flowLvl, int course, int flow, int subgroup,
            String name, String teacher, String cabinet,
            int dayOfWeek, int lessonNum, boolean numerator
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

        return add(
                flowId, lessonId, dayOfWeek, lessonNum, numerator
        );
    }

    public int update(
            long flow, long lesson, int dayOfWeek, int lessonNum, boolean numerator
    ) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("lesson", lesson);

        String whereClause = "flow=? AND day_of_week=? AND lesson_num=? AND numerator=?";
        String[] whereArgs = new String[] {
                String.valueOf(flow), String.valueOf(dayOfWeek), String.valueOf(lessonNum),
                numerator ? "1" : "0"
        };

        int count = db.update("schedule", values, whereClause, whereArgs);
        db.close();
        return count;
    }

    public int update(
            int flowLvl, int course, int flow, int subgroup,
            String name, String teacher, String cabinet,
            int dayOfWeek, int lessonNum, boolean numerator
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

        return update(flowId, lessonId, dayOfWeek, lessonNum, numerator);
    }

    public void addOrUpdate(
            long flow, long lesson, int dayOfWeek, int lessonNum, boolean numerator
    ) {
        Schedule foundSchedule = findByFlowAndDayOfWeekAndLessonNumAndNumerator(
                flow, dayOfWeek, lessonNum, numerator
        );
        if (foundSchedule != null) update(flow, lesson, dayOfWeek, lessonNum, numerator);
        else add(flow, lesson, dayOfWeek, lessonNum, numerator);
    }

    public void addOrUpdate(
            int flowLvl, int course, int flow, int subgroup,
            String name, String teacher, String cabinet,
            int dayOfWeek, int lessonNum, boolean numerator
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

        addOrUpdate(flowId, lessonId, dayOfWeek, lessonNum, numerator);
    }

    @SuppressLint("Range")
    public Schedule findByFlowAndDayOfWeekAndLessonNumAndNumerator(
            long flow, int dayOfWeek, int lessonNum, boolean numerator
    ) {
        Schedule result = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM schedule " +
                "WHERE flow=? AND day_of_week=? AND lesson_num=? AND numerator=?;";
        String[] selectionArgs = new String[] {
                String.valueOf(flow), String.valueOf(dayOfWeek), String.valueOf(lessonNum),
                numerator ? "1" : "0"
        };
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            long lesson = cursor.getLong(cursor.getColumnIndex("lesson"));

            result = new Schedule();
            result.setId(id);
            result.setFlow(flow);
            result.setLesson(lesson);
            result.setDayOfWeek(dayOfWeek);
            result.setLessonNum(lessonNum);
            result.setNumerator(numerator);
        }
        cursor.close();
        db.close();
        return result;
    }

    public Schedule findByFlowAndDayOfWeekAndLessonNumAndNumerator(
            int flowLvl, int course, int flow, int subgroup,
            int dayOfWeek, int lessonNum, boolean numerator
    ) {
        Flow foundFlow = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
                flowLvl, course, flow, subgroup
        );
        if (foundFlow == null) return null;

        long flowId = foundFlow.getId();

        return findByFlowAndDayOfWeekAndLessonNumAndNumerator(
                flowId, dayOfWeek, lessonNum, numerator
        );
    }

    @SuppressLint("Range")
    public List<Schedule> findAllByFlow(long flow) {
        List<Schedule> result = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM schedule WHERE flow=?;";
        String[] selectionArgs = new String[] {String.valueOf(flow)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            long lesson = cursor.getLong(cursor.getColumnIndex("lesson"));
            int dayOfWeek = cursor.getInt(cursor.getColumnIndex("day_of_week"));
            int lessonNum = cursor.getInt(cursor.getColumnIndex("lesson_num"));
            boolean numerator = cursor.getInt(cursor.getColumnIndex("numerator")) == 1;

            Schedule schedule = new Schedule();
            schedule.setId(id);
            schedule.setFlow(flow);
            schedule.setLesson(lesson);
            schedule.setDayOfWeek(dayOfWeek);
            schedule.setLessonNum(lessonNum);
            schedule.setNumerator(numerator);

            result.add(schedule);
        }
        cursor.close();
        db.close();
        return result;
    }

    public List<Schedule> findAllByFlow(int flowLvl, int course, int flow, int subgroup) {
        Flow foundFlow = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
                flowLvl, course, flow, subgroup
        );
        if (foundFlow == null) return null;

        long flowId = foundFlow.getId();

        return findAllByFlow(flowId);
    }

    public int deleteByFlow(long flow) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = "flow=?";
        String[] whereArgs = new String[] {String.valueOf(flow)};

        int count = db.delete("schedule", whereClause, whereArgs);
        db.close();
        return count;
    }

    public int deleteByFlow(int flowLvl, int course, int flow, int subgroup) {
        Flow foundFlow = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
                flowLvl, course, flow, subgroup
        );
        if (foundFlow == null) return 0;

        long flowId = foundFlow.getId();

        return deleteByFlow(flowId);
    }

    public int deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
            long flow, int dayOfWeek, int lessonNum, boolean numerator
    ) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = "flow=? AND day_of_week=? AND lesson_num=? AND numerator=?";
        String[] whereArgs = new String[] {
                String.valueOf(flow), String.valueOf(dayOfWeek), String.valueOf(lessonNum),
                numerator ? "1" : "0"
        };

        int count = db.delete("schedule", whereClause, whereArgs);
        db.close();
        return count;
    }

    public int deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
            int flowLvl, int course, int flow, int subgroup,
            int dayOfWeek, int lessonNum, boolean numerator
    ) {
        Flow foundFlow = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
                flowLvl, course, flow, subgroup
        );
        if (foundFlow == null) return 0;

        long flowId = foundFlow.getId();

        return deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
                flowId, dayOfWeek, lessonNum, numerator
        );
    }
}
