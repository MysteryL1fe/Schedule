package com.example.schedule.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.schedule.BackendService;
import com.example.schedule.R;
import com.example.schedule.RetrofitHelper;
import com.example.schedule.ScheduleDBHelper;
import com.example.schedule.SettingsStorage;
import com.example.schedule.dto.FlowResponse;
import com.example.schedule.dto.HomeworkResponse;
import com.example.schedule.dto.ScheduleResponse;
import com.example.schedule.dto.TempScheduleResponse;
import com.example.schedule.entity.Flow;
import com.example.schedule.fragments.ChangeScheduleFragment;
import com.example.schedule.fragments.HomeworkFragment;
import com.example.schedule.fragments.NewHomeworkFragment;
import com.example.schedule.fragments.ScheduleFragment;
import com.example.schedule.fragments.SettingsFragment;
import com.example.schedule.fragments.TempScheduleFragment;
import com.example.schedule.repo.FlowRepo;
import com.example.schedule.repo.HomeworkRepo;
import com.example.schedule.repo.LessonRepo;
import com.example.schedule.repo.ScheduleRepo;
import com.example.schedule.repo.TempScheduleRepo;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDateTime;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ScheduleActivity extends AppCompatActivity {
    private int flowLvl, course, group, subgroup;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private FragmentManager fragmentManager;
    private MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Intent intent = getIntent();
        flowLvl = intent.getIntExtra("flowLvl", 0);
        course = intent.getIntExtra("course", 1);
        group = intent.getIntExtra("group", 1);
        subgroup = intent.getIntExtra("subgroup", 1);

        topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setTitle(String.format("Группа %s.%s.%s", course, group, subgroup));
        drawerLayout = findViewById(R.id.drawerLayout);
        topAppBar.setNavigationOnClickListener(new DrawerLayoutListener());

        navView = findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(new NavViewListener());

        updateTextSize();

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            UpdateSchedule updateSchedule = new UpdateSchedule();
            updateSchedule.execute();

            fragmentManager.beginTransaction().replace(
                    R.id.fragment_view,
                    ScheduleFragment.newInstance(flowLvl, course, group, subgroup)
            ).commit();
            navView.setCheckedItem(R.id.nav_schedule);
        }
    }

    public void updateTextSize() {
        switch (SettingsStorage.textSize) {
            case 0:
                topAppBar.setTitleTextAppearance(this,
                        com.google.android.material.R.style.
                                TextAppearance_MaterialComponents_Subtitle1
                );
                navView.setItemTextAppearance(R.style.NavigationViewSmall);
                break;
            case 1:
                topAppBar.setTitleTextAppearance(this,
                        com.google.android.material.R.style.
                                TextAppearance_MaterialComponents_Headline5
                );
                navView.setItemTextAppearance(R.style.NavigationViewMedium);
                break;
            case 2:
                topAppBar.setTitleTextAppearance(this,
                        com.google.android.material.R.style.
                                TextAppearance_MaterialComponents_Headline4
                );
                navView.setItemTextAppearance(R.style.NavigationViewBig);
                break;
        }
    }

    public void updateTimer() {
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment instanceof ScheduleFragment) ((ScheduleFragment) fragment).addTimer();
        }
    }

    public void setNewHomeworkFragment() {
        fragmentManager.beginTransaction().replace(
                R.id.fragment_view,
                NewHomeworkFragment.newInstance(flowLvl, course, group, subgroup)
        ).commit();
        navView.setCheckedItem(R.id.nav_homework);
    }

    private class DrawerLayoutListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            drawerLayout.open();
        }
    }

    private class NavViewListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.isChecked()) {
                drawerLayout.close();
                return false;
            }
            drawerLayout.close();
            switch (item.getItemId()) {
                case R.id.nav_schedule:
                    fragmentManager.beginTransaction().replace(
                            R.id.fragment_view,
                            ScheduleFragment.newInstance(flowLvl, course, group, subgroup)
                    ).commit();
                    return true;
                case R.id.nav_change_schedule:
                    fragmentManager.beginTransaction().replace(
                            R.id.fragment_view,
                            ChangeScheduleFragment.newInstance(flowLvl, course, group, subgroup)
                    ).commit();
                    return true;
                case R.id.nav_temp_schedule:
                    fragmentManager.beginTransaction().replace(
                            R.id.fragment_view,
                            TempScheduleFragment.newInstance(flowLvl, course, group, subgroup)
                    ).commit();
                    return true;
                case R.id.nav_homework:
                    fragmentManager.beginTransaction().replace(
                            R.id.fragment_view,
                            HomeworkFragment.newInstance(flowLvl, course, group, subgroup)
                    ).commit();
                    return true;
                case R.id.nav_settings:
                    fragmentManager.beginTransaction().replace(
                            R.id.fragment_view,
                            SettingsFragment.newInstance(flowLvl, course, group, subgroup)
                    ).commit();
                    return true;
                default:
                    return false;
            }
        }
    }

    private class UpdateSchedule extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            BackendService backendService = null;
            try {
                backendService = RetrofitHelper.getBackendService();
            } catch (Exception e) {
                return null;
            }

            ScheduleDBHelper dbHelper = new ScheduleDBHelper(ScheduleActivity.this);
            FlowRepo flowRepo = new FlowRepo(dbHelper);
            LessonRepo lessonRepo = new LessonRepo(dbHelper);
            HomeworkRepo homeworkRepo = new HomeworkRepo(dbHelper, flowRepo);
            ScheduleRepo scheduleRepo = new ScheduleRepo(dbHelper, flowRepo, lessonRepo);
            TempScheduleRepo tempScheduleRepo = new TempScheduleRepo(
                    dbHelper, flowRepo, lessonRepo
            );

            try {
                Call<FlowResponse> flowCall = backendService.flow(
                        flowLvl, course, group, subgroup
                );
                Response<FlowResponse> flowResponse = flowCall.execute();
                FlowResponse backFlow = flowResponse.body();

                Flow foundFlow = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
                        flowLvl, course, group, subgroup
                );

                if (foundFlow.getLastEdit().isBefore(backFlow.getLastEdit())) {
                    Call<List<ScheduleResponse>> scheduleCall = backendService.schedules(
                            flowLvl, course, group, subgroup
                    );
                    Response<List<ScheduleResponse>> scheduleResponse = scheduleCall.execute();
                    List<ScheduleResponse> schedules = scheduleResponse.body();

                    schedules.forEach((e) -> scheduleRepo.addOrUpdate(
                            flowLvl, course, group, subgroup, e.getLesson().getName(),
                            e.getLesson().getTeacher(), e.getLesson().getCabinet(),
                            e.getDayOfWeek(), e.getLessonNum(), e.isNumerator()
                    ));
                    flowRepo.update(flowLvl, course, group, subgroup, LocalDateTime.now());
                }
            } catch (Exception e) {
                Log.e("Backend", e.toString());
            }

            try {
                Call<List<HomeworkResponse>> call = backendService.homeworks(
                        flowLvl, course, group, subgroup
                );
                Response<List<HomeworkResponse>> response = call.execute();
                List<HomeworkResponse> homeworks = response.body();

                homeworks.forEach((e) -> homeworkRepo.addOrUpdate(
                        flowLvl, course, group, subgroup, e.getLessonName(), e.getHomework(),
                        e.getLessonDate(), e.getLessonNum()
                ));
            } catch (Exception e) {
                Log.e("Backend", e.toString());
            }

            try {
                Call<List<TempScheduleResponse>> call = backendService.tempSchedules(
                        flowLvl, course, group, subgroup
                );
                Response<List<TempScheduleResponse>> response = call.execute();
                List<TempScheduleResponse> tempSchedules = response.body();

                tempSchedules.forEach((e) -> tempScheduleRepo.addOrUpdate(
                        flowLvl, course, group, subgroup, e.getLesson().getName(),
                        e.getLesson().getTeacher(), e.getLesson().getCabinet(),
                        e.getLessonDate(), e.getLessonNum(), e.isWillLessonBe()
                ));
            } catch (Exception e) {
                Log.e("Backend", e.toString());
            }

            fragmentManager.beginTransaction().replace(
                    R.id.fragment_view,
                    ScheduleFragment.newInstance(flowLvl, course, group, subgroup)
            ).commit();
            navView.setCheckedItem(R.id.nav_schedule);

            return null;
        }
    }
}