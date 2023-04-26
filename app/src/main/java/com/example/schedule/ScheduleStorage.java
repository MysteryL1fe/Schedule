package com.example.schedule;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.schedule.exceptions.ScheduleException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ScheduleStorage {
    private static Set<Schedule> storage = new HashSet<>();
    private static final Gson gson = new Gson();
    private static final Type type = new TypeToken<Set<Schedule>>(){}.getType();
    private static Schedule curSchedule;
    private static int curFlowLvl, curCourse, curGroup, curSubgroup;

    public static void saveStorage(SharedPreferences pref) {
        String strObject = gson.toJson(storage, type);
        Editor editor = pref.edit();
        editor.putString("Storage", strObject);
        editor.apply();
    }

    public static Set<Schedule> getStorage() {
        if (storage == null) return new HashSet<>();
        return Set.copyOf(storage);
    }

    public static void updateStorage(SharedPreferences pref) {
        String json = pref.getString("Storage", "");
        storage = gson.fromJson(json, type);
    }

    public static void addSchedule(Schedule schedule, SharedPreferences pref) {
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
        saveStorage(pref);
    }

    public static void clearStorage(SharedPreferences saves) {
        storage = new HashSet<>();
        Editor editor = saves.edit();
        String strObject = gson.toJson(storage, type);
        editor.putString("Storage", strObject);
        editor.apply();
    }

    public static Schedule getSchedule(int flowLvl, int course, int group, int subgroup) {
        if (curSchedule != null && flowLvl == curFlowLvl && course == curCourse
                && group == curGroup && subgroup == curSubgroup) return curSchedule;
        curFlowLvl = flowLvl;
        curCourse = course;
        curGroup = group;
        curSubgroup = subgroup;
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
        Schedule schedule = getSchedule(flowLvl, course, group, subgroup);
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

    public static boolean exportSchedule() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!dir.exists()) dir.mkdir();
        try {
            Schedule schedule = getSchedule(curFlowLvl, curCourse, curGroup, curSubgroup);
            if (schedule == null) throw new ScheduleException("Расписание не найдено");
            String fileName = Utils.generateStr();
            File file = new File(dir, String.format("%s.sch", fileName));
            FileOutputStream outputStream = new FileOutputStream(file, false);
            outputStream.write(schedule.toExport().getBytes());
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void importSchedule(String path, SharedPreferences saves) {
        if (path == null || !path.endsWith(".sch")) return;
        File file = new File(path);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str = br.readLine();
            br.close();
            String[] weeks = str.split(";");
            String[] numerator = weeks[0].split("&");
            String[] denominator = weeks[1].split("&");
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 8; j++) {
                    String s = numerator[i * 8 + j];
                    if (s.equals("_")) changeLesson(curFlowLvl, curCourse, curGroup, curSubgroup,
                            i + 1, j + 1, true, "",
                            "", "", saves);
                    else {
                        String[] lesson = s.split("\\*");
                        changeLesson(curFlowLvl, curCourse, curGroup, curSubgroup,
                                i + 1, j + 1, true,
                                lesson[0].equals("_") ? "" : lesson[0],
                                lesson[1].equals("_") ? "" : lesson[1],
                                lesson[2].equals("_") ? "" : lesson[2], saves);
                    }
                }
            }
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 8; j++) {
                    String s = denominator[i * 8 + j];
                    if (s.equals("_")) changeLesson(curFlowLvl, curCourse, curGroup, curSubgroup,
                            i + 1, j + 1, false, "",
                            "", "", saves);
                    else {
                        String[] lesson = s.split("\\*");
                        changeLesson(curFlowLvl, curCourse, curGroup, curSubgroup,
                                i + 1, j + 1, false,
                                lesson[0].equals("_") ? "" : lesson[0],
                                lesson[1].equals("_") ? "" : lesson[1],
                                lesson[2].equals("_") ? "" : lesson[2], saves);
                    }
                }
            }
        } catch (Exception ignored) {}
    }
}
