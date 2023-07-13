package com.example.schedule;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Calendar;

public class SettingsStorage {
    private static final float VERSION = 1.00009f;
    public static final String SCHEDULE_SAVES = "ScheduleSaves";
    public static int textSize = 1;
    private static Calendar countdownBeginning;
    private static final Gson gson = new Gson();
    private static final Type calendarType = new TypeToken<Calendar>(){}.getType();

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

    public static Calendar getCountdownBeginning() {
        return (Calendar) countdownBeginning.clone();
    }

    public static void setCountdownBeginning(Calendar countdownBeginning, SharedPreferences saves) {
        while (countdownBeginning.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            countdownBeginning.add(Calendar.DAY_OF_MONTH, -1);
        }
        SettingsStorage.countdownBeginning = countdownBeginning;
        Editor editor = saves.edit();
        editor.putString("countdownBeginning", gson.toJson(countdownBeginning, calendarType));
        editor.apply();
    }

    public static void updateCountdownBeginning(SharedPreferences saves) {
        if (countdownBeginning == null) {
            countdownBeginning = gson.fromJson(
                    saves.getString("countdownBeginning", ""), calendarType
            );
            if (countdownBeginning == null) {
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                countdownBeginning = calendar;
                Editor editor = saves.edit();
                editor.putString(
                        "countdownBeginning", gson.toJson(countdownBeginning, calendarType)
                );
                editor.apply();
            }
        }
    }
}
