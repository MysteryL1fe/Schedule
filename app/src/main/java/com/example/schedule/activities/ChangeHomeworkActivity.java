package com.example.schedule.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.schedule.R;
import com.example.schedule.ScheduleDBHelper;
import com.example.schedule.SettingsStorage;

public class ChangeHomeworkActivity extends AppCompatActivity {
    private EditText homeworkEditText;
    private int flowLvl, course, group, subgroup, year, month, day, lessonNum;
    private String lessonName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_homework);

        Intent intent = getIntent();
        flowLvl = intent.getIntExtra("flowLvl", 0);
        course = intent.getIntExtra("course", 0);
        group = intent.getIntExtra("group", 0);
        subgroup = intent.getIntExtra("subgroup", 0);
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        day = intent.getIntExtra("day", 0);
        lessonNum = intent.getIntExtra("lessonNum", 0);
        lessonName = intent.getStringExtra("lessonName");
        String homework = intent.getStringExtra("homework");

        homeworkEditText = findViewById(R.id.homeworkEditText);
        Button cancelBtn = findViewById(R.id.cancelBtn);
        Button nextBtn = findViewById(R.id.nextBtn);

        switch (SettingsStorage.textSize) {
            case 0:
                homeworkEditText.setTextSize(10.0f);
                cancelBtn.setTextSize(10.0f);
                nextBtn.setTextSize(10.0f);
                break;
            case 1:
                homeworkEditText.setTextSize(20.0f);
                cancelBtn.setTextSize(20.0f);
                nextBtn.setTextSize(20.0f);
                break;
            case 2:
                homeworkEditText.setTextSize(30.0f);
                cancelBtn.setTextSize(30.0f);
                nextBtn.setTextSize(30.0f);
                break;
        }

        homeworkEditText.setText(homework);

        cancelBtn.setOnClickListener(new CancelBtnListener());
        nextBtn.setOnClickListener(new NextBtnListener());
    }

    private class CancelBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    private class NextBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (homeworkEditText.getText().toString().trim().isEmpty())
                return;
            ScheduleDBHelper dbHelper = new ScheduleDBHelper(ChangeHomeworkActivity.this);
            dbHelper.addOrUpdateHomework(
                    flowLvl, course, group, subgroup, year, month, day, lessonNum, lessonName,
                    homeworkEditText.getText().toString()
            );
            finish();
        }
    }
}