package com.example.schedule.repo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.schedule.ScheduleDBHelper;
import com.example.schedule.entity.Flow;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FlowRepo {
    private final ScheduleDBHelper dbHelper;

    public FlowRepo(Context context) {
        this.dbHelper = new ScheduleDBHelper(context);
    }

    public FlowRepo(ScheduleDBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long add(int flowLvl, int course, int flow, int subgroup) {
        return add(
                flowLvl, course, flow, subgroup,
                LocalDateTime.of(1970, 1, 1, 0, 0, 0),
                LocalDate.of(1970, 1, 1),
                LocalDate.of(2970, 1, 1),
                LocalDate.of(2970, 1, 1), true
        );
    }

    public long add(
            int flowLvl, int course, int flow, int subgroup, LocalDateTime lastEdit,
            LocalDate lessonsStartDate, LocalDate sessionStartDate, LocalDate sessionEndDate,
            boolean active
    ) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("flow_lvl", flowLvl);
        values.put("course", course);
        values.put("flow", flow);
        values.put("subgroup", subgroup);
        values.put(
                "last_edit",
                lastEdit.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
        values.put(
                "lessons_start_date",
                lessonsStartDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
        values.put(
                "session_start_date",
                sessionStartDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
        values.put(
                "session_end_date",
                sessionEndDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
        values.put("active", active);

        long id = db.insert("flow", null, values);
        db.close();
        return id;
    }

    @SuppressLint("Range")
    public Flow findById(long id) {
        Flow result = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM flow WHERE id=?;";
        String[] selectionArgs = new String[] {String.valueOf(id)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            int flowLvl = cursor.getInt(cursor.getColumnIndex("flow_lvl"));
            int course = cursor.getInt(cursor.getColumnIndex("course"));
            int flow = cursor.getInt(cursor.getColumnIndex("flow"));
            int subgroup = cursor.getInt(cursor.getColumnIndex("subgroup"));
            LocalDateTime lastEdit = LocalDateTime.parse(
                    cursor.getString(cursor.getColumnIndex("last_edit")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            );
            LocalDate lessonsStartDate = LocalDate.parse(
                    cursor.getString(cursor.getColumnIndex("lessons_start_date")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
            );
            LocalDate sessionStartDate = LocalDate.parse(
                    cursor.getString(cursor.getColumnIndex("session_start_date")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
            );
            LocalDate sessionEndDate = LocalDate.parse(
                    cursor.getString(cursor.getColumnIndex("session_end_date")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
            );

            result = new Flow();
            result.setId(id);
            result.setFlowLvl(flowLvl);
            result.setCourse(course);
            result.setFlow(flow);
            result.setSubgroup(subgroup);
            result.setLastEdit(lastEdit);
            result.setLessonsStartDate(lessonsStartDate);
            result.setSessionStartDate(sessionStartDate);
            result.setSessionEndDate(sessionEndDate);
        }
        cursor.close();
        db.close();
        return result;
    }

    @SuppressLint("Range")
    public Flow findByFlowLvlAndCourseAndFlowAndSubgroup(
            int flowLvl, int course, int flow, int subgroup
    ) {
        Flow result = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM flow " +
                "WHERE flow_lvl=? AND course=? AND flow=? AND subgroup=?;";
        String[] selectionArgs = new String[] {
                String.valueOf(flowLvl), String.valueOf(course), String.valueOf(flow),
                String.valueOf(subgroup)
        };
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            LocalDateTime lastEdit = LocalDateTime.parse(
                    cursor.getString(cursor.getColumnIndex("last_edit")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            );
            LocalDate lessonsStartDate = LocalDate.parse(
                    cursor.getString(cursor.getColumnIndex("lessons_start_date")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
            );
            LocalDate sessionStartDate = LocalDate.parse(
                    cursor.getString(cursor.getColumnIndex("session_start_date")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
            );
            LocalDate sessionEndDate = LocalDate.parse(
                    cursor.getString(cursor.getColumnIndex("session_end_date")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
            );

            result = new Flow();
            result.setId(id);
            result.setFlowLvl(flowLvl);
            result.setCourse(course);
            result.setFlow(flow);
            result.setSubgroup(subgroup);
            result.setLastEdit(lastEdit);
            result.setLessonsStartDate(lessonsStartDate);
            result.setSessionStartDate(sessionStartDate);
            result.setSessionEndDate(sessionEndDate);
        }
        cursor.close();
        db.close();
        return result;
    }

    @SuppressLint("Range")
    public List<Flow> findAllByFlowLvl(int flowLvl) {
        List<Flow> result = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM flow WHERE flow_lvl=?;";
        String[] selectionArgs = new String[] {String.valueOf(flowLvl)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            int course = cursor.getInt(cursor.getColumnIndex("course"));
            int flow = cursor.getInt(cursor.getColumnIndex("flow"));
            int subgroup = cursor.getInt(cursor.getColumnIndex("subgroup"));
            LocalDateTime lastEdit = LocalDateTime.parse(
                    cursor.getString(cursor.getColumnIndex("last_edit")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            );
            LocalDate lessonsStartDate = LocalDate.parse(
                    cursor.getString(cursor.getColumnIndex("lessons_start_date")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
            );
            LocalDate sessionStartDate = LocalDate.parse(
                    cursor.getString(cursor.getColumnIndex("session_start_date")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
            );
            LocalDate sessionEndDate = LocalDate.parse(
                    cursor.getString(cursor.getColumnIndex("session_end_date")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
            );

            Flow foundFlow = new Flow();
            foundFlow.setId(id);
            foundFlow.setFlowLvl(flowLvl);
            foundFlow.setCourse(course);
            foundFlow.setFlow(flow);
            foundFlow.setSubgroup(subgroup);
            foundFlow.setLastEdit(lastEdit);
            foundFlow.setLessonsStartDate(lessonsStartDate);
            foundFlow.setSessionStartDate(sessionStartDate);
            foundFlow.setSessionEndDate(sessionEndDate);

            result.add(foundFlow);
        }
        cursor.close();
        db.close();
        return result;
    }

    @SuppressLint("Range")
    public List<Flow> findAllByFlowLvlAndCourse(int flowLvl, int course) {
        List<Flow> result = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM flow WHERE flow_lvl=? AND course=?;";
        String[] selectionArgs = new String[] {String.valueOf(flowLvl), String.valueOf(course)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            int flow = cursor.getInt(cursor.getColumnIndex("flow"));
            int subgroup = cursor.getInt(cursor.getColumnIndex("subgroup"));
            LocalDateTime lastEdit = LocalDateTime.parse(
                    cursor.getString(cursor.getColumnIndex("last_edit")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            );
            LocalDate lessonsStartDate = LocalDate.parse(
                    cursor.getString(cursor.getColumnIndex("lessons_start_date")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
            );
            LocalDate sessionStartDate = LocalDate.parse(
                    cursor.getString(cursor.getColumnIndex("session_start_date")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
            );
            LocalDate sessionEndDate = LocalDate.parse(
                    cursor.getString(cursor.getColumnIndex("session_end_date")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
            );

            Flow foundFlow = new Flow();
            foundFlow.setId(id);
            foundFlow.setFlowLvl(flowLvl);
            foundFlow.setCourse(course);
            foundFlow.setFlow(flow);
            foundFlow.setSubgroup(subgroup);
            foundFlow.setLastEdit(lastEdit);
            foundFlow.setLessonsStartDate(lessonsStartDate);
            foundFlow.setSessionStartDate(sessionStartDate);
            foundFlow.setSessionEndDate(sessionEndDate);

            result.add(foundFlow);
        }
        cursor.close();
        db.close();
        return result;
    }

    @SuppressLint("Range")
    public List<Flow> findAllByFlowLvlAndCourseAndFlow(int flowLvl, int course, int flow) {
        List<Flow> result = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM flow WHERE flow_lvl=? AND course=? AND flow=?;";
        String[] selectionArgs = new String[] {
                String.valueOf(flowLvl), String.valueOf(course), String.valueOf(flow)
        };
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            int subgroup = cursor.getInt(cursor.getColumnIndex("subgroup"));
            LocalDateTime lastEdit = LocalDateTime.parse(
                    cursor.getString(cursor.getColumnIndex("last_edit")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            );
            LocalDate lessonsStartDate = LocalDate.parse(
                    cursor.getString(cursor.getColumnIndex("lessons_start_date")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
            );
            LocalDate sessionStartDate = LocalDate.parse(
                    cursor.getString(cursor.getColumnIndex("session_start_date")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
            );
            LocalDate sessionEndDate = LocalDate.parse(
                    cursor.getString(cursor.getColumnIndex("session_end_date")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
            );

            Flow foundFlow = new Flow();
            foundFlow.setId(id);
            foundFlow.setFlowLvl(flowLvl);
            foundFlow.setCourse(course);
            foundFlow.setFlow(flow);
            foundFlow.setSubgroup(subgroup);
            foundFlow.setLastEdit(lastEdit);
            foundFlow.setLessonsStartDate(lessonsStartDate);
            foundFlow.setSessionStartDate(sessionStartDate);
            foundFlow.setSessionEndDate(sessionEndDate);

            result.add(foundFlow);
        }
        cursor.close();
        db.close();
        return result;
    }

    @SuppressLint("Range")
    public List<Integer> findDistinctCourseByFlowLvl(int flowLvl) {
        List<Integer> result = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT DISTINCT course FROM flow WHERE flow_lvl=?;";
        String[] selectionArgs = new String[] {String.valueOf(flowLvl)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()) {
            int course = cursor.getInt(cursor.getColumnIndex("course"));
            result.add(course);
        }
        cursor.close();
        db.close();
        return result;
    }

    @SuppressLint("Range")
    public List<Integer> findDistinctFlowByFlowLvlAndCourse(int flowLvl, int course) {
        List<Integer> result = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT DISTINCT flow FROM flow WHERE flow_lvl=? AND course=?;";
        String[] selectionArgs = new String[] {String.valueOf(flowLvl), String.valueOf(course)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()) {
            int flow = cursor.getInt(cursor.getColumnIndex("flow"));
            result.add(flow);
        }
        cursor.close();
        db.close();
        return result;
    }

    @SuppressLint("Range")
    public List<Integer> findDistinctSubgroupByFlowLvlAndCourseAndFlow(
            int flowLvl, int course, int flow
    ) {
        List<Integer> result = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT DISTINCT subgroup FROM flow WHERE flow_lvl=? AND course=? AND flow=?;";
        String[] selectionArgs = new String[] {
                String.valueOf(flowLvl), String.valueOf(course), String.valueOf(flow)
        };
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()) {
            int subgroup = cursor.getInt(cursor.getColumnIndex("subgroup"));
            result.add(subgroup);
        }
        cursor.close();
        db.close();
        return result;
    }
}
