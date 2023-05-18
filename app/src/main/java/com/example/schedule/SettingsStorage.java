package com.example.schedule;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingsStorage {
    private static float VERSION = 1.00007f;
    public static String SCHEDULE_SAVES = "ScheduleSaves";

    public static float getCurVersion(SharedPreferences saves) {
        return saves.getFloat("version", 0);
    }

    public static boolean isLastVersion(SharedPreferences saves) {
        return VERSION == getCurVersion(saves);
    }

    public static void changeToLastVersion(SharedPreferences saves) {
        Editor editor = saves.edit();
        editor.putFloat("version", VERSION);
        editor.apply();
    }

    public static int[] getCurFlow(SharedPreferences saves) {
        return new int[] {saves.getInt("flowLvl", 0),
                saves.getInt("course", 0),
                saves.getInt("group", 0),
                saves.getInt("subgroup", 0)};
    }

    public static void saveCurFlow(int flowLvl, int course, int group, int subgroup,
                                   SharedPreferences saves) {
        Editor editor = saves.edit();
        editor.putInt("flowLvl", flowLvl);
        editor.putInt("course", course);
        editor.putInt("group", group);
        editor.putInt("subgroup", subgroup);
        editor.apply();
    }

    public static int getTheme(SharedPreferences saves) {
        return saves.getInt("theme", 0);
    }

    public static void setTheme(int theme, SharedPreferences saves) {
        Editor editor = saves.edit();
        editor.putInt("theme", theme);
        editor.apply();
    }
}
