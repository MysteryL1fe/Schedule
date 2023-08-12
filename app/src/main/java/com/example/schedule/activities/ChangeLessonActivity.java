package com.example.schedule.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.schedule.R;
import com.example.schedule.ScheduleDBHelper;
import com.example.schedule.SettingsStorage;

public class ChangeLessonActivity extends AppCompatActivity {
    private EditText lessonNameEditText, cabinetEditText;
    private EditText surnameEditText, teacherNameEditText, patronymicEditText;
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
        String lessonName = intent.getStringExtra("lessonName");
        String surname = intent.getStringExtra("surname");
        String teacherName = intent.getStringExtra("teacherName");
        String patronymic = intent.getStringExtra("patronymic");
        String cabinet = intent.getStringExtra("cabinet");

        lessonNameEditText = findViewById(R.id.lessonNameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        teacherNameEditText = findViewById(R.id.teacherNameEditText);
        patronymicEditText = findViewById(R.id.patronymicEditText);
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
                surnameEditText.setTextSize(10.0f);
                teacherNameEditText.setTextSize(10.0f);
                patronymicEditText.setTextSize(10.0f);
                cabinetEditText.setTextSize(10.0f);
                cancelBtn.setTextSize(10.0f);
                nextBtn.setTextSize(10.0f);
                isNumeratorCheckBox.setTextSize(10.0f);
                isDenominatorCheckBox.setTextSize(10.0f);
                break;
            case 1:
                lessonNameEditText.setTextSize(20.0f);
                surnameEditText.setTextSize(20.0f);
                teacherNameEditText.setTextSize(20.0f);
                patronymicEditText.setTextSize(20.0f);
                cabinetEditText.setTextSize(20.0f);
                cancelBtn.setTextSize(20.0f);
                nextBtn.setTextSize(20.0f);
                isNumeratorCheckBox.setTextSize(20.0f);
                isDenominatorCheckBox.setTextSize(20.0f);
                break;
            case 2:
                lessonNameEditText.setTextSize(30.0f);
                surnameEditText.setTextSize(30.0f);
                teacherNameEditText.setTextSize(30.0f);
                patronymicEditText.setTextSize(30.0f);
                cabinetEditText.setTextSize(30.0f);
                cancelBtn.setTextSize(30.0f);
                nextBtn.setTextSize(30.0f);
                isNumeratorCheckBox.setTextSize(30.0f);
                isDenominatorCheckBox.setTextSize(30.0f);
                break;
        }

        lessonNameEditText.setText(lessonName);
        surnameEditText.setText(
                surname == null || surname.isEmpty() ? ""
                        : surname.substring(0, 1).toUpperCase() + surname.substring(1)
        );
        teacherNameEditText.setText(
                teacherName == null || teacherName.isEmpty() ? ""
                        : teacherName.substring(0, 1).toUpperCase() + teacherName.substring(1)
        );
        patronymicEditText.setText(
                patronymic == null || patronymic.isEmpty() ? ""
                        : patronymic.substring(0, 1).toUpperCase() + patronymic.substring(1)
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
            ScheduleDBHelper dbHelper = new ScheduleDBHelper(ChangeLessonActivity.this);
            if (isTempView) {
                dbHelper.addOrUpdateTempSchedule(
                        flowLvl, course, group, subgroup, year, month, day, lessonNum,
                        lessonNameEditText.getText().toString(),
                        cabinetEditText.getText().toString(),
                        surnameEditText.getText().toString(),
                        teacherNameEditText.getText().toString(),
                        patronymicEditText.getText().toString()
                );

            } else {
                if (isNumeratorCheckBox.isChecked()) {
                    dbHelper.addOrUpdateSchedule(
                            flowLvl, course, group, subgroup, dayOfWeek, lessonNum, true,
                            lessonNameEditText.getText().toString(),
                            cabinetEditText.getText().toString(),
                            surnameEditText.getText().toString(),
                            teacherNameEditText.getText().toString(),
                            patronymicEditText.getText().toString()
                    );
                } else if (isNumerator) {
                    dbHelper.deleteSchedule(
                            flowLvl, course, group, subgroup, dayOfWeek, lessonNum, true
                    );
                }
                if (isDenominatorCheckBox.isChecked()) {
                    dbHelper.addOrUpdateSchedule(
                            flowLvl, course, group, subgroup, dayOfWeek, lessonNum, false,
                            lessonNameEditText.getText().toString(),
                            cabinetEditText.getText().toString(),
                            surnameEditText.getText().toString(),
                            teacherNameEditText.getText().toString(),
                            patronymicEditText.getText().toString()
                    );
                } else if (isDenominator) {
                    dbHelper.deleteSchedule(
                            flowLvl, course, group, subgroup, dayOfWeek, lessonNum, false
                    );
                }
            }
            dbHelper.close();
            finish();
        }
    }
}