package com.example.schedule;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingsStorage {
    private static final float VERSION = 1.00010f;
    public static final String SCHEDULE_SAVES = "ScheduleSaves";
    public static int textSize = 1;
    public static boolean displayModeFull = true;

    public static void updateTextSize(SharedPreferences saves) {
        textSize = saves.getInt("textSize", 1);
    }

    public static void saveTextSize(int textSize, SharedPreferences saves) {
        SettingsStorage.textSize = textSize;
        Editor editor = saves.edit();
        editor.putInt("textSize", textSize);
        editor.apply();
    }

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

    public static void updateDisplayMode(SharedPreferences saves) {
        SettingsStorage.displayModeFull = saves.getBoolean("displayFull", true);
    }

    public static void saveDisplayMode(boolean isFullDisplay, SharedPreferences saves) {
        SettingsStorage.displayModeFull = isFullDisplay;
        Editor editor = saves.edit();
        editor.putBoolean("displayFull", isFullDisplay);
        editor.apply();
    }
}
