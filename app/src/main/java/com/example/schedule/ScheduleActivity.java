package com.example.schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.schedule.exceptions.ScheduleException;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Set;

public class ScheduleActivity extends AppCompatActivity {
    private Schedule schedule = null;
    private int flowLvl, course, group, subgroup;
    private LinearLayout lessonsContainer;
    private DrawerLayout drawerLayout;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Intent intent = getIntent();
        flowLvl = intent.getIntExtra("flowLvl", 0);
        course = intent.getIntExtra("course", 1);
        group = intent.getIntExtra("group", 1);
        subgroup = intent.getIntExtra("subgroup", 1);

        Set<Schedule> storage = ScheduleStorage.getStorage();
        for (Schedule sched : storage) {
            if (sched.getFlowLvl() == flowLvl
                    && sched.getCourse() == course
                    && sched.getGroup() == group
                    && sched.getSubgroup() == subgroup) {
                schedule = sched;
                break;
            }
        }

        if (schedule == null) {
            try {
                schedule = new Schedule(flowLvl, course, group, subgroup);
                ScheduleStorage.addSchedule(schedule, getSharedPreferences("ScheduleSaves", MODE_PRIVATE));
            } catch (ScheduleException e) {
                finish();
            }
        }

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        lessonsContainer = findViewById(R.id.lessonsContainer);

        LoadLessons loadLessons = new LoadLessons();
        loadLessons.execute();

        topAppBar.setTitle(String.format("Группа %s.%s.%s", course, group, subgroup));
        drawerLayout = findViewById(R.id.drawerLayout);
        topAppBar.setNavigationOnClickListener(new DrawerLayoutListener());

        navView = findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(new NavViewListener());
    }

    private class LoadLessons extends AsyncTask<Void, Void, Void> {
        private final LessonsView[] lessonsViews = new LessonsView[21];
        /*LinearLayout.LayoutParams lastLessonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout.LayoutParams lessonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );*/

        @Override
        protected Void doInBackground(Void... voids) {

            Calendar calendar = Calendar.getInstance();
            int day, month, year, dayOfWeek;
            boolean isNumerator;

            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
            dayOfWeek = Utils.getDayOfWeek(year, month, day);
            isNumerator = Utils.isNumerator(year, month, day);

            /*lastLessonParams.bottomMargin = 50;

            lessonParams.setMargins(15,0, 15, 0);*/

            for (int i = 0; i < 21; i++) {
                LessonsView lessonsView = new LessonsView(lessonsContainer.getContext(),
                        schedule, day, month, year, dayOfWeek, isNumerator);
                lessonsViews[i] = lessonsView;

                calendar.add(Calendar.DAY_OF_MONTH, 1);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
                if (++dayOfWeek == 8) {
                    dayOfWeek = 1;
                    isNumerator = !isNumerator;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            for (int i = 0; i < 21; i++) {
                lessonsContainer.addView(lessonsViews[i]);
            }
        }
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
            item.setChecked(true);
            drawerLayout.close();
            return true;
        }
    }
}