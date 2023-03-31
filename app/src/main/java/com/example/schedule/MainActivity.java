package com.example.schedule;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private int flowLvl, course, group, subgroup;
    private Button courseBtn, groupBtn, subgroupBtn, contBtn, flowLvlBtn;
    private SharedPreferences saves;
    SharedPreferences.Editor editor;
    private Set<Schedule> storage;
    ActivityResultLauncher<Intent> newFlowActivity;
    private String[] flowLvlStr = new String[] {"Бакалавриат/Специалитет", "Магистратура",
            "Аспирантура"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saves = getSharedPreferences("ScheduleSaves", MODE_PRIVATE);
        editor = saves.edit();
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
        }

        ScheduleStorage.updateStorage(saves);
        storage = ScheduleStorage.getStorage();

        courseBtn = findViewById(R.id.courseBtn);
        groupBtn = findViewById(R.id.groupBtn);
        subgroupBtn = findViewById(R.id.subgroupBtn);
        contBtn = findViewById(R.id.contBtn);
        flowLvlBtn = findViewById(R.id.flowLvlBtn);

        updateFlowLvlBtn();
        updateCourseBtn();
        updateGroupBtn();
        updateSubgroupBtn();

        courseBtn.setOnClickListener(new CourseBtnListener());
        groupBtn.setOnClickListener(new GroupBtnListener());
        subgroupBtn.setOnClickListener(new SubgroupBtnListener());
        contBtn.setOnClickListener(new ContBtnListener());
        flowLvlBtn.setOnClickListener(new FlowLvlBtn());

        newFlowActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new NewFlowActivityResultCallback()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateCourseBtn();
        updateGroupBtn();
        updateSubgroupBtn();
    }

    private void updateFlowLvlBtn() {
        flowLvlBtn.setText(flowLvlStr[flowLvl]);
    }

    @SuppressLint("SetTextI18n")
    private void updateCourseBtn() {
        if (course == 0) {
            courseBtn.setText("Выберите курс");
        } else {
            courseBtn.setText(String.format("%s курс", course));
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateGroupBtn() {
        if (group == 0) {
            groupBtn.setText("Выберите группу");
        } else {
            groupBtn.setText(String.format("%s группа", group));
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateSubgroupBtn() {
        if (subgroup == 0) {
            subgroupBtn.setText("Выберите подгруппу");
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
            startActivity(intent);
        }
    }

    private class CourseBtnListener implements View.OnClickListener {
        private String[] items;

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            Context context = MainActivity.this;
            Set<String> itemsSet = new HashSet<>();
            for (Schedule sched : storage) {
                if (sched.getFlowLvl() == flowLvl)
                    itemsSet.add(Integer.toString(sched.getCourse()));
            }
            itemsSet.add("...");
            items = itemsSet.toArray(new String[0]);
            new MaterialAlertDialogBuilder(context, R.style.Theme_Schedule_Dialog)
                    .setTitle("Выберите курс")
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

        @Override
        public void onClick(View v) {
            if (course > 0) {
                Context context = MainActivity.this;
                Set<String> itemsSet = new HashSet<>();
                for (Schedule sched : storage) {
                    if (sched.getFlowLvl() == flowLvl && sched.getCourse() == course)
                        itemsSet.add(Integer.toString(sched.getGroup()));
                }
                itemsSet.add("...");
                items = itemsSet.toArray(new String[0]);
                new MaterialAlertDialogBuilder(context, R.style.Theme_Schedule_Dialog)
                        .setTitle("Выберите группу")
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

        @Override
        public void onClick(View v) {
            if (course > 0 && group > 0) {
                Context context = MainActivity.this;
                Set<String> itemsSet = new HashSet<>();
                for (Schedule sched : storage) {
                    if (sched.getFlowLvl() == flowLvl && sched.getCourse() == course &&
                            sched.getGroup() == group)
                        itemsSet.add(Integer.toString(sched.getSubgroup()));
                }
                itemsSet.add("...");
                items = itemsSet.toArray(new String[0]);
                new MaterialAlertDialogBuilder(context, R.style.Theme_Schedule_Dialog)
                        .setTitle("Выберите подгруппу")
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

    private class FlowLvlBtn implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Context context = MainActivity.this;
            new MaterialAlertDialogBuilder(context, R.style.Theme_Schedule_Dialog)
                    .setTitle("Выберите уровень образования")
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
}