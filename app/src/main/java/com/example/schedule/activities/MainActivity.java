package com.example.schedule.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.schedule.R;
import com.example.schedule.ScheduleDBHelper;
import com.example.schedule.SettingsStorage;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private int flowLvl, course, group, subgroup;
    private Button courseBtn, groupBtn, subgroupBtn, flowLvlBtn, contBtn;
    private SharedPreferences saves;
    private ActivityResultLauncher<Intent> newFlowActivity;
    private final String[] flowLvlStr = new String[] {
            "Бакалавриат/Специалитет",
            "Магистратура",
            "Аспирантура"
    };
    private ScheduleDBHelper dbHelper;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saves = getSharedPreferences(SettingsStorage.SCHEDULE_SAVES, MODE_PRIVATE);

        dbHelper = new ScheduleDBHelper(this);
        tmp();

        Calendar calendar = Calendar.getInstance();
        dbHelper.deleteHomeworkBefore(
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dbHelper.deleteTempScheduleBefore(
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        SettingsStorage.updateCountdownBeginning(saves);
        SettingsStorage.updateDisplayMode(saves);

        int theme = SettingsStorage.getTheme(saves);
        switch (theme) {
            case 0:
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                );
                break;
            case 1:
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO
                );
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES
                );
                break;
        }

        if (SettingsStorage.isLastVersion(saves)) {
            int[] curFlow = SettingsStorage.getCurFlow(saves);
            flowLvl = curFlow[0];
            course = curFlow[1];
            group = curFlow[2];
            subgroup = curFlow[3];
            startActivitySchedule();
        } else {
            flowLvl = 0;
            course = 0;
            group = 0;
            subgroup = 0;
            SettingsStorage.saveCurFlow(flowLvl, course, group, subgroup, saves);
            SettingsStorage.changeToLastVersion(saves);
            SharedPreferences.Editor editor = saves.edit();
            editor.remove("Storage");
            editor.apply();
        }

        courseBtn = findViewById(R.id.courseBtn);
        groupBtn = findViewById(R.id.groupBtn);
        subgroupBtn = findViewById(R.id.subgroupBtn);
        contBtn = findViewById(R.id.contBtn);
        flowLvlBtn = findViewById(R.id.flowLvlBtn);

        SettingsStorage.updateTextSize(saves);
        updateScreen();
        updateFlowLvlBtn();
        updateCourseBtn();
        updateGroupBtn();
        updateSubgroupBtn();

        courseBtn.setOnClickListener(new CourseBtnListener());
        groupBtn.setOnClickListener(new GroupBtnListener());
        subgroupBtn.setOnClickListener(new SubgroupBtnListener());
        contBtn.setOnClickListener(new ContBtnListener());
        flowLvlBtn.setOnClickListener(new FlowLvlBtnListener());

        newFlowActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new NewFlowActivityResultCallback()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateScreen();
        updateCourseBtn();
        updateGroupBtn();
        updateSubgroupBtn();
    }

    private void updateScreen() {
        switch (SettingsStorage.textSize) {
            case 0:
                courseBtn.setTextSize(10.0f);
                groupBtn.setTextSize(10.0f);
                subgroupBtn.setTextSize(10.0f);
                contBtn.setTextSize(10.0f);
                flowLvlBtn.setTextSize(10.0f);
                break;
            case 1:
                courseBtn.setTextSize(20.0f);
                groupBtn.setTextSize(20.0f);
                subgroupBtn.setTextSize(20.0f);
                contBtn.setTextSize(20.0f);
                flowLvlBtn.setTextSize(20.0f);
                break;
            case 2:
                courseBtn.setTextSize(30.0f);
                groupBtn.setTextSize(30.0f);
                subgroupBtn.setTextSize(30.0f);
                contBtn.setTextSize(30.0f);
                flowLvlBtn.setTextSize(30.0f);
                break;
        }
    }

    private void updateFlowLvlBtn() {
        flowLvlBtn.setText(flowLvlStr[flowLvl]);
    }

    private void updateCourseBtn() {
        if (course == 0) {
            courseBtn.setText(getString(R.string.choose_course));
        } else {
            courseBtn.setText(String.format("%s курс", course));
        }
    }

    private void updateGroupBtn() {
        if (group == 0) {
            groupBtn.setText(getString(R.string.choose_group));
        } else {
            groupBtn.setText(String.format("%s группа", group));
        }
    }

    private void updateSubgroupBtn() {
        if (subgroup == 0) {
            subgroupBtn.setText(getString(R.string.choose_subgroup));
        } else {
            subgroupBtn.setText(String.format("%s подгруппа", subgroup));
        }
    }

    private void startActivitySchedule() {
        if (course > 0 && group > 0 && subgroup > 0) {
            Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
            intent.putExtra("flowLvl", flowLvl);
            intent.putExtra("course", course);
            intent.putExtra("group", group);
            intent.putExtra("subgroup", subgroup);
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            String selection = String.format(
                    "%s = %s AND %s = %s AND %s = %s AND %s = %s",
                    ScheduleDBHelper.KEY_FLOW_LVL, flowLvl,
                    ScheduleDBHelper.KEY_COURSE, course,
                    ScheduleDBHelper.KEY_GROUP, group,
                    ScheduleDBHelper.KEY_SUBGROUP, subgroup
            );
            Cursor cursor = database.query(
                    ScheduleDBHelper.FLOW_TABLE_NAME, null, selection, null,
                    null, null, null
            );
            if (!cursor.moveToFirst()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ScheduleDBHelper.KEY_FLOW_LVL, flowLvl);
                contentValues.put(ScheduleDBHelper.KEY_COURSE, course);
                contentValues.put(ScheduleDBHelper.KEY_GROUP, group);
                contentValues.put(ScheduleDBHelper.KEY_SUBGROUP, subgroup);
                database.insert(ScheduleDBHelper.FLOW_TABLE_NAME, null, contentValues);
            }
            cursor.close();
            database.close();
            dbHelper.close();
            startActivity(intent);
        }
    }

    private class FlowLvlBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Context context = MainActivity.this;
            new MaterialAlertDialogBuilder(context, R.style.Theme_Schedule_Dialog)
                    .setTitle(getString(R.string.choose_flow_lvl))
                    .setItems(flowLvlStr, new DialogInterfaceListener())
                    .show();
        }

        private class DialogInterfaceListener implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (flowLvl != which) {
                    flowLvl = which;
                    course = 0;
                    group = 0;
                    subgroup = 0;
                    updateFlowLvlBtn();
                    updateCourseBtn();
                    updateGroupBtn();
                    updateSubgroupBtn();
                    SettingsStorage.saveCurFlow(flowLvl, course, group, subgroup, saves);
                }
            }
        }
    }

    private class CourseBtnListener implements View.OnClickListener {
        private String[] items;

        @SuppressLint("Range")
        @Override
        public void onClick(View v) {
            Context context = MainActivity.this;
            ArrayList<String> itemsList = new ArrayList<>();
            SQLiteDatabase database = dbHelper.getReadableDatabase();
            String[] columns = new String[] {ScheduleDBHelper.KEY_COURSE};
            String selection = String.format(
                    "%s = %s AND %s > 0", ScheduleDBHelper.KEY_FLOW_LVL, flowLvl,
                    ScheduleDBHelper.KEY_COURSE);
            String groupBy = ScheduleDBHelper.KEY_COURSE;
            String orderBy = ScheduleDBHelper.KEY_COURSE;
            Cursor cursor = database.query(
                    ScheduleDBHelper.FLOW_TABLE_NAME, columns, selection, null,
                    groupBy, null, orderBy
            );
            if (cursor.moveToFirst()) {
                do {
                    itemsList.add(Integer.toString(cursor.getInt(
                            cursor.getColumnIndex(ScheduleDBHelper.KEY_COURSE)
                    )));
                } while (cursor.moveToNext());
            }
            cursor.close();
            database.close();
            itemsList.add("...");
            items = itemsList.toArray(new String[0]);
            new MaterialAlertDialogBuilder(context, R.style.Theme_Schedule_Dialog)
                    .setTitle(getString(R.string.choose_course))
                    .setItems(items, new DialogInterfaceListener())
                    .show();
        }

        private class DialogInterfaceListener implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("...")) {
                    Intent intent = new Intent(MainActivity.this, NewFlowActivity.class);
                    intent.putExtra("flow", "course");
                    newFlowActivity.launch(intent);
                    return;
                }
                int newCourse = Integer.parseInt(items[which]);
                if (course != newCourse) {
                    course = newCourse;
                    group = 0;
                    subgroup = 0;
                    updateCourseBtn();
                    updateGroupBtn();
                    updateSubgroupBtn();
                    SettingsStorage.saveCurFlow(flowLvl, course, group, subgroup, saves);
                }
            }
        }
    }

    private class GroupBtnListener implements View.OnClickListener {
        private String[] items;

        @SuppressLint("Range")
        @Override
        public void onClick(View v) {
            if (course > 0) {
                Context context = MainActivity.this;
                ArrayList<String> itemsList = new ArrayList<>();
                SQLiteDatabase database = dbHelper.getReadableDatabase();
                String[] columns = new String[] {ScheduleDBHelper.KEY_GROUP};
                String selection = String.format(
                        "%s = %s AND %s = %s AND %s > 0", ScheduleDBHelper.KEY_FLOW_LVL, flowLvl,
                        ScheduleDBHelper.KEY_COURSE, course, ScheduleDBHelper.KEY_GROUP
                );
                String groupBy = ScheduleDBHelper.KEY_GROUP;
                String orderBy = ScheduleDBHelper.KEY_GROUP;
                Cursor cursor = database.query(
                        ScheduleDBHelper.FLOW_TABLE_NAME, columns, selection, null,
                        groupBy, null, orderBy
                );
                if (cursor.moveToFirst()) {
                    do {
                        itemsList.add(Integer.toString(cursor.getInt(
                                cursor.getColumnIndex(ScheduleDBHelper.KEY_GROUP)
                        )));
                    } while (cursor.moveToNext());
                }
                cursor.close();
                database.close();
                itemsList.add("...");
                items = itemsList.toArray(new String[0]);
                new MaterialAlertDialogBuilder(context, R.style.Theme_Schedule_Dialog)
                        .setTitle(getString(R.string.choose_group))
                        .setItems(items, new DialogInterfaceListener())
                        .show();
            }
        }

        private class DialogInterfaceListener implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("...")) {
                    Intent intent = new Intent(MainActivity.this, NewFlowActivity.class);
                    intent.putExtra("flow", "group");
                    newFlowActivity.launch(intent);
                    return;
                }
                int newGroup = Integer.parseInt(items[which]);
                if (group != newGroup) {
                    group = newGroup;
                    subgroup = 0;
                    updateGroupBtn();
                    updateSubgroupBtn();
                    SettingsStorage.saveCurFlow(flowLvl, course, group, subgroup, saves);
                }
            }
        }
    }

    private class SubgroupBtnListener implements View.OnClickListener {
        private String[] items;

        @SuppressLint("Range")
        @Override
        public void onClick(View v) {
            if (course > 0 && group > 0) {
                Context context = MainActivity.this;
                ArrayList<String> itemsList = new ArrayList<>();
                SQLiteDatabase database = dbHelper.getReadableDatabase();
                String[] columns = new String[] {ScheduleDBHelper.KEY_SUBGROUP};
                String selection = String.format(
                        "%s = %s AND %s = %s AND %s = %s AND %s > 0",ScheduleDBHelper.KEY_FLOW_LVL,
                        flowLvl, ScheduleDBHelper.KEY_COURSE, course, ScheduleDBHelper.KEY_GROUP,
                        group, ScheduleDBHelper.KEY_SUBGROUP
                );
                String groupBy = ScheduleDBHelper.KEY_SUBGROUP;
                String orderBy = ScheduleDBHelper.KEY_SUBGROUP;
                Cursor cursor = database.query(
                        ScheduleDBHelper.FLOW_TABLE_NAME, columns, selection, null,
                        groupBy, null, orderBy
                );
                if (cursor.moveToFirst()) {
                    do {
                        itemsList.add(Integer.toString(cursor.getInt(
                                cursor.getColumnIndex(ScheduleDBHelper.KEY_SUBGROUP)
                        )));
                    } while (cursor.moveToNext());
                }
                cursor.close();
                database.close();
                itemsList.add("...");
                items = itemsList.toArray(new String[0]);
                new MaterialAlertDialogBuilder(context, R.style.Theme_Schedule_Dialog)
                        .setTitle(getString(R.string.choose_subgroup))
                        .setItems(items, new DialogInterfaceListener())
                        .show();
            }
        }

        private class DialogInterfaceListener implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("...")) {
                    Intent intent = new Intent(MainActivity.this, NewFlowActivity.class);
                    intent.putExtra("flow", "subgroup");
                    newFlowActivity.launch(intent);
                    return;
                }
                int newSubgroup = Integer.parseInt(items[which]);
                if (subgroup != newSubgroup) {
                    subgroup = newSubgroup;
                    updateSubgroupBtn();
                    SettingsStorage.saveCurFlow(flowLvl, course, group, subgroup, saves);
                }
            }
        }
    }

    private class ContBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            startActivitySchedule();
        }

    }

    private class NewFlowActivityResultCallback implements ActivityResultCallback<ActivityResult> {
        @Override
        public void onActivityResult(ActivityResult result) {
            try {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String flow = data.getStringExtra("flow");
                        int res = data.getIntExtra("result", 0);
                        switch (flow) {
                            case "course":
                                course = res;
                                group = 0;
                                subgroup = 0;
                                updateCourseBtn();
                                updateGroupBtn();
                                updateSubgroupBtn();
                                SettingsStorage.saveCurFlow(flowLvl, course, group, subgroup, saves);
                                break;
                            case "group":
                                group = res;
                                subgroup = 0;
                                updateGroupBtn();
                                updateSubgroupBtn();
                                SettingsStorage.saveCurFlow(flowLvl, course, group, subgroup, saves);
                                break;
                            case "subgroup":
                                subgroup = res;
                                updateSubgroupBtn();
                                SettingsStorage.saveCurFlow(flowLvl, course, group, subgroup, saves);
                                break;
                        }
                    }
                }
            } catch (Exception ignored) {}
        }
    }

    @SuppressLint("Range")
    private void tmp() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Log.w("Database", "_____________ FLOW TABLE _____________");
        Cursor cursor = database.query(ScheduleDBHelper.FLOW_TABLE_NAME, null, null, null,
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Log.w("Database", String.format("%s - %s, %s - %s, %s - %s, %s - %s, %s - %s",
                        ScheduleDBHelper.KEY_ID, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_ID)),
                        ScheduleDBHelper.KEY_FLOW_LVL, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_FLOW_LVL)),
                        ScheduleDBHelper.KEY_COURSE, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_COURSE)),
                        ScheduleDBHelper.KEY_GROUP, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_GROUP)),
                        ScheduleDBHelper.KEY_SUBGROUP, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_SUBGROUP))));
            } while (cursor.moveToNext());
        } else {
            Log.w("Database", "0 rows");
        }
        cursor.close();

        Log.w("Database", "_____________ TEACHER TABLE _____________");
        cursor = database.query(ScheduleDBHelper.TEACHER_TABLE_NAME, null, null, null,
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Log.w("Database", String.format("%s - %s, %s - %s, %s - %s, %s - %s",
                        ScheduleDBHelper.KEY_ID, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_ID)),
                        ScheduleDBHelper.KEY_SURNAME, cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_SURNAME)),
                        ScheduleDBHelper.KEY_NAME, cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_NAME)),
                        ScheduleDBHelper.KEY_PATRONYMIC, cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_PATRONYMIC))));
            } while (cursor.moveToNext());
        } else {
            Log.w("Database", "0 rows");
        }
        cursor.close();

        Log.w("Database", "_____________ LESSON TABLE _____________");
        cursor = database.query(ScheduleDBHelper.LESSON_TABLE_NAME, null, null, null,
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Log.w("Database", String.format("%s - %s, %s - %s, %s - %s, %s - %s",
                        ScheduleDBHelper.KEY_ID, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_ID)),
                        ScheduleDBHelper.KEY_NAME, cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_NAME)),
                        ScheduleDBHelper.KEY_CABINET, cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_CABINET)),
                        ScheduleDBHelper.KEY_TEACHER, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_TEACHER))));
            } while (cursor.moveToNext());
        } else {
            Log.w("Database", "0 rows");
        }
        cursor.close();

        Log.w("Database", "_____________ SCHEDULE TABLE _____________");
        cursor = database.query(ScheduleDBHelper.SCHEDULE_TABLE_NAME, null, null, null,
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Log.w("Database", String.format("%s - %s, %s - %s, %s - %s, %s - %s, %s - %s, %s - %s",
                        ScheduleDBHelper.KEY_ID, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_ID)),
                        ScheduleDBHelper.KEY_FLOW, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_FLOW)),
                        ScheduleDBHelper.KEY_DAY_OF_WEEK, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_DAY_OF_WEEK)),
                        ScheduleDBHelper.KEY_LESSON, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_LESSON)),
                        ScheduleDBHelper.KEY_LESSON_NUM, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_LESSON_NUM)),
                        ScheduleDBHelper.KEY_IS_NUMERATOR, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_IS_NUMERATOR))));
            } while (cursor.moveToNext());
        } else {
            Log.w("Database", "0 rows");
        }
        cursor.close();

        Log.w("Database", "_____________ HOMEWORK TABLE _____________");
        cursor = database.query(ScheduleDBHelper.HOMEWORK_TABLE_NAME, null, null, null,
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Log.w("Database", String.format("%s - %s, %s - %s, %s - %s, %s - %s, %s - %s, %s - %s, %s - %s, %s - %s",
                        ScheduleDBHelper.KEY_ID, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_ID)),
                        ScheduleDBHelper.KEY_FLOW, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_FLOW)),
                        ScheduleDBHelper.KEY_YEAR, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_YEAR)),
                        ScheduleDBHelper.KEY_MONTH, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_MONTH)),
                        ScheduleDBHelper.KEY_DAY, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_DAY)),
                        ScheduleDBHelper.KEY_LESSON_NUM, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_LESSON_NUM)),
                        ScheduleDBHelper.KEY_LESSON_NAME, cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_LESSON_NAME)),
                        ScheduleDBHelper.KEY_HOMEWORK, cursor.getString(cursor.getColumnIndex(ScheduleDBHelper.KEY_HOMEWORK))));
            } while (cursor.moveToNext());
        } else {
            Log.w("Database", "0 rows");
        }
        cursor.close();

        Log.w("Database", "_____________ TEMP SCHEDULE TABLE _____________");
        cursor = database.query(ScheduleDBHelper.TEMP_SCHEDULE_TABLE_NAME, null, null, null,
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Log.w("Database", String.format("%s - %s, %s - %s, %s - %s, %s - %s, %s - %s, %s - %s, %s - %s, %s - %s",
                        ScheduleDBHelper.KEY_ID, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_ID)),
                        ScheduleDBHelper.KEY_FLOW, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_FLOW)),
                        ScheduleDBHelper.KEY_LESSON, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_LESSON)),
                        ScheduleDBHelper.KEY_YEAR, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_YEAR)),
                        ScheduleDBHelper.KEY_MONTH, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_MONTH)),
                        ScheduleDBHelper.KEY_DAY, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_DAY)),
                        ScheduleDBHelper.KEY_LESSON_NUM, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_LESSON_NUM)),
                        ScheduleDBHelper.KEY_WILL_LESSON_BE, cursor.getInt(cursor.getColumnIndex(ScheduleDBHelper.KEY_WILL_LESSON_BE))));
            } while (cursor.moveToNext());
        } else {
            Log.w("Database", "0 rows");
        }
        cursor.close();
        database.close();
    }
}