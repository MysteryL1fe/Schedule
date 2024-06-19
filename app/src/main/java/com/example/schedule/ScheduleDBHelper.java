package com.example.schedule;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScheduleDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "schedule";

    public ScheduleDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS flow (" +
                "id integer PRIMARY KEY AUTOINCREMENT, " +
                "flow_lvl integer NOT NULL CHECK (flow_lvl >= 1 AND flow_lvl <= 3), " +
                "course integer NOT NULL CHECK (course > 0 AND course <= 5), " +
                "flow integer NOT NULL CHECK (flow > 0), " +
                "subgroup integer NOT NULL CHECK (subgroup > 0), " +
                "last_edit text NOT NULL, " +
                "lessons_start_date text NOT NULL, " +
                "session_start_date text NOT NULL, " +
                "session_end_date text NOT NULL, " +
                "active boolean NOT NULL, " +
                "UNIQUE (flow_lvl, course, flow, subgroup)" +
                ");";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS lesson (" +
                "id integer PRIMARY KEY AUTOINCREMENT, " +
                "name text NOT NULL, " +
                "teacher text, " +
                "cabinet text, " +
                "UNIQUE (name, teacher, cabinet)" +
                ");";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS schedule (" +
                "id integer PRIMARY KEY AUTOINCREMENT, " +
                "flow integer NOT NULL, " +
                "lesson integer NOT NULL, " +
                "day_of_week integer NOT NULL CHECK (day_of_week >= 1 AND day_of_week <= 7), " +
                "lesson_num integer NOT NULL CHECK (lesson_num >= 1 AND lesson_num <= 8), " +
                "numerator integer NOT NULL, " +
                "FOREIGN KEY (flow) REFERENCES flow(id), " +
                "FOREIGN KEY (lesson) REFERENCES lesson(id), " +
                "UNIQUE (flow, day_of_week, lesson_num, numerator)" +
                ");";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS homework (" +
                "id integer PRIMARY KEY AUTOINCREMENT, " +
                "homework text NOT NULL, " +
                "lesson_date text NOT NULL, " +
                "lesson_num integer NOT NULL CHECK (lesson_num >= 1 AND lesson_num <= 8), " +
                "flow integer NOT NULL, " +
                "lesson_name text NOT NULL, " +
                "FOREIGN KEY (flow) REFERENCES flow(id), " +
                "UNIQUE (lesson_date, lesson_num, flow)" +
                ");";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS temp_schedule (" +
                "id integer PRIMARY KEY AUTOINCREMENT, " +
                "flow integer NOT NULL, " +
                "lesson integer NOT NULL, " +
                "lesson_date text NOT NULL, " +
                "lesson_num integer NOT NULL CHECK (lesson_num >= 1 AND lesson_num <= 8), " +
                "will_lesson_be integer NOT NULL, " +
                "FOREIGN KEY (flow) REFERENCES flow(id), " +
                "FOREIGN KEY (lesson) REFERENCES lesson(id), " +
                "UNIQUE (flow, lesson_date, lesson_num)" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS temp_schedule;");
        db.execSQL("DROP TABLE IF EXISTS homework;");
        db.execSQL("DROP TABLE IF EXISTS schedule;");
        db.execSQL("DROP TABLE IF EXISTS lesson;");
        db.execSQL("DROP TABLE IF EXISTS teacher;");
        db.execSQL("DROP TABLE IF EXISTS flow;");

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS temp_schedule;");
        db.execSQL("DROP TABLE IF EXISTS homework;");
        db.execSQL("DROP TABLE IF EXISTS schedule;");
        db.execSQL("DROP TABLE IF EXISTS lesson;");
        db.execSQL("DROP TABLE IF EXISTS teacher;");
        db.execSQL("DROP TABLE IF EXISTS flow;");

        onCreate(db);
    }
}
