package com.example.schedule.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.schedule.fragments.ChangeScheduleFragment;
import com.example.schedule.R;
import com.example.schedule.fragments.ScheduleFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class ScheduleActivity extends AppCompatActivity {
    private int flowLvl, course, group, subgroup;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private FragmentManager fragmentManager;
    private ActivityResultLauncher<Intent> changeLessonActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Intent intent = getIntent();
        flowLvl = intent.getIntExtra("flowLvl", 0);
        course = intent.getIntExtra("course", 1);
        group = intent.getIntExtra("group", 1);
        subgroup = intent.getIntExtra("subgroup", 1);

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setTitle(String.format("Группа %s.%s.%s", course, group, subgroup));
        drawerLayout = findViewById(R.id.drawerLayout);
        topAppBar.setNavigationOnClickListener(new DrawerLayoutListener());

        navView = findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(new NavViewListener());

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_view,
                ScheduleFragment.newInstance(flowLvl, course, group, subgroup)).commit();
        navView.setCheckedItem(R.id.nav_schedule);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navView.setCheckedItem(R.id.nav_schedule);
    }

    public int getFlowLvl() {
        return flowLvl;
    }

    public int getCourse() {
        return course;
    }

    public int getGroup() {
        return group;
    }

    public int getSubgroup() {
        return subgroup;
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
            navView.setCheckedItem(item);
            switch (item.getItemId()) {
                case R.id.nav_schedule:
                    fragmentManager.beginTransaction().replace(R.id.fragment_view,
                            ScheduleFragment.newInstance(flowLvl, course, group, subgroup))
                            .commit();
                    drawerLayout.close();
                    return true;
                case R.id.nav_change_schedule:
                    fragmentManager.beginTransaction().replace(R.id.fragment_view,
                            ChangeScheduleFragment.newInstance(flowLvl, course, group, subgroup))
                            .commit();
                    drawerLayout.close();
                    return true;
                case R.id.nav_settings:
                    drawerLayout.close();
                    return true;
                default:
                    drawerLayout.close();
                    return false;
            }
        }
    }
}