package com.example.schedule;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ScheduleStorage {
    public static void clearStorage(SharedPreferences saves) {
        Editor editor = saves.edit();
        editor.remove("Storage");
        editor.apply();
    }
}
