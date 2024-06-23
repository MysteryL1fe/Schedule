package com.example.schedule.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.schedule.BackendService;
import com.example.schedule.R;
import com.example.schedule.RetrofitHelper;
import com.example.schedule.ScheduleDBHelper;
import com.example.schedule.SettingsStorage;
import com.example.schedule.dto.FlowResponse;
import com.example.schedule.entity.Flow;
import com.example.schedule.repo.FlowRepo;
import com.example.schedule.repo.HomeworkRepo;
import com.example.schedule.repo.LessonRepo;
import com.example.schedule.repo.TempScheduleRepo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

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
    private FlowRepo flowRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saves = getSharedPreferences(SettingsStorage.SCHEDULE_SAVES, MODE_PRIVATE);
        SettingsStorage.updateDisplayMode(saves);
        SettingsStorage.updateUseServer(saves);
        SettingsStorage.updateTextSize(saves);

        ScheduleDBHelper dbHelper = new ScheduleDBHelper(this);
        flowRepo = new FlowRepo(dbHelper);
        LessonRepo lessonRepo = new LessonRepo(dbHelper);
        HomeworkRepo homeworkRepo = new HomeworkRepo(dbHelper, flowRepo);
        TempScheduleRepo tempScheduleRepo = new TempScheduleRepo(dbHelper, flowRepo, lessonRepo);

        UploadFlows uploadFlows = new UploadFlows();
        uploadFlows.execute();

        LocalDate now = LocalDate.now();
        homeworkRepo.deleteAllBeforeDate(now);
        tempScheduleRepo.deleteAllBeforeDate(now);

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
            if (
                    flowLvl < 1 || flowLvl > 3 || course < 1 || course > 5
                            || group < 1 || subgroup < 1
            ) {
                flowLvl = 1;
                course = 1;
                group = 1;
                subgroup = 1;
            } else {
                startActivitySchedule();
            }
        } else {
            flowLvl = 1;
            course = 1;
            group = 1;
            subgroup = 1;
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

        courseBtn.setOnClickListener(new CourseBtnListener());
        groupBtn.setOnClickListener(new GroupBtnListener());
        subgroupBtn.setOnClickListener(new SubgroupBtnListener());
        contBtn.setOnClickListener(new ContBtnListener());
        flowLvlBtn.setOnClickListener(new FlowLvlBtnListener());

        updateScreen();

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

        updateFlowLvlBtn();
        updateCourseBtn();
        updateGroupBtn();
        updateSubgroupBtn();
    }

    private void updateFlowLvlBtn() {
        flowLvlBtn.setText(flowLvlStr[flowLvl - 1]);
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
        if (flowLvl > 0 && flowLvl < 4 && course > 0 && course < 6 && group > 0 && subgroup > 0) {
            Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
            intent.putExtra("flowLvl", flowLvl);
            intent.putExtra("course", course);
            intent.putExtra("group", group);
            intent.putExtra("subgroup", subgroup);
            Flow flow = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
                    flowLvl, course, group, subgroup
            );
            if (flow == null) flowRepo.add(flowLvl, course, group, subgroup);
            else if (!flow.isActive()) flowRepo.update(flowLvl, course, group, subgroup, true);
            startActivity(intent);
        }
    }

    private class FlowLvlBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new MaterialAlertDialogBuilder(MainActivity.this, R.style.Theme_Schedule_Dialog)
                    .setTitle(getString(R.string.choose_flow_lvl))
                    .setItems(flowLvlStr, new DialogInterfaceListener())
                    .show();
        }

        private class DialogInterfaceListener implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (flowLvl != which + 1) {
                    flowLvl = which + 1;
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

        @Override
        public void onClick(View v) {
            ArrayList<String> itemsList = new ArrayList<>();
            List<Integer> courses = flowRepo.findDistinctActiveCourseByFlowLvl(flowLvl);
            courses.forEach((e) -> itemsList.add(String.valueOf(e)));
            itemsList.add("...");
            items = itemsList.toArray(new String[0]);
            new MaterialAlertDialogBuilder(MainActivity.this, R.style.Theme_Schedule_Dialog)
                    .setTitle(getString(R.string.choose_course))
                    .setItems(items, new DialogInterfaceListener())
                    .show();
        }

        private class DialogInterfaceListener implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("...")) {
                    Intent intent = new Intent(
                            MainActivity.this, NewFlowActivity.class
                    );
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
                ArrayList<String> itemsList = new ArrayList<>();
                List<Integer> flows = flowRepo.findDistinctActiveFlowByFlowLvlAndCourse(flowLvl, course);
                flows.forEach((e) -> itemsList.add(String.valueOf(e)));
                itemsList.add("...");
                items = itemsList.toArray(new String[0]);
                new MaterialAlertDialogBuilder(
                        MainActivity.this, R.style.Theme_Schedule_Dialog
                )
                        .setTitle(getString(R.string.choose_group))
                        .setItems(items, new DialogInterfaceListener())
                        .show();
            }
        }

        private class DialogInterfaceListener implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("...")) {
                    Intent intent = new Intent(
                            MainActivity.this, NewFlowActivity.class
                    );
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
                ArrayList<String> itemsList = new ArrayList<>();
                List<Integer> subgroups = flowRepo.findDistinctActiveSubgroupByFlowLvlAndCourseAndFlow(
                        flowLvl, course, group
                );
                subgroups.forEach((e) -> itemsList.add(String.valueOf(e)));
                itemsList.add("...");
                items = itemsList.toArray(new String[0]);
                new MaterialAlertDialogBuilder(
                        MainActivity.this, R.style.Theme_Schedule_Dialog
                )
                        .setTitle(getString(R.string.choose_subgroup))
                        .setItems(items, new DialogInterfaceListener())
                        .show();
            }
        }

        private class DialogInterfaceListener implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("...")) {
                    Intent intent = new Intent(
                            MainActivity.this, NewFlowActivity.class
                    );
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
                                SettingsStorage.saveCurFlow(
                                        flowLvl, course, group, subgroup, saves
                                );
                                break;
                            case "group":
                                group = res;
                                subgroup = 0;
                                updateGroupBtn();
                                updateSubgroupBtn();
                                SettingsStorage.saveCurFlow(
                                        flowLvl, course, group, subgroup, saves
                                );
                                break;
                            case "subgroup":
                                subgroup = res;
                                updateSubgroupBtn();
                                SettingsStorage.saveCurFlow(
                                        flowLvl, course, group, subgroup, saves
                                );
                                break;
                        }
                    }
                }
            } catch (Exception ignored) {}
        }
    }

    private class UploadFlows extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            if (!SettingsStorage.useServer) return null;

            try {
                BackendService backendService = RetrofitHelper.getBackendService();
                Call<List<FlowResponse>> call = backendService.getAllFlows();
                Response<List<FlowResponse>> response = call.execute();
                List<FlowResponse> flows = response.body();
                flows.forEach((e) -> {
                    if (flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
                            e.getFlowLvl(), e.getCourse(), e.getFlow(), e.getSubgroup()
                    ) != null)
                        flowRepo.update(
                                e.getFlowLvl(), e.getCourse(), e.getFlow(), e.getSubgroup(),
                                e.getLessonsStartDate(), e.getSessionStartDate(),
                                e.getSessionEndDate(), e.isActive()
                        );
                    else flowRepo.add(
                            e.getFlowLvl(), e.getCourse(), e.getFlow(), e.getSubgroup(),
                            e.getLessonsStartDate(), e.getSessionStartDate(),
                            e.getSessionEndDate(), e.isActive()
                    );
                });

                flowRepo.findAllActive().forEach((e) -> {
                    boolean found = false;
                    for (FlowResponse flowResponse : flows) {
                        if (flowResponse.getFlowLvl() == e.getFlow()
                                && flowResponse.getCourse() == e.getCourse()
                                && flowResponse.getFlow() == e.getFlow()
                                && flowResponse.getSubgroup() == e.getSubgroup()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) flowRepo.update(
                            e.getFlowLvl(), e.getCourse(), e.getFlow(), e.getSubgroup(), false
                    );
                });
            } catch (Exception e) {
                Log.e("Backend", e.toString());
            }
            return null;
        }
    }
}