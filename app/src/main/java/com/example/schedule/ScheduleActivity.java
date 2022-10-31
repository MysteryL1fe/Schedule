package com.example.schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class ScheduleActivity extends AppCompatActivity {
    Schedule schedule;
    int subgroup;
    LinearLayout lessonsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Intent intent = getIntent();
        subgroup = intent.getIntExtra("subgroup", 1011);
        schedule = Utils.initSchedule(subgroup);

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        lessonsContainer = findViewById(R.id.lessonsContainer);

        LoadLessons loadLessons = new LoadLessons();
        loadLessons.execute();

        topAppBar.setTitle(String.format("Группа %s.%s.%s", subgroup / 1000,
                subgroup % 1000 / 10, subgroup % 10));
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleActivity.this, LeftMenuActivity.class);
                startActivity(intent);
            }
        });
    }

    private class LoadLessons extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < 21; i++) {
                LessonsView lessonsView = new LessonsView(ScheduleActivity.this);
                lessonsView.init(i, schedule);
                lessonsContainer.addView(lessonsView);
            }
            return null;
        }
    }
}