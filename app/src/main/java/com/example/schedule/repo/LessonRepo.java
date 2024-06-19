package com.example.schedule.repo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.schedule.ScheduleDBHelper;
import com.example.schedule.entity.Lesson;

public class LessonRepo {
    private final ScheduleDBHelper dbHelper;

    public LessonRepo(Context context) {
        this.dbHelper = new ScheduleDBHelper(context);
    }

    public LessonRepo(ScheduleDBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long add(String name, String teacher, String cabinet) {
        if (name == null) return -1;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("teacher", teacher == null ? "" : teacher);
        values.put("cabinet", cabinet == null ? "" : cabinet);

        long id = db.insert("lesson", null, values);
        db.close();
        return id;
    }

    @SuppressLint("Range")
    public Lesson findById(long id) {
        Lesson result = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM lesson WHERE id=?;";
        String[] selectionArgs = new String[] {String.valueOf(id)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String teacher = cursor.getString(cursor.getColumnIndex("teacher"));
            String cabinet = cursor.getString(cursor.getColumnIndex("cabinet"));

            result = new Lesson();
            result.setId(id);
            result.setName(name);
            result.setTeacher(teacher);
            result.setCabinet(cabinet);
        }
        cursor.close();
        db.close();
        return result;
    }

    @SuppressLint("Range")
    public Lesson findByNameAndTeacherAndCabinet(String name, String teacher, String cabinet) {
        Lesson result = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM lesson" +
                " WHERE name=? AND teacher=? AND cabinet=?;";
        String[] selectionArgs = new String[] {name, teacher, cabinet};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndex("id"));

            result = new Lesson();
            result.setId(id);
            result.setName(name);
            result.setTeacher(teacher);
            result.setCabinet(cabinet);
        }
        cursor.close();
        db.close();
        return result;
    }
}
