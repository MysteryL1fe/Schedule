package com.example.schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.schedule.exceptions.ScheduleException;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.util.Set;

public class ChangeScheduleActivity extends AppCompatActivity {
    private int flowLvl, course, group, subgroup;
    private Schedule schedule;
    private LinearLayout lessonsContainer;
    private DrawerLayout drawerLayout;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_schedule);

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

        topAppBar.setTitle(String.format("Группа %s.%s.%s", course, group, subgroup));
        drawerLayout = findViewById(R.id.drawerLayout);
        topAppBar.setNavigationOnClickListener(new DrawerLayoutListener());

        navView = findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(new NavViewListener());
        navView.setCheckedItem(0);
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
            switch (item.getItemId()) {
                case 0:
                    finish();
                case 2:
                default:
                    drawerLayout.close();
                    return false;
            }
        }
    }
}