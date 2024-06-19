package com.example.schedule;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScheduleDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "schedule";

    public static final String KEY_ID = "id";
    public static final String KEY_FLOW = "flow";
    public static final String KEY_LESSON = "lesson";
    public static final String KEY_NAME = "name";
    public static final String KEY_LESSON_NUM = "lesson_num";
    public static final String KEY_YEAR = "year";
    public static final String KEY_MONTH = "month";
    public static final String KEY_DAY = "day";

    public static final String TEACHER_TABLE_NAME = "teacher";
    public static final String KEY_SURNAME = "surname";
    public static final String KEY_PATRONYMIC = "patronymic";

    public static final String FLOW_TABLE_NAME = "flow";
    public static final String KEY_FLOW_LVL = "flow_lvl";
    public static final String KEY_COURSE = "course";
    public static final String KEY_GROUP = "flow";
    public static final String KEY_SUBGROUP = "subgroup";

    public static final String LESSON_TABLE_NAME = "lesson";
    public static final String KEY_TEACHER = "teacher";
    public static final String KEY_CABINET = "cabinet";

    public static final String SCHEDULE_TABLE_NAME = "schedule";
    public static final String KEY_DAY_OF_WEEK = "day_of_week";
    public static final String KEY_IS_NUMERATOR = "is_numerator";

    public static final String HOMEWORK_TABLE_NAME = "homework";
    public static final String KEY_HOMEWORK = "homework";

    public static final String TEMP_SCHEDULE_TABLE_NAME = "temp_schedule";
    public static final String KEY_WILL_LESSON_BE = "will_lesson_be";
    public static final String KEY_LESSON_NAME = "lesson_name";

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

    /*@SuppressLint("Range")
    public LessonStruct getLesson(int flowLvl, int course, int group, int subgroup,
                                  int dayOfWeek, int lessonNum, boolean isNumerator) {
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT l.name, l.teacher, l.cabinet " +
                "FROM schedule s " +
                "JOIN flow f ON s.flow = f.id " +
                "JOIN lesson l ON s.lesson = l.id " +
                "WHERE f.flow_lvl=? AND f.course=? AND f.flow=? AND f.subgroup=? " +
                "AND s.day_of_week=? AND s.lesson_num=? AND s.numerator=?;";
        String[] selectionArgs = new String[] {
                String.valueOf(flowLvl), String.valueOf(course), String.valueOf(group),
                String.valueOf(subgroup), String.valueOf(dayOfWeek), String.valueOf(lessonNum),
                String.valueOf(isNumerator)
        };
        Cursor cursor = database.rawQuery(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            String lessonName = cursor.getString(cursor.getColumnIndex("name"));
            String teacher = cursor.getString(cursor.getColumnIndex("teacher"));
            String cabinet = cursor.getString(cursor.getColumnIndex("cabinet"));
            cursor.close();
            database.close();
            return new LessonStruct(lessonName, teacher, cabinet);
        }
        cursor.close();
        database.close();
        return null;
    }

    public void addOrUpdateSchedule(int flowLvl, int course, int group, int subgroup,
                                    int dayOfWeek, int lessonNum, boolean isNumerator,
                                    String lessonName, String teacher, String cabinet) {
        int flowId = getFlowId(flowLvl, course, group, subgroup);
        addOrUpdateSchedule(
                flowId, dayOfWeek, lessonNum, isNumerator, lessonName, teacher, cabinet
        );
    }

    @SuppressLint("Range")
    private void addOrUpdateSchedule(int flowId, int dayOfWeek, int lessonNum, boolean isNumerator,
                                     String lessonName, String teacher, String cabinet) {
        if (dayOfWeek < 1 || dayOfWeek > 7 || lessonNum < 1 || lessonNum > 8 || flowId == -1
                || lessonName == null) {
            return;
        }
        if (cabinet == null) cabinet = "";
        if (teacher == null) teacher = "";

        lessonName = lessonName.replaceAll("\"", "").trim();
        teacher = teacher.replaceAll("\"", "").trim();
        cabinet = cabinet.replaceAll("\"", "").trim();

        if (lessonName.isEmpty()) return;

        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            int lessonId = -1, scheduleId = -1, oldLessonId = -1;

            // finding lesson id
            String sql = "SELECT id FROM lesson " +
                    "WHERE name=? AND teacher=? AND cabinet=?";
            String[] selectionArgs = new String[] {
                    lessonName, teacher, cabinet
            };
            Cursor cursor = database.rawQuery(sql, selectionArgs);

            if (cursor.moveToFirst()) {
                lessonId = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            }
            cursor.close();

            if (lessonId >= 0) {
                sql = "SELECT id FROM schedule " +
                        "WHERE flow=? AND lesson=? AND day_of_week=? " +
                        "AND lesson_num=? AND numerator=?";
                selectionArgs = new String[] {
                        String.valueOf(flowId), String.valueOf(lessonId),
                        String.valueOf(dayOfWeek), String.valueOf(lessonNum),
                        String.valueOf(isNumerator)
                };
                cursor = database.rawQuery(sql, selectionArgs);
                if (cursor.moveToFirst()) {
                    // nothing to change
                    database.setTransactionSuccessful();
                    return;
                }
                cursor.close();
            }

            // need we to add or to update
            columns = new String[] {KEY_ID, KEY_LESSON};
            selection = String.format(
                    "%s = %s AND %s = %s AND %s = %s AND %s = %s",
                    KEY_FLOW, flowId, KEY_DAY_OF_WEEK, dayOfWeek, KEY_LESSON_NUM, lessonNum,
                    KEY_IS_NUMERATOR, isNumerator ? 1 : 0
            );
            cursor = database.query(
                    SCHEDULE_TABLE_NAME, columns, selection, null,
                    null, null, null
            );
            if (cursor.moveToFirst()) {
                scheduleId = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                oldLessonId = cursor.getInt(cursor.getColumnIndex(KEY_LESSON));
            }
            cursor.close();

            ContentValues contentValues = new ContentValues();

            if (teacherId < 0) {
                // new teacher
                contentValues = new ContentValues();
                contentValues.put(KEY_SURNAME, teacherSurname);
                contentValues.put(KEY_NAME, teacherName);
                contentValues.put(KEY_PATRONYMIC, teacherPatronymic);
                database.insert(TEACHER_TABLE_NAME, null, contentValues);
                columns = new String[] {KEY_ID};
                selection = String.format(
                        "%s = \"%s\" AND %s = \"%s\" AND %s = \"%s\"", KEY_SURNAME, teacherSurname,
                        KEY_NAME, teacherName, KEY_PATRONYMIC, teacherPatronymic
                );
                cursor = database.query(
                        TEACHER_TABLE_NAME, columns, selection, null,
                        null, null, null
                );
                cursor.moveToFirst();
                teacherId = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                cursor.close();
                contentValues.clear();
            }

            if (lessonId < 0) {
                // new lesson
                contentValues = new ContentValues();
                contentValues.put(KEY_NAME, lessonName);
                contentValues.put(KEY_CABINET, cabinet);
                contentValues.put(KEY_TEACHER, teacherId);
                database.insert(LESSON_TABLE_NAME, null, contentValues);
                columns = new String[] {KEY_ID};
                selection = String.format(
                        "%s = \"%s\" AND %s = \"%s\" AND %s = %s", KEY_NAME, lessonName,
                        KEY_CABINET, cabinet, KEY_TEACHER, teacherId
                );
                cursor = database.query(
                        LESSON_TABLE_NAME, columns, selection, null,
                        null, null, null
                );
                cursor.moveToFirst();
                lessonId = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                cursor.close();
                contentValues.clear();
            }

            if (scheduleId >= 0) {
                // update schedule
                contentValues.put(KEY_LESSON, lessonId);
                database.update(
                        SCHEDULE_TABLE_NAME, contentValues,
                        KEY_ID + " = " + scheduleId, null
                );
                contentValues.clear();
            } else {
                // add schedule
                contentValues.put(KEY_FLOW, flowId);
                contentValues.put(KEY_DAY_OF_WEEK, dayOfWeek);
                contentValues.put(KEY_LESSON, lessonId);
                contentValues.put(KEY_LESSON_NUM, lessonNum);
                contentValues.put(KEY_IS_NUMERATOR, isNumerator ? 1 : 0);
                database.insert(SCHEDULE_TABLE_NAME, null, contentValues);
                contentValues.clear();
            }

            checkLesson(oldLessonId);

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    @SuppressLint("Range")
    public void deleteSchedule(int flowLvl, int course, int group, int subgroup,
                               int dayOfWeek, int lessonNum, boolean isNumerator) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            int flowId = getFlowId(flowLvl, course, group, subgroup);
            if (flowId >= 0) {
                int lessonId = -1;
                String[] columns = new String[] {KEY_LESSON};
                String scheduleSelection = String.format(
                        "%s = %s AND %s = %s AND %s = %s AND %s = %s", KEY_FLOW, flowId,
                        KEY_DAY_OF_WEEK, dayOfWeek, KEY_LESSON_NUM, lessonNum,
                        KEY_IS_NUMERATOR, isNumerator ? 1 : 0
                );
                Cursor cursor = database.query(
                        SCHEDULE_TABLE_NAME, columns, scheduleSelection, null,
                        null, null, null
                );
                if (cursor.moveToFirst()) {
                    lessonId = cursor.getInt(cursor.getColumnIndex(KEY_LESSON));
                    cursor.close();
                } else cursor.close();
                database.delete(SCHEDULE_TABLE_NAME, scheduleSelection, null);
                checkLesson(lessonId);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        database.close();
    }

    @SuppressLint("Range")
    private void deleteSchedule(int flowId) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            Set<Integer> lessonIds = new HashSet<>();
            String[] columns = new String[] {KEY_LESSON};
            String selection = String.format("%s = %s", KEY_FLOW, flowId);
            Cursor cursor = database.query(
                    SCHEDULE_TABLE_NAME, columns, selection, null,
                    null, null, null
            );
            if (cursor.moveToFirst()) {
                do {
                    lessonIds.add(cursor.getInt(cursor.getColumnIndex(KEY_LESSON)));
                } while (cursor.moveToNext());
            } else cursor.close();
            database.delete(SCHEDULE_TABLE_NAME, selection, null);

            for (int lessonId : lessonIds) {
                checkLesson(lessonId);
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        database.close();
    }

    @SuppressLint("Range")
    public boolean exportSchedule(int flowLvl, int course, int group, int subgroup) {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!dir.exists()) dir.mkdir();
        SQLiteDatabase database = getReadableDatabase();
        try {
            ArrayList<ExportValues> exportValues = new ArrayList<>();
            String lessonNameColumn = "lesson_name", teacherNameColumn = "teacher_name";
            String sql = "SELECT "
                    + SCHEDULE_TABLE_NAME + "." + KEY_DAY_OF_WEEK + ", "
                    + SCHEDULE_TABLE_NAME + "." + KEY_LESSON_NUM + ", "
                    + SCHEDULE_TABLE_NAME + "." + KEY_IS_NUMERATOR + ", "
                    + LESSON_TABLE_NAME + "." + KEY_NAME + " AS " + lessonNameColumn + ", "
                    + LESSON_TABLE_NAME + "." + KEY_CABINET + ", "
                    + TEACHER_TABLE_NAME + "." + KEY_SURNAME + ", "
                    + TEACHER_TABLE_NAME + "." + KEY_NAME + " AS " + teacherNameColumn + ", "
                    + TEACHER_TABLE_NAME + "." + KEY_PATRONYMIC
                    + " FROM " + SCHEDULE_TABLE_NAME
                    + " INNER JOIN " + FLOW_TABLE_NAME + " ON "+ SCHEDULE_TABLE_NAME + "."
                    + KEY_FLOW + " = " + FLOW_TABLE_NAME + "." + KEY_ID
                    + " INNER JOIN " + LESSON_TABLE_NAME + " ON " + SCHEDULE_TABLE_NAME + "."
                    + KEY_LESSON + " = " + LESSON_TABLE_NAME + "." + KEY_ID
                    + " INNER JOIN " + TEACHER_TABLE_NAME + " ON " + LESSON_TABLE_NAME + "."
                    + KEY_TEACHER + " = " + TEACHER_TABLE_NAME + "." + KEY_ID
                    + " WHERE "
                    + FLOW_TABLE_NAME + "." + KEY_FLOW_LVL + " = " + flowLvl + " AND "
                    + FLOW_TABLE_NAME + "." + KEY_COURSE + " = " + course + " AND "
                    + FLOW_TABLE_NAME + "." + KEY_GROUP + " = " + group + " AND "
                    + FLOW_TABLE_NAME + "." + KEY_SUBGROUP + " = " + subgroup + ";";
            Cursor cursor = database.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    exportValues.add(new ExportValues(
                            cursor.getInt(cursor.getColumnIndex(KEY_DAY_OF_WEEK)),
                            cursor.getInt(cursor.getColumnIndex(KEY_LESSON_NUM)),
                            cursor.getInt(cursor.getColumnIndex(KEY_IS_NUMERATOR)) == 1,
                            cursor.getString(cursor.getColumnIndex(lessonNameColumn)),
                            cursor.getString(cursor.getColumnIndex(KEY_CABINET)),
                            cursor.getString(cursor.getColumnIndex(KEY_SURNAME)),
                            cursor.getString(cursor.getColumnIndex(teacherNameColumn)),
                            cursor.getString(cursor.getColumnIndex(KEY_PATRONYMIC))
                    ));
                } while (cursor.moveToNext());
            } else {
                cursor.close();
                return false;
            }
            cursor.close();
            Gson gson = new Gson();
            Type exportValuesArrayListType = new TypeToken<ArrayList<ExportValues>>(){}.getType();
            String fileName = Utils.generateStr();
            File file = new File(dir, String.format("%s.sch", fileName));
            FileOutputStream outputStream = new FileOutputStream(file, false);
            outputStream.write(gson.toJson(exportValues, exportValuesArrayListType).getBytes());
            outputStream.flush();
            outputStream.close();
            database.close();
            return true;
        } catch (Exception e) {
            database.close();
            return false;
        }
    }

    public void importScheduleBefore29(String path, int flowLvl, int course, int group,
                                              int subgroup) {
        File file = new File(path);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            importSchedule(br.readLine(), flowLvl, course, group, subgroup);
            br.close();
        } catch (Exception ignored) {}
    }

    public void importScheduleAfter28(Uri uri, ContentResolver resolver,
                                             int flowLvl, int course, int group, int subgroup) {
        try (InputStream stream = resolver.openInputStream(uri)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            importSchedule(br.readLine(), flowLvl, course, group, subgroup);
            br.close();
        } catch (Exception ignored) {}
    }

    private void importSchedule(String schedule, int flowLvl, int course, int group,
                                       int subgroup) {
        Gson gson = new Gson();
        Type exportValuesArrayListType = new TypeToken<ArrayList<ExportValues>>(){}.getType();
        ArrayList<ExportValues> importedSchedule = gson.fromJson(
                schedule, exportValuesArrayListType
        );
        if (importedSchedule != null) {
            int flowId = getFlowId(flowLvl, course, group, subgroup);
            if (flowId == -1) {
                return;
            }
            deleteSchedule(flowId);

            for (ExportValues values : importedSchedule) {
                addOrUpdateSchedule(
                        flowId, values.dayOfWeek, values.lessonNum, values.isNumerator,
                        values.lessonName, values.cabinet, values.surname, values.teacherName,
                        values.patronymic
                );
            }
        }
    }

    @SuppressLint("Range")
    public ArrayList<Homework> getAllHomeworks(int flowLvl, int course, int group, int subgroup) {
        ArrayList<Homework> result = new ArrayList<>();

        int flowId = getFlowId(flowLvl, course, group, subgroup);
        if (flowId == -1) return result;

        SQLiteDatabase database = getReadableDatabase();
        String[] columns = new String[] {
                KEY_YEAR, KEY_MONTH, KEY_DAY, KEY_LESSON_NUM, KEY_HOMEWORK, KEY_LESSON_NAME
        };
        String selection = String.format("%s = %s", KEY_FLOW, flowId);
        Cursor cursor = database.query(
                HOMEWORK_TABLE_NAME, columns, selection, null,
                null, null, null
        );
        if (cursor.moveToFirst()) {
            do {
                int year = cursor.getInt(cursor.getColumnIndex(KEY_YEAR));
                int month = cursor.getInt(cursor.getColumnIndex(KEY_MONTH));
                int day = cursor.getInt(cursor.getColumnIndex(KEY_DAY));
                int lessonNum = cursor.getInt(cursor.getColumnIndex(KEY_LESSON_NUM));
                String homework = cursor.getString(cursor.getColumnIndex(KEY_HOMEWORK));
                String lessonName = cursor.getString(cursor.getColumnIndex(KEY_LESSON_NAME));
                result.add(new Homework(year, month, day, lessonNum, homework, lessonName));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();

        return result;
    }

    @SuppressLint("Range")
    public Homework getHomework(int flowLvl, int course, int group, int subgroup,
                              int year, int month, int day, int lessonNum) {
        int flowId = getFlowId(flowLvl, course, group, subgroup);
        if (flowId == -1) return null;

        SQLiteDatabase database = getReadableDatabase();
        String[] columns = new String[] {KEY_HOMEWORK, KEY_LESSON_NAME};
        String selection = String.format(
                "%s = %s AND %s = %s AND %s = %s AND %s = %s AND %s = %s", KEY_FLOW, flowId,
                KEY_YEAR, year, KEY_MONTH, month, KEY_DAY, day, KEY_LESSON_NUM, lessonNum
        );
        Cursor cursor = database.query(
                HOMEWORK_TABLE_NAME, columns, selection, null,
                null, null, null
        );
        if (cursor.moveToFirst()) {
            String homework = cursor.getString(cursor.getColumnIndex(KEY_HOMEWORK));
            String lessonName = cursor.getString(cursor.getColumnIndex(KEY_LESSON_NAME));
            cursor.close();
            database.close();
            return new Homework(year, month, day, lessonNum, homework, lessonName);
        }
        cursor.close();
        database.close();
        return null;
    }

    @SuppressLint("Range")
    public void addOrUpdateHomework(int flowLvl, int course, int group, int subgroup,
                                    int year, int month, int day, int lessonNum,
                                    String lessonName, String homework) {
        if (lessonName == null || homework == null) return;

        lessonName = lessonName.replaceAll("\"", "").trim();
        homework = homework.replaceAll("\"", "").trim();
        int flowId = getFlowId(flowLvl, course, group, subgroup);

        if (flowId == -1 || lessonName.isEmpty() || homework.isEmpty()) return;

        SQLiteDatabase database = getWritableDatabase();
        String[] columns = new String[] {KEY_ID};
        String selection = String.format(
                "%s = %s AND %s = %s AND %s = %s AND %s = %s AND %s = %s", KEY_FLOW, flowId,
                KEY_YEAR, year, KEY_MONTH, month, KEY_DAY, day, KEY_LESSON_NUM, lessonNum
        );
        Cursor cursor = database.query(
                HOMEWORK_TABLE_NAME, columns, selection, null,
                null, null, null
        );
        ContentValues contentValues = new ContentValues();
        if (cursor.moveToFirst()) {
            int homeworkId = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            cursor.close();
            contentValues.put(KEY_HOMEWORK, homework);
            contentValues.put(KEY_LESSON_NAME, lessonName);
            database.update(
                    HOMEWORK_TABLE_NAME, contentValues,
                    KEY_ID + " = " + homeworkId, null
            );
        } else {
            cursor.close();
            contentValues.put(KEY_FLOW, flowId);
            contentValues.put(KEY_YEAR, year);
            contentValues.put(KEY_MONTH, month);
            contentValues.put(KEY_DAY, day);
            contentValues.put(KEY_LESSON_NUM, lessonNum);
            contentValues.put(KEY_HOMEWORK, homework);
            contentValues.put(KEY_LESSON_NAME, lessonName);
            database.insert(HOMEWORK_TABLE_NAME, null, contentValues);
        }
        database.close();
    }

    public void deleteHomework(int flowLvl, int course, int group, int subgroup,
                               int year, int month, int day, int lessonNum) {
        int flowId = getFlowId(flowLvl, course, group, subgroup);
        if (flowId == -1) return;
        SQLiteDatabase database = getWritableDatabase();
        String clause = String.format(
                "%s = %s AND %s = %s AND %s = %s AND %s = %s AND %s = %s", KEY_FLOW, flowId,
                KEY_YEAR, year, KEY_MONTH, month, KEY_DAY, day, KEY_LESSON_NUM, lessonNum
        );
        database.delete(HOMEWORK_TABLE_NAME, clause, null);
        database.close();
    }

    public void deleteHomeworkBefore(int year, int month, int day) {
        SQLiteDatabase database = getWritableDatabase();
        String clause = String.format(
                "%s < %s OR (%s = %s AND %s < %s) OR (%s = %s AND %s = %s AND %s < %s)",
                KEY_YEAR, year, KEY_YEAR, year, KEY_MONTH, month,
                KEY_YEAR, year, KEY_MONTH, month, KEY_DAY, day
        );
        database.delete(HOMEWORK_TABLE_NAME, clause, null);
        database.close();
    }

    @SuppressLint("Range")
    public LessonStruct getTempLesson(int flowLvl, int course, int group, int subgroup,
                                      int year, int month, int day, int lessonNum) {
        SQLiteDatabase database = getReadableDatabase();
        String lessonNameColumn = "lesson_name";
        String teacherNameColumn = "teacher_name";
        String sql = "SELECT "
                + LESSON_TABLE_NAME + "." + KEY_NAME + " AS " + lessonNameColumn + ", "
                + LESSON_TABLE_NAME + "." + KEY_CABINET + ", "
                + TEACHER_TABLE_NAME + "." + KEY_SURNAME + ", "
                + TEACHER_TABLE_NAME + "." + KEY_NAME + " AS " + teacherNameColumn + ", "
                + TEACHER_TABLE_NAME + "." + KEY_PATRONYMIC + ", "
                + TEMP_SCHEDULE_TABLE_NAME + "." + KEY_WILL_LESSON_BE
                + " FROM " + TEMP_SCHEDULE_TABLE_NAME
                + " INNER JOIN " + FLOW_TABLE_NAME + " ON " + TEMP_SCHEDULE_TABLE_NAME + "."
                + KEY_FLOW + " = " + FLOW_TABLE_NAME + "." + KEY_ID
                + " LEFT JOIN " + LESSON_TABLE_NAME + " ON " + TEMP_SCHEDULE_TABLE_NAME + "."
                + KEY_LESSON + " = " + LESSON_TABLE_NAME + "." + KEY_ID
                + " LEFT JOIN " + TEACHER_TABLE_NAME + " ON " + LESSON_TABLE_NAME + "."
                + KEY_TEACHER + " = " + TEACHER_TABLE_NAME + "." + KEY_ID + " WHERE "
                + FLOW_TABLE_NAME + "." + KEY_FLOW_LVL + " = " + flowLvl + " AND "
                + FLOW_TABLE_NAME + "." + KEY_COURSE + " = " + course + " AND "
                + FLOW_TABLE_NAME + "." + KEY_GROUP + " = " + group + " AND "
                + FLOW_TABLE_NAME + "." + KEY_SUBGROUP + " = " + subgroup + " AND "
                + TEMP_SCHEDULE_TABLE_NAME + "." + KEY_YEAR + " = " + year + " AND "
                + TEMP_SCHEDULE_TABLE_NAME + "." + KEY_MONTH + " = " + month + " AND "
                + TEMP_SCHEDULE_TABLE_NAME + "." + KEY_DAY + " = " + day + " AND "
                + TEMP_SCHEDULE_TABLE_NAME + "." + KEY_LESSON_NUM + " = " + lessonNum;
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            String lessonName = cursor.getString(cursor.getColumnIndex(lessonNameColumn));
            String cabinet = cursor.getString(cursor.getColumnIndex(KEY_CABINET));
            String surname = cursor.getString(cursor.getColumnIndex(KEY_SURNAME));
            String teacherName = cursor.getString(cursor.getColumnIndex(teacherNameColumn));
            String patronymic = cursor.getString(cursor.getColumnIndex(KEY_PATRONYMIC));
            boolean willLessonBe = cursor.getInt(cursor.getColumnIndex(KEY_WILL_LESSON_BE)) == 1;
            cursor.close();
            database.close();
            return new LessonStruct(
                    lessonName, cabinet, surname, teacherName, patronymic, willLessonBe
            );
        }
        cursor.close();
        database.close();
        return null;
    }

    @SuppressLint("Range")
    public void addOrUpdateTempSchedule(int flowLvl, int course, int group, int subgroup,
                                        int year, int month, int day, int lessonNum,
                                        String lessonName, String cabinet, String teacherSurname,
                                        String teacherName, String teacherPatronymic) {
        int flowId = getFlowId(flowLvl, course, group, subgroup);
        addOrUpdateTempSchedule(
                flowId, year, month, day, lessonNum, lessonName, cabinet, teacherSurname,
                teacherName, teacherPatronymic
        );
    }

    @SuppressLint("Range")
    private void addOrUpdateTempSchedule(int flowId, int year, int month, int day, int lessonNum,
                                         String lessonName, String cabinet, String teacherSurname,
                                         String teacherName, String teacherPatronymic) {
        if (lessonNum < 1 || lessonNum > 8 || lessonName == null || flowId == -1) {
            return;
        }

        if (cabinet == null) cabinet = "";
        if (teacherSurname == null) teacherSurname = "";
        if (teacherName == null) teacherName = "";
        if (teacherPatronymic == null) teacherPatronymic = "";

        lessonName = lessonName.replaceAll("\"", "").trim();
        cabinet = cabinet.replaceAll("\"", "");
        teacherSurname = teacherSurname.replaceAll("\"", "").toLowerCase().trim();
        teacherName = teacherName.replaceAll("\"", "").toLowerCase().trim();
        teacherPatronymic = teacherPatronymic.replaceAll("\"", "")
                .toLowerCase().trim();

        if (lessonName.isEmpty()) return;

        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            int teacherId = -1, lessonId = -1, tempScheduleId = -1, oldLessonId = -1;

            // finding teacher id
            String[] columns = new String[] {KEY_ID};
            String selection = String.format(
                    "%s = \"%s\" AND %s = \"%s\" AND %s = \"%s\"", KEY_SURNAME, teacherSurname,
                    KEY_NAME, teacherName, KEY_PATRONYMIC, teacherPatronymic
            );
            Cursor cursor = database.query(
                    TEACHER_TABLE_NAME, columns, selection, null,
                    null, null, null
            );
            if (cursor.moveToFirst()) {
                teacherId = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            }
            cursor.close();

            if (teacherId >= 0) {
                // finding lesson id
                columns = new String[] {KEY_ID};
                selection = String.format(
                        "%s = \"%s\" AND %s = \"%s\" AND %s = %s", KEY_NAME, lessonName,
                        KEY_CABINET, cabinet, KEY_TEACHER, teacherId
                );
                cursor = database.query(
                        LESSON_TABLE_NAME, columns, selection, null,
                        null, null, null
                );
                if (cursor.moveToFirst()) {
                    lessonId = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                }
                cursor.close();
            }

            if (lessonId >= 0) {
                // check if this schedule already exist
                selection = String.format(
                        "%s = %s AND %s = %s AND %s = %s AND %s = %s AND %s = %s " +
                                "AND %s = %s AND %s = %s",
                        KEY_FLOW, flowId, KEY_LESSON, lessonId, KEY_YEAR, year, KEY_MONTH, month,
                        KEY_DAY, day, KEY_LESSON_NUM, lessonNum, KEY_WILL_LESSON_BE, 1
                );
                cursor = database.query(
                        TEMP_SCHEDULE_TABLE_NAME, null, selection, null,
                        null, null, null
                );
                if (cursor.moveToFirst()) {
                    // nothing to change
                    database.setTransactionSuccessful();
                    return;
                }
                cursor.close();
            }

            // need we to add or to update
            columns = new String[] {KEY_ID, KEY_LESSON};
            selection = String.format(
                    "%s = %s AND %s = %s AND %s = %s AND %s = %s AND %s = %s",
                    KEY_FLOW, flowId, KEY_YEAR, year, KEY_MONTH, month,
                    KEY_DAY, day, KEY_LESSON_NUM, lessonNum
            );
            cursor = database.query(
                    TEMP_SCHEDULE_TABLE_NAME, columns, selection, null,
                    null, null, null
            );
            if (cursor.moveToFirst()) {
                tempScheduleId = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                oldLessonId = cursor.getInt(cursor.getColumnIndex(KEY_LESSON));
            }
            cursor.close();

            ContentValues contentValues = new ContentValues();

            if (teacherId < 0) {
                // new teacher
                contentValues = new ContentValues();
                contentValues.put(KEY_SURNAME, teacherSurname);
                contentValues.put(KEY_NAME, teacherName);
                contentValues.put(KEY_PATRONYMIC, teacherPatronymic);
                database.insert(TEACHER_TABLE_NAME, null, contentValues);
                columns = new String[] {KEY_ID};
                selection = String.format(
                        "%s = \"%s\" AND %s = \"%s\" AND %s = \"%s\"", KEY_SURNAME, teacherSurname,
                        KEY_NAME, teacherName, KEY_PATRONYMIC, teacherPatronymic
                );
                cursor = database.query(
                        TEACHER_TABLE_NAME, columns, selection, null,
                        null, null, null
                );
                cursor.moveToFirst();
                teacherId = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                cursor.close();
                contentValues.clear();
            }

            if (lessonId < 0) {
                // new lesson
                contentValues = new ContentValues();
                contentValues.put(KEY_NAME, lessonName);
                contentValues.put(KEY_CABINET, cabinet);
                contentValues.put(KEY_TEACHER, teacherId);
                database.insert(LESSON_TABLE_NAME, null, contentValues);
                columns = new String[] {KEY_ID};
                selection = String.format(
                        "%s = \"%s\" AND %s = \"%s\" AND %s = %s", KEY_NAME, lessonName,
                        KEY_CABINET, cabinet, KEY_TEACHER, teacherId
                );
                cursor = database.query(
                        LESSON_TABLE_NAME, columns, selection, null,
                        null, null, null
                );
                cursor.moveToFirst();
                lessonId = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                cursor.close();
                contentValues.clear();
            }

            if (tempScheduleId >= 0) {
                // update schedule
                contentValues.put(KEY_LESSON, lessonId);
                contentValues.put(KEY_WILL_LESSON_BE, true);
                database.update(
                        TEMP_SCHEDULE_TABLE_NAME, contentValues,
                        KEY_ID + " = " + tempScheduleId, null
                );
                contentValues.clear();
            } else {
                // add schedule
                contentValues.put(KEY_FLOW, flowId);
                contentValues.put(KEY_LESSON, lessonId);
                contentValues.put(KEY_YEAR, year);
                contentValues.put(KEY_MONTH, month);
                contentValues.put(KEY_DAY, day);
                contentValues.put(KEY_LESSON_NUM, lessonNum);
                contentValues.put(KEY_WILL_LESSON_BE, true);
                database.insert(TEMP_SCHEDULE_TABLE_NAME, null, contentValues);
                contentValues.clear();
            }

            checkLesson(oldLessonId);

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    @SuppressLint("Range")
    public void deleteTempSchedule(int flowLvl, int course, int group, int subgroup,
                                   int year, int month, int day, int lessonNum) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            int flowId = getFlowId(flowLvl, course, group, subgroup);
            if (flowId >= 0) {
                int lessonId = -1;
                String[] columns = new String[] {KEY_LESSON};
                String tempScheduleSelection = String.format(
                        "%s = %s AND %s = %s AND %s = %s AND %s = %s AND %s = %s", KEY_FLOW, flowId,
                        KEY_YEAR, year, KEY_MONTH, month, KEY_DAY, day,
                        KEY_LESSON_NUM, lessonNum
                );
                Cursor cursor = database.query(
                        TEMP_SCHEDULE_TABLE_NAME, columns, tempScheduleSelection, null,
                        null, null, null
                );
                if (cursor.moveToFirst()) {
                    lessonId = cursor.getInt(cursor.getColumnIndex(KEY_LESSON));
                    cursor.close();
                } else cursor.close();
                database.delete(TEMP_SCHEDULE_TABLE_NAME, tempScheduleSelection, null);
                checkLesson(lessonId);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        database.close();
    }

    @SuppressLint("Range")
    public void deleteTempScheduleBefore(int year, int month, int day) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            Set<Integer> lessonIds = new HashSet<>();
            String[] columns = new String[] {KEY_LESSON};
            String selection = String.format(
                    "%s < %s OR (%s = %s AND %s < %s) OR (%s = %s AND %s = %s AND %s < %s)",
                    KEY_YEAR, year, KEY_YEAR, year, KEY_MONTH, month,
                    KEY_YEAR, year, KEY_MONTH, month, KEY_DAY, day
            );
            Cursor cursor = database.query(
                    TEMP_SCHEDULE_TABLE_NAME, columns, selection, null,
                    null, null, null
            );
            if (cursor.moveToFirst()) {
                do {
                    lessonIds.add(cursor.getInt(cursor.getColumnIndex(KEY_LESSON)));
                } while (cursor.moveToNext());
            } else cursor.close();
            database.delete(TEMP_SCHEDULE_TABLE_NAME, selection, null);

            for (int lessonId : lessonIds) {
                checkLesson(lessonId);
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        database.close();
    }

    @SuppressLint("Range")
    public void setLessonWontBe(int flowLvl, int course, int group, int subgroup,
                                int year, int month, int day, int lessonNum) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            int flowId = getFlowId(flowLvl, course, group, subgroup);
            if (flowId == -1) {
                database.setTransactionSuccessful();
                return;
            }

            int lessonId = -1, tempScheduleId = -1, oldLessonId = -1;

            // check if this temp schedule already exist
            String selection = String.format(
                    "%s = %s AND %s = %s AND %s = %s AND %s = %s AND %s = %s " +
                            "AND %s = %s AND %s = %s",
                    KEY_FLOW, flowId, KEY_LESSON, lessonId, KEY_YEAR, year, KEY_MONTH, month,
                    KEY_DAY, day, KEY_LESSON_NUM, lessonNum, KEY_WILL_LESSON_BE, 0
            );
            Cursor cursor = database.query(
                    TEMP_SCHEDULE_TABLE_NAME, null, selection, null,
                    null, null, null
            );
            if (cursor.moveToFirst()) {
                // nothing to change
                database.setTransactionSuccessful();
                return;
            }
            cursor.close();

            // need we to add or to update
            String[] columns = new String[] {KEY_ID, KEY_LESSON};
            selection = String.format(
                    "%s = %s AND %s = %s AND %s = %s AND %s = %s AND %s = %s",
                    KEY_FLOW, flowId, KEY_YEAR, year, KEY_MONTH, month,
                    KEY_DAY, day, KEY_LESSON_NUM, lessonNum
            );
            cursor = database.query(
                    TEMP_SCHEDULE_TABLE_NAME, columns, selection, null,
                    null, null, null
            );
            if (cursor.moveToFirst()) {
                tempScheduleId = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                oldLessonId = cursor.getInt(cursor.getColumnIndex(KEY_LESSON));
            }
            cursor.close();

            ContentValues contentValues = new ContentValues();

            if (tempScheduleId >= 0) {
                // update schedule
                contentValues.put(KEY_LESSON, lessonId);
                contentValues.put(KEY_WILL_LESSON_BE, false);
                database.update(
                        TEMP_SCHEDULE_TABLE_NAME, contentValues,
                        KEY_ID + " = " + tempScheduleId, null
                );
                contentValues.clear();
            } else {
                // add schedule
                contentValues.put(KEY_FLOW, flowId);
                contentValues.put(KEY_LESSON, lessonId);
                contentValues.put(KEY_YEAR, year);
                contentValues.put(KEY_MONTH, month);
                contentValues.put(KEY_DAY, day);
                contentValues.put(KEY_LESSON_NUM, lessonNum);
                contentValues.put(KEY_WILL_LESSON_BE, false);
                database.insert(TEMP_SCHEDULE_TABLE_NAME, null, contentValues);
                contentValues.clear();
            }

            checkLesson(oldLessonId);

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    @SuppressLint("Range")
    private void checkLesson(int lessonId) {
        if (lessonId >= 0) {
            int teacherId = -1;
            SQLiteDatabase database = getWritableDatabase();

            String[] columns = new String[]{KEY_TEACHER};
            String selection = String.format("%s = %s", KEY_ID, lessonId);
            Cursor cursor = database.query(
                    LESSON_TABLE_NAME, columns, selection, null,
                    null, null, null
            );
            if (cursor.moveToFirst()) {
                teacherId = cursor.getInt(cursor.getColumnIndex(KEY_TEACHER));
            }
            cursor.close();

            selection = String.format("%s = %s", KEY_LESSON, lessonId);
            cursor = database.query(
                    SCHEDULE_TABLE_NAME, null, selection,
                    null, null, null, null
            );
            if (cursor.getCount() == 0) {
                cursor.close();
                cursor = database.query(
                        TEMP_SCHEDULE_TABLE_NAME, null, selection,
                        null, null, null, null
                );
                if (cursor.getCount() == 0) {
                    database.delete(
                            LESSON_TABLE_NAME, KEY_ID + " = " + lessonId,
                            null
                    );
                }
                cursor.close();
            } else cursor.close();

            if (teacherId >= 0) {
                selection = String.format("%s = %s", KEY_TEACHER, teacherId);
                cursor = database.query(
                        LESSON_TABLE_NAME, null, selection,
                        null, null, null, null
                );
                if (cursor.getCount() == 0) {
                    database.delete(
                            TEACHER_TABLE_NAME, KEY_ID + " = " + teacherId,
                            null
                    );
                }
                cursor.close();
            }
        }
    }

    @SuppressLint("Range")
    private int getFlowId(int flowLvl, int course, int group, int subgroup) {
        SQLiteDatabase database = getReadableDatabase();
        String[] columns = new String[] {KEY_ID};
        String selection = String.format(
                "%s = %s AND %s = %s AND %s = %s AND %s = %s", KEY_FLOW_LVL, flowLvl,
                KEY_COURSE, course, KEY_GROUP, group, KEY_SUBGROUP, subgroup
        );
        Cursor cursor = database.query(
                FLOW_TABLE_NAME, columns, selection, null,
                null, null, null
        );
        if (cursor.moveToFirst()) {
            int flowId = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            cursor.close();
            return flowId;
        } else {
            // flow not founded
            cursor.close();
            return -1;
        }
    }

    private static class ExportValues {
        public final int dayOfWeek, lessonNum;
        public final boolean isNumerator;
        public final String lessonName, cabinet, surname, teacherName, patronymic;

        public ExportValues(int dayOfWeek, int lessonNum, boolean isNumerator, String lessonName,
                            String cabinet, String surname, String teacherName, String patronymic) {
            this.dayOfWeek = dayOfWeek;
            this.lessonNum = lessonNum;
            this.isNumerator = isNumerator;
            this.lessonName = lessonName;
            this.cabinet = cabinet;
            this.surname = surname;
            this.teacherName = teacherName;
            this.patronymic = patronymic;
        }
    }*/
}
