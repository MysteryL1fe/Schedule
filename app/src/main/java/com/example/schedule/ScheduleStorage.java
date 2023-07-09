package com.example.schedule;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.schedule.exceptions.ScheduleException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class ScheduleStorage {
    /*private static final Gson gson = new Gson();
    private static final Type scheduleSetType = new TypeToken<Set<Schedule>>(){}.getType();
    private static Set<Schedule> storage = new HashSet<>();
    private static final Type scheduleType = new TypeToken<Schedule>(){}.getType();
    private static Schedule curSchedule;
    private static int curFlowLvl, curCourse, curGroup, curSubgroup;

    public static void saveStorage(SharedPreferences pref) {
        String strObject = gson.toJson(storage, scheduleSetType);
        Editor editor = pref.edit();
        editor.putString("Storage", strObject);
        editor.apply();
    }

    public static Set<Schedule> getStorage(SharedPreferences saves) {
        updateStorage(saves);
        if (storage == null) return new HashSet<>();
        return Set.copyOf(storage);
    }

    public static void updateStorage(SharedPreferences saves) {
        String json = saves.getString("Storage", "");
        storage = gson.fromJson(json, scheduleSetType);
    }

    public static void addSchedule(Schedule schedule, SharedPreferences saves) {
        if (storage != null) {
            for (Schedule sched : storage) {
                if (schedule.getCourse() == sched.getCourse()
                        && schedule.getGroup() == sched.getGroup()
                        && schedule.getSubgroup() == sched.getSubgroup()) return;
            }
        } else {
            storage = new HashSet<>();
        }
        storage.add(schedule);
        saveStorage(saves);
    }*/

    public static void clearStorage(SharedPreferences saves) {
        Editor editor = saves.edit();
        editor.remove("Storage");
        editor.apply();
    }

    /*public static Schedule getSchedule(int flowLvl, int course, int group, int subgroup,
                                       SharedPreferences saves) {
        if (curSchedule != null && flowLvl == curFlowLvl && course == curCourse
                && group == curGroup && subgroup == curSubgroup) return curSchedule;
        curFlowLvl = flowLvl;
        curCourse = course;
        curGroup = group;
        curSubgroup = subgroup;
        updateStorage(saves);
        for (Schedule schedule : storage) {
            if (schedule.getFlowLvl() == flowLvl && schedule.getCourse() == course
                    && schedule.getGroup() == group && schedule.getSubgroup() == subgroup) {
                curSchedule = schedule;
                return curSchedule;
            }
        }
        curSchedule = null;
        return null;
    }

    public static void changeLesson(int flowLvl, int course, int group, int subgroup, int dayOfWeek,
                                    int lessonNum, boolean isNumerator, String newLessonName,
                                    String newTeacher, String newCabinet, SharedPreferences saves)
            throws ScheduleException {
        Schedule schedule = getSchedule(flowLvl, course, group, subgroup, saves);
        if (schedule != null) {
            LessonStruct lesson = schedule.getLesson(dayOfWeek, lessonNum, isNumerator);
            if (lesson == null) lesson = new LessonStruct();
            lesson.name = newLessonName;
            lesson.teacher = newTeacher;
            lesson.cabinet = newCabinet;
            schedule.setLesson(dayOfWeek, lessonNum, isNumerator, lesson);
            saveStorage(saves);
        }
    }

    public static void changeSchedule(Schedule oldSchedule, Schedule newSchedule,
                                      SharedPreferences saves) {
        oldSchedule.setNumerator(newSchedule.getNumerator());
        oldSchedule.setDenominator(newSchedule.getDenominator());
        saveStorage(saves);
    }

    public static boolean exportSchedule(SharedPreferences saves) {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!dir.exists()) dir.mkdir();
        try {
            Schedule schedule = getSchedule(curFlowLvl, curCourse, curGroup, curSubgroup, saves);
            if (schedule == null) throw new ScheduleException("Расписание не найдено");
            String fileName = Utils.generateStr();
            File file = new File(dir, String.format("%s.sch", fileName));
            FileOutputStream outputStream = new FileOutputStream(file, false);
            outputStream.write(gson.toJson(schedule, scheduleType).getBytes());
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void importScheduleBefore29(String path, SharedPreferences saves) {
        File file = new File(path);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            importSchedule(br.readLine(), saves);
            br.close();
        } catch (Exception e) {
            Log.e("Schedule", e.toString());
        }
    }

    public static void importScheduleAfter28(Uri uri, ContentResolver resolver,
                                             SharedPreferences saves) {
        try (InputStream stream = resolver.openInputStream(uri)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            importSchedule(br.readLine(), saves);
            br.close();
        } catch (Exception e) {
            Log.e("Schedule", e.toString());
        }
    }

    private static void importSchedule(String schedule, SharedPreferences saves) {
        Schedule importedSchedule = gson.fromJson(schedule, scheduleType);
        if (importedSchedule != null && curSchedule != null) {
            changeSchedule(curSchedule, importedSchedule, saves);
        }
    }*/
}
