package com.example.schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int course, group, subgroup;
    Button courseBtn, groupBtn, subgroupBtn, contBtn;
    SharedPreferences saves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saves = getSharedPreferences("ScheduleSaves", MODE_PRIVATE);
        course = saves.getInt("course", 0);
        group = saves.getInt("group", 0);
        subgroup = saves.getInt("subgroup", 0);
        startActivitySchedule();

        courseBtn = findViewById(R.id.courseBtn);
        groupBtn = findViewById(R.id.groupBtn);
        subgroupBtn = findViewById(R.id.subgroupBtn);
        contBtn = findViewById(R.id.contBtn);

        updateCourseBtn();
        updateGroupBtn();
        updateSubgroupBtn();

        courseBtn.setOnClickListener(this);
        groupBtn.setOnClickListener(this);
        subgroupBtn.setOnClickListener(this);
        contBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateCourseBtn();
        updateGroupBtn();
        updateSubgroupBtn();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = saves.edit();
        Context context = MainActivity.this;
        String[] items = new String[0];
        switch (v.getId()) {
            case R.id.courseBtn:
                items = new String[] {"1"};
                new MaterialAlertDialogBuilder(context)
                        .setTitle("Выберите курс")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (course != which + 1) {
                                    course = which + 1;
                                    group = 0;
                                    subgroup = 0;
                                    updateCourseBtn();
                                    updateGroupBtn();
                                    updateSubgroupBtn();
                                    editor.putInt("course", course);
                                    editor.putInt("group", group);
                                    editor.putInt("subgroup", subgroup);
                                    editor.apply();
                                }
                            }
                        })
                        .show();
                break;
            case R.id.groupBtn:
                if (course == 1) {
                    items = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9",
                            "10", "11", "12", "13", "14", "15", "16"};
                }
                if (course > 0) {
                    new MaterialAlertDialogBuilder(context)
                            .setTitle("Выберите группу")
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (group != which + 1) {
                                        group = which + 1;
                                        subgroup = 0;
                                        updateGroupBtn();
                                        updateSubgroupBtn();
                                        editor.putInt("group", group);
                                        editor.putInt("subgroup", subgroup);
                                        editor.apply();
                                    }
                                }
                            })
                            .show();
                }
                break;
            case R.id.subgroupBtn:
                switch (group) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 10:
                    case 11:
                    case 13:
                    case 15:
                    case 16:
                        items = new String[] {"1", "2"};
                        break;
                    case 12:
                    case 14:
                        items = new String[] {"1"};
                        break;
                    case 9:
                        items = new String[] {"1", "2", "3"};
                }
                if (group > 0) {
                    new MaterialAlertDialogBuilder(context)
                            .setTitle("Выберите подгруппу")
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    subgroup = which + 1;
                                    updateSubgroupBtn();
                                    editor.putInt("subgroup", subgroup);
                                    editor.apply();
                                }
                            })
                            .show();
                }
                break;
            case R.id.contBtn:
                startActivitySchedule();
        }
    }

    @SuppressLint("SetTextI18n")
    public void updateCourseBtn() {
        if (course == 0) {
            courseBtn.setText("Выберите курс");
        } else {
            courseBtn.setText(String.format("%s курс", course));
        }
    }

    @SuppressLint("SetTextI18n")
    public void updateGroupBtn() {
        if (group == 0) {
            groupBtn.setText("Выберите группу");
        } else {
            groupBtn.setText(String.format("%s группа", group));
        }
    }

    @SuppressLint("SetTextI18n")
    public void updateSubgroupBtn() {
        if (subgroup == 0) {
            subgroupBtn.setText("Выберите подгруппу");
        } else {
            subgroupBtn.setText(String.format("%s подгруппа", subgroup));
        }
    }

    public void startActivitySchedule() {
        if (course != 0 && group != 0 && subgroup != 0) {
            Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
            intent.putExtra("subgroup", course * 1000 + group * 10 + subgroup);
            startActivity(intent);
        }
    }
}