package com.example.schedule.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.schedule.R;
import com.example.schedule.ScheduleStorage;
import com.example.schedule.SettingsStorage;
import com.example.schedule.exceptions.ScheduleException;

public class ChangeLessonActivity extends AppCompatActivity {
    private EditText lessonNameEditText, teacherEditText, cabinetEditText;
    private Button cancelBtn, nextBtn;
    private int flowLvl, course, group, subgroup, dayOfWeek, lessonNum;
    private boolean isNumerator, isDenominator;
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
        dayOfWeek = intent.getIntExtra("dayOfWeek", 0);
        lessonNum = intent.getIntExtra("lessonNum", 0);
        isNumerator = intent.getBooleanExtra("isNumerator", true);
        isDenominator = intent.getBooleanExtra("isDenominator", true);
        String lessonName = intent.getStringExtra("lessonName");
        String teacher = intent.getStringExtra("teacher");
        String cabinet = intent.getStringExtra("cabinet");

        lessonNameEditText = findViewById(R.id.lesson_name_edit_text);
        teacherEditText = findViewById(R.id.teacher_edit_text);
        cabinetEditText = findViewById(R.id.cabinet_edit_text);
        cancelBtn = findViewById(R.id.cancel_btn);
        nextBtn = findViewById(R.id.next_btn);
        isNumeratorCheckBox = findViewById(R.id.isNumeratorCheckBox);
        isDenominatorCheckBox = findViewById(R.id.isDenominatorCheckBox);

        switch (SettingsStorage.TEXT_SIZE) {
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

        lessonNameEditText.setText(lessonName);
        teacherEditText.setText(teacher);
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
            try {
                if (lessonNameEditText.getText().toString().isEmpty() ||
                        !(isNumeratorCheckBox.isChecked() || isDenominatorCheckBox.isChecked()))
                    return;
                if (isNumeratorCheckBox.isChecked()) ScheduleStorage.changeLesson(
                        flowLvl, course, group, subgroup, dayOfWeek, lessonNum,
                        true, lessonNameEditText.getText().toString(),
                        teacherEditText.getText().toString(), cabinetEditText.getText().toString(),
                        getSharedPreferences(SettingsStorage.SCHEDULE_SAVES, MODE_PRIVATE));
                else if (isNumerator) ScheduleStorage.changeLesson(
                        flowLvl, course, group, subgroup, dayOfWeek, lessonNum,
                        true, "", "", "",
                        getSharedPreferences(SettingsStorage.SCHEDULE_SAVES, MODE_PRIVATE));
                if (isDenominatorCheckBox.isChecked()) ScheduleStorage.changeLesson(
                        flowLvl, course, group, subgroup, dayOfWeek, lessonNum,
                        false, lessonNameEditText.getText().toString(),
                        teacherEditText.getText().toString(), cabinetEditText.getText().toString(),
                        getSharedPreferences(SettingsStorage.SCHEDULE_SAVES, MODE_PRIVATE));
                else if (isDenominator) ScheduleStorage.changeLesson(
                        flowLvl, course, group, subgroup, dayOfWeek, lessonNum,
                        false, "", "", "",
                        getSharedPreferences(SettingsStorage.SCHEDULE_SAVES, MODE_PRIVATE));
                finish();
            } catch (ScheduleException e) {
                finish();
            }
        }
    }
}