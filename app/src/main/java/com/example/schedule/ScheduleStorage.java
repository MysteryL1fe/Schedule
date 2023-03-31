package com.example.schedule;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class ScheduleStorage {
    private static Set<Schedule> storage = new HashSet<>();
    private static final Gson gson = new Gson();
    private static final Type type = new TypeToken<Set<Schedule>>(){}.getType();

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
}
