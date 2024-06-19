package com.example.schedule.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.schedule.R;
import com.example.schedule.SettingsStorage;
import com.example.schedule.repo.ScheduleRepo;
import com.example.schedule.repo.TempScheduleRepo;

import java.time.LocalDate;

public class ChangeLessonActivity extends AppCompatActivity {
    private EditText lessonNameEditText, cabinetEditText, teacherEditText;
    private int flowLvl, course, group, subgroup, dayOfWeek, lessonNum;
    private int year, month, day;
    private boolean isNumerator, isDenominator, isTempView;
    private CheckBox isNumeratorCheckBox, isDenominatorCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_lesson);

        Intent intent = getIntent();
        flowLvl = intent.getIntExtra("flowLvl", 0);
        course = intent.getIntExtra("course", 0);
        group = intent.getIntExtra("group", 0);
        subgroup = intent.getIntExtra("subgroup", 0);
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        day = intent.getIntExtra("day", 0);
        dayOfWeek = intent.getIntExtra("dayOfWeek", 0);
        lessonNum = intent.getIntExtra("lessonNum", 0);
        isNumerator = intent.getBooleanExtra("isNumerator", true);
        isDenominator = intent.getBooleanExtra("isDenominator", true);
        isTempView = intent.getBooleanExtra("isTempView", false);
        String lesson = intent.getStringExtra("lesson");
        String teacher = intent.getStringExtra("teacher");
        String cabinet = intent.getStringExtra("cabinet");

        lessonNameEditText = findViewById(R.id.lessonNameEditText);
        teacherEditText = findViewById(R.id.teacherEditText);
        cabinetEditText = findViewById(R.id.cabinetEditText);
        Button cancelBtn = findViewById(R.id.cancelBtn);
        Button nextBtn = findViewById(R.id.nextBtn);
        isNumeratorCheckBox = findViewById(R.id.isNumeratorCheckBox);
        isDenominatorCheckBox = findViewById(R.id.isDenominatorCheckBox);

        if (isTempView) {
            isNumeratorCheckBox.setVisibility(View.GONE);
            isDenominatorCheckBox.setVisibility(View.GONE);
        }

        switch (SettingsStorage.textSize) {
            case 0:
                lessonNameEditText.setTextSize(10.0f);
                teacherEditText.setTextSize(10.0f);
                cabinetEditText.setTextSize(10.0f);
                cancelBtn.setTextSize(10.0f);
                nextBtn.setTextSize(10.0f);
                isNumeratorCheckBox.setTextSize(10.0f);
                isDenominatorCheckBox.setTextSize(10.0f);
                break;
            case 1:
                lessonNameEditText.setTextSize(20.0f);
                teacherEditText.setTextSize(20.0f);
                cabinetEditText.setTextSize(20.0f);
                cancelBtn.setTextSize(20.0f);
                nextBtn.setTextSize(20.0f);
                isNumeratorCheckBox.setTextSize(20.0f);
                isDenominatorCheckBox.setTextSize(20.0f);
                break;
            case 2:
                lessonNameEditText.setTextSize(30.0f);
                teacherEditText.setTextSize(30.0f);
                cabinetEditText.setTextSize(30.0f);
                cancelBtn.setTextSize(30.0f);
                nextBtn.setTextSize(30.0f);
                isNumeratorCheckBox.setTextSize(30.0f);
                isDenominatorCheckBox.setTextSize(30.0f);
                break;
        }

        lessonNameEditText.setText(lesson);
        teacherEditText.setText(
                teacher == null || teacher.isEmpty() ? ""
                        : teacher.substring(0, 1).toUpperCase() + teacher.substring(1)
        );
        cabinetEditText.setText(cabinet);
        isNumeratorCheckBox.setChecked(isNumerator);
        isDenominatorCheckBox.setChecked(isDenominator);

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
            if (lessonNameEditText.getText().toString().trim().isEmpty() ||
                    !(isNumeratorCheckBox.isChecked() || isDenominatorCheckBox.isChecked()))
                return;
            ScheduleRepo scheduleRepo = new ScheduleRepo(ChangeLessonActivity.this);
            TempScheduleRepo tempScheduleRepo = new TempScheduleRepo(
                    ChangeLessonActivity.this
            );
            if (isTempView) {
                tempScheduleRepo.addOrUpdate(
                        flowLvl, course, group, subgroup, lessonNameEditText.getText().toString(),
                        teacherEditText.getText().toString(), cabinetEditText.getText().toString(),
                        LocalDate.of(year, month, day), lessonNum, true
                );
            } else {
                if (isNumeratorCheckBox.isChecked()) {
                    scheduleRepo.addOrUpdate(
                            flowLvl, course, group, subgroup,
                            lessonNameEditText.getText().toString(),
                            teacherEditText.getText().toString(),
                            cabinetEditText.getText().toString(),
                            dayOfWeek, lessonNum, true
                    );
                } else if (isNumerator) {
                    scheduleRepo.deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
                            flowLvl, course, group, subgroup, dayOfWeek, lessonNum, true
                    );
                }
                if (isDenominatorCheckBox.isChecked()) {
                    scheduleRepo.addOrUpdate(
                            flowLvl, course, group, subgroup,
                            lessonNameEditText.getText().toString(),
                            teacherEditText.getText().toString(),
                            cabinetEditText.getText().toString(), dayOfWeek, lessonNum,
                            false
                    );
                } else if (isDenominator) {
                    scheduleRepo.deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
                            flowLvl, course, group, subgroup, dayOfWeek, lessonNum, false
                    );
                }
            }
            finish();
        }
    }
}