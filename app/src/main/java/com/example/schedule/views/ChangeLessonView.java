package com.example.schedule.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.schedule.LessonStruct;
import com.example.schedule.ScheduleDBHelper;
import com.example.schedule.SettingsStorage;
import com.example.schedule.activities.ChangeLessonActivity;
import com.example.schedule.R;
import com.example.schedule.Utils;
import com.google.android.material.divider.MaterialDivider;

public class ChangeLessonView extends LinearLayout {
    private int flowLvl, course, group, subgroup, dayOfWeek, lessonNum;
    private LessonStruct numerator, denominator;
    private Context context;

    public ChangeLessonView(Context context) {
        super(context);
    }

    public ChangeLessonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChangeLessonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChangeLessonView(Context context, int flowLvl, int course, int group, int subgroup,
                            int dayOfWeek, int lessonNum) {
        super(context);
        init(flowLvl, course, group, subgroup, dayOfWeek, lessonNum);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void init(int flowLvl, int course, int group, int subgroup, int dayOfWeek,
                     int lessonNum) {
        this.flowLvl = flowLvl;
        this.course = course;
        this.group = group;
        this.subgroup = subgroup;
        this.dayOfWeek = dayOfWeek;
        this.lessonNum = lessonNum;

        context = getContext();

        ScheduleDBHelper dbHelper = new ScheduleDBHelper(context);
        numerator = dbHelper.getLesson(
                flowLvl, course, group, subgroup, dayOfWeek, lessonNum, true
        );
        denominator = dbHelper.getLesson(
                flowLvl, course, group, subgroup, dayOfWeek, lessonNum, false
        );
        dbHelper.close();

        String numeratorTeacher = "", denominatorTeacher = "";
        if (numerator != null && numerator.surname != null && !numerator.surname.isEmpty()) {
            numeratorTeacher = numerator.surname.substring(0, 1).toUpperCase()
                    + numerator.surname.substring(1);
            if (numerator.teacherName != null && !numerator.teacherName.isEmpty()) {
                numeratorTeacher += " " + numerator.teacherName.substring(0, 1).toUpperCase() + ".";
            }
            if (numerator.patronymic != null && !numerator.patronymic.isEmpty()) {
                numeratorTeacher += " " + numerator.patronymic.substring(0, 1).toUpperCase() + ".";
            }
        }
        if (denominator != null && denominator.surname != null && !denominator.surname.isEmpty()) {
            denominatorTeacher = denominator.surname.substring(0, 1).toUpperCase()
                    + denominator.surname.substring(1);
            if (denominator.teacherName != null && !denominator.teacherName.isEmpty()) {
                denominatorTeacher += " " + denominator.teacherName.substring(0, 1).toUpperCase()
                        + ".";
            }
            if (denominator.patronymic != null && !denominator.patronymic.isEmpty()) {
                denominatorTeacher += " " + denominator.patronymic.substring(0, 1).toUpperCase()
                        + ".";
            }
        }

        LayoutParams paramsMatchWrap = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );
        LayoutParams paramsWrapWrap = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        LayoutParams paramsLessonData = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT,
                1.0f
        );
        LayoutParams paramsChangeButtons = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        paramsChangeButtons.rightMargin = 10;

        this.setLayoutParams(paramsMatchWrap);
        this.setGravity(Gravity.START);
        this.setOrientation(VERTICAL);
        this.setBackground(
                getResources().getDrawable(R.drawable.lesson_background, context.getTheme())
        );

        LinearLayout firstStroke = new LinearLayout(context);
        firstStroke.setLayoutParams(paramsMatchWrap);
        firstStroke.setOrientation(HORIZONTAL);
        firstStroke.setPadding(0, 10, 0, 10);
        this.addView(firstStroke);

        TextView lessonNumTV = new TextView(context);
        lessonNumTV.setLayoutParams(paramsWrapWrap);
        lessonNumTV.setText(String.format("%s", lessonNum));
        lessonNumTV.setGravity(Gravity.START);
        lessonNumTV.setBackgroundResource(R.drawable.behind_lesson_num);
        lessonNumTV.setPadding(75, 5, 15, 5);
        firstStroke.addView(lessonNumTV);

        TextView timeTV = new TextView(context);
        timeTV.setLayoutParams(paramsMatchWrap);
        timeTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_END);
        timeTV.setText(Utils.getTimeByLesson(lessonNum));
        timeTV.setGravity(Gravity.END);
        timeTV.setPadding(0, 0, 25, 0);
        firstStroke.addView(timeTV);

        switch (SettingsStorage.textSize) {
            case 0:
                lessonNumTV.setTextSize(8.0f);
                timeTV.setTextSize(8.0f);
                break;
            case 2:
                lessonNumTV.setTextSize(24.0f);
                timeTV.setTextSize(24.0f);
                break;
            default:
                lessonNumTV.setTextSize(16.0f);
                timeTV.setTextSize(16.0f);
                break;
        }

        if (numerator != null && numerator.lessonName != null && !numerator.lessonName.isEmpty()
                && denominator != null && denominator.lessonName != null &&
                !denominator.lessonName.isEmpty() && numerator.equals(denominator)) {
            LinearLayout secondStroke = new LinearLayout(context);
            secondStroke.setLayoutParams(paramsMatchWrap);
            secondStroke.setOrientation(HORIZONTAL);
            secondStroke.setPadding(0, 10, 0, 10);
            this.addView(secondStroke);

            LinearLayout lessonData = new LinearLayout(context);
            lessonData.setLayoutParams(paramsLessonData);
            lessonData.setOrientation(VERTICAL);
            secondStroke.addView(lessonData);

            TextView lessonTV = new TextView(context);
            lessonTV.setLayoutParams(paramsMatchWrap);
            lessonTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            switch (SettingsStorage.textSize) {
                case 0:
                    lessonTV.setTextSize(8.0f);
                    break;
                case 2:
                    lessonTV.setTextSize(24.0f);
                    break;
                default:
                    lessonTV.setTextSize(16.0f);
                    break;
            }
            lessonTV.setText(numerator.lessonName);
            lessonTV.setPadding(50, 0, 0, 0);
            lessonData.addView(lessonTV);

            if (!numerator.cabinet.isEmpty() || !numeratorTeacher.isEmpty()) {
                TextView cabinetTV = new TextView(context);
                cabinetTV.setLayoutParams(paramsMatchWrap);
                cabinetTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                switch (SettingsStorage.textSize) {
                    case 0:
                        cabinetTV.setTextSize(8.0f);
                        break;
                    case 2:
                        cabinetTV.setTextSize(24.0f);
                        break;
                    default:
                        cabinetTV.setTextSize(16.0f);
                        break;
                }
                if (numeratorTeacher.isEmpty())
                    cabinetTV.setText(numerator.cabinet);
                else if (numerator.cabinet.isEmpty())
                    cabinetTV.setText(numeratorTeacher);
                else
                    cabinetTV.setText(
                            String.format("%s, %s", numerator.cabinet, numeratorTeacher));
                cabinetTV.setPadding(50, 0, 0, 25);
                lessonData.addView(cabinetTV);
            }

            LinearLayout changeRow = new LinearLayout(context);
            changeRow.setLayoutParams(paramsWrapWrap);
            changeRow.setOrientation(HORIZONTAL);
            changeRow.setPadding(25, 0, 0, 0);
            secondStroke.addView(changeRow);

            ImageButton changeLessonBtn = new ImageButton(context);
            changeLessonBtn.setLayoutParams(paramsChangeButtons);
            changeLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_rename));
            changeLessonBtn.setBackground(
                    getResources().getDrawable(R.drawable.secondary_color, context.getTheme())
            );
            changeLessonBtn.setPadding(10, 10, 10, 10);
            changeLessonBtn.setOnClickListener(
                    new ChangeLessonBtnListener(true,true));
            changeRow.addView(changeLessonBtn);

            ImageButton deleteLessonBtn = new ImageButton(context);
            deleteLessonBtn.setLayoutParams(paramsChangeButtons);
            deleteLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_thrash));
            deleteLessonBtn.setBackground(
                    getResources().getDrawable(R.drawable.secondary_color, context.getTheme())
            );
            deleteLessonBtn.setPadding(10, 10, 10, 10);
            deleteLessonBtn.setOnClickListener(
                    new DeleteLessonBtnListener(true, true));
            changeRow.addView(deleteLessonBtn);
        }
        else if (numerator != null && numerator.lessonName != null
                && !numerator.lessonName.isEmpty() && denominator != null
                && denominator.lessonName != null && !denominator.lessonName.isEmpty()) {
            LinearLayout secondStroke = new LinearLayout(context);
            secondStroke.setLayoutParams(paramsMatchWrap);
            secondStroke.setOrientation(HORIZONTAL);
            secondStroke.setPadding(0, 10, 0, 10);
            this.addView(secondStroke);

            LinearLayout numeratorLessonData = new LinearLayout(context);
            numeratorLessonData.setLayoutParams(paramsLessonData);
            numeratorLessonData.setOrientation(VERTICAL);
            secondStroke.addView(numeratorLessonData);

            TextView numeratorLessonTV = new TextView(context);
            numeratorLessonTV.setLayoutParams(paramsMatchWrap);
            numeratorLessonTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            switch (SettingsStorage.textSize) {
                case 0:
                    numeratorLessonTV.setTextSize(8.0f);
                    break;
                case 2:
                    numeratorLessonTV.setTextSize(24.0f);
                    break;
                default:
                    numeratorLessonTV.setTextSize(16.0f);
                    break;
            }
            numeratorLessonTV.setText(numerator.lessonName);
            numeratorLessonTV.setPadding(50, 0, 0, 0);
            numeratorLessonData.addView(numeratorLessonTV);

            if (!numerator.cabinet.isEmpty() || !numeratorTeacher.isEmpty()) {
                TextView cabinetTV = new TextView(context);
                cabinetTV.setLayoutParams(paramsMatchWrap);
                cabinetTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                switch (SettingsStorage.textSize) {
                    case 0:
                        cabinetTV.setTextSize(8.0f);
                        break;
                    case 2:
                        cabinetTV.setTextSize(24.0f);
                        break;
                    default:
                        cabinetTV.setTextSize(16.0f);
                        break;
                }
                if (numeratorTeacher.isEmpty())
                    cabinetTV.setText(numerator.cabinet);
                else if (numerator.cabinet.isEmpty())
                    cabinetTV.setText(numeratorTeacher);
                else
                    cabinetTV.setText(
                            String.format("%s, %s", numerator.cabinet, numeratorTeacher));
                cabinetTV.setPadding(50, 0, 0, 25);
                numeratorLessonData.addView(cabinetTV);
            }

            LinearLayout numeratorChangeRow = new LinearLayout(context);
            numeratorChangeRow.setLayoutParams(paramsWrapWrap);
            numeratorChangeRow.setOrientation(HORIZONTAL);
            numeratorChangeRow.setPadding(25, 0, 0, 0);
            secondStroke.addView(numeratorChangeRow);

            ImageButton numeratorChangeLessonBtn = new ImageButton(context);
            numeratorChangeLessonBtn.setLayoutParams(paramsChangeButtons);
            numeratorChangeLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_rename));
            numeratorChangeLessonBtn.setBackground(
                    getResources().getDrawable(R.drawable.secondary_color, context.getTheme())
            );
            numeratorChangeLessonBtn.setPadding(10, 10, 10, 10);
            numeratorChangeLessonBtn.setOnClickListener(
                    new ChangeLessonBtnListener(true, false));
            numeratorChangeRow.addView(numeratorChangeLessonBtn);

            ImageButton numeratorDeleteLessonBtn = new ImageButton(context);
            numeratorDeleteLessonBtn.setLayoutParams(paramsChangeButtons);
            numeratorDeleteLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_thrash));
            numeratorDeleteLessonBtn.setBackground(
                    getResources().getDrawable(R.drawable.secondary_color, context.getTheme())
            );
            numeratorDeleteLessonBtn.setPadding(10, 10, 10, 10);
            numeratorDeleteLessonBtn.setOnClickListener(
                    new DeleteLessonBtnListener(true, false));
            numeratorChangeRow.addView(numeratorDeleteLessonBtn);

            MaterialDivider divider = new MaterialDivider(context);
            this.addView(divider);

            LinearLayout thirdStroke = new LinearLayout(context);
            thirdStroke.setLayoutParams(paramsMatchWrap);
            thirdStroke.setOrientation(HORIZONTAL);
            thirdStroke.setPadding(0, 10, 0, 10);
            this.addView(thirdStroke);

            LinearLayout denominatorLessonData = new LinearLayout(context);
            denominatorLessonData.setLayoutParams(paramsLessonData);
            denominatorLessonData.setOrientation(VERTICAL);
            thirdStroke.addView(denominatorLessonData);

            TextView denominatorLessonTV = new TextView(context);
            denominatorLessonTV.setLayoutParams(paramsMatchWrap);
            denominatorLessonTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            switch (SettingsStorage.textSize) {
                case 0:
                    denominatorLessonTV.setTextSize(8.0f);
                    break;
                case 2:
                    denominatorLessonTV.setTextSize(24.0f);
                    break;
                default:
                    denominatorLessonTV.setTextSize(16.0f);
                    break;
            }
            denominatorLessonTV.setText(denominator.lessonName);
            denominatorLessonTV.setPadding(50, 0, 0, 0);
            denominatorLessonData.addView(denominatorLessonTV);

            if (!denominator.cabinet.isEmpty() || !denominatorTeacher.isEmpty()) {
                TextView cabinetTV = new TextView(context);
                cabinetTV.setLayoutParams(paramsMatchWrap);
                cabinetTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                switch (SettingsStorage.textSize) {
                    case 0:
                        cabinetTV.setTextSize(8.0f);
                        break;
                    case 2:
                        cabinetTV.setTextSize(24.0f);
                        break;
                    default:
                        cabinetTV.setTextSize(16.0f);
                        break;
                }
                if (denominatorTeacher.isEmpty())
                    cabinetTV.setText(denominator.cabinet);
                else if (denominator.cabinet.isEmpty())
                    cabinetTV.setText(denominatorTeacher);
                else
                    cabinetTV.setText(
                            String.format("%s, %s", denominator.cabinet, denominatorTeacher));
                cabinetTV.setPadding(50, 0, 0, 25);
                denominatorLessonData.addView(cabinetTV);
            }

            LinearLayout denominatorChangeRow = new LinearLayout(context);
            denominatorChangeRow.setLayoutParams(paramsWrapWrap);
            denominatorChangeRow.setOrientation(HORIZONTAL);
            denominatorChangeRow.setPadding(25, 0, 0, 0);
            thirdStroke.addView(denominatorChangeRow);

            ImageButton denominatorChangeLessonBtn = new ImageButton(context);
            denominatorChangeLessonBtn.setLayoutParams(paramsChangeButtons);
            denominatorChangeLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_rename));
            denominatorChangeLessonBtn.setBackground(
                    getResources().getDrawable(R.drawable.secondary_color, context.getTheme())
            );
            denominatorChangeLessonBtn.setPadding(10, 10, 10, 10);
            denominatorChangeLessonBtn.setOnClickListener(
                    new ChangeLessonBtnListener(false, true));
            denominatorChangeRow.addView(denominatorChangeLessonBtn);

            ImageButton denominatorDeleteLessonBtn = new ImageButton(context);
            denominatorDeleteLessonBtn.setLayoutParams(paramsChangeButtons);
            denominatorDeleteLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_thrash));
            denominatorDeleteLessonBtn.setBackground(
                    getResources().getDrawable(R.drawable.secondary_color, context.getTheme())
            );
            denominatorDeleteLessonBtn.setPadding(10, 10, 10, 10);
            denominatorDeleteLessonBtn.setOnClickListener(
                    new DeleteLessonBtnListener(false, true));
            denominatorChangeRow.addView(denominatorDeleteLessonBtn);
        } else if (numerator != null && numerator.lessonName != null
                && !numerator.lessonName.isEmpty()) {
            LinearLayout secondStroke = new LinearLayout(context);
            secondStroke.setLayoutParams(paramsMatchWrap);
            secondStroke.setOrientation(HORIZONTAL);
            secondStroke.setPadding(0, 10, 0, 10);
            this.addView(secondStroke);

            LinearLayout numeratorLessonData = new LinearLayout(context);
            numeratorLessonData.setLayoutParams(paramsLessonData);
            numeratorLessonData.setOrientation(VERTICAL);
            secondStroke.addView(numeratorLessonData);

            TextView numeratorLessonTV = new TextView(context);
            numeratorLessonTV.setLayoutParams(paramsMatchWrap);
            numeratorLessonTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            switch (SettingsStorage.textSize) {
                case 0:
                    numeratorLessonTV.setTextSize(8.0f);
                    break;
                case 2:
                    numeratorLessonTV.setTextSize(24.0f);
                    break;
                default:
                    numeratorLessonTV.setTextSize(16.0f);
                    break;
            }
            numeratorLessonTV.setText(numerator.lessonName);
            numeratorLessonTV.setPadding(50, 0, 0, 0);
            numeratorLessonData.addView(numeratorLessonTV);

            if (!numerator.cabinet.isEmpty() || !numeratorTeacher.isEmpty()) {
                TextView cabinetTV = new TextView(context);
                cabinetTV.setLayoutParams(paramsMatchWrap);
                cabinetTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                switch (SettingsStorage.textSize) {
                    case 0:
                        cabinetTV.setTextSize(8.0f);
                        break;
                    case 2:
                        cabinetTV.setTextSize(24.0f);
                        break;
                    default:
                        cabinetTV.setTextSize(16.0f);
                        break;
                }
                if (numeratorTeacher.isEmpty())
                    cabinetTV.setText(numerator.cabinet);
                else if (numerator.cabinet.isEmpty())
                    cabinetTV.setText(numeratorTeacher);
                else
                    cabinetTV.setText(
                            String.format("%s, %s", numerator.cabinet, numeratorTeacher));
                cabinetTV.setPadding(50, 0, 0, 25);
                numeratorLessonData.addView(cabinetTV);
            }

            LinearLayout numeratorChangeRow = new LinearLayout(context);
            numeratorChangeRow.setLayoutParams(paramsWrapWrap);
            numeratorChangeRow.setOrientation(HORIZONTAL);
            numeratorChangeRow.setPadding(25, 0, 0, 0);
            secondStroke.addView(numeratorChangeRow);

            ImageButton numeratorChangeLessonBtn = new ImageButton(context);
            numeratorChangeLessonBtn.setLayoutParams(paramsChangeButtons);
            numeratorChangeLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_rename));
            numeratorChangeLessonBtn.setBackground(
                    getResources().getDrawable(R.drawable.secondary_color, context.getTheme())
            );
            numeratorChangeLessonBtn.setPadding(10, 10, 10, 10);
            numeratorChangeLessonBtn.setOnClickListener(
                    new ChangeLessonBtnListener(true, false));
            numeratorChangeRow.addView(numeratorChangeLessonBtn);

            ImageButton numeratorDeleteLessonBtn = new ImageButton(context);
            numeratorDeleteLessonBtn.setLayoutParams(paramsChangeButtons);
            numeratorDeleteLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_thrash));
            numeratorDeleteLessonBtn.setBackground(
                    getResources().getDrawable(R.drawable.secondary_color, context.getTheme())
            );
            numeratorDeleteLessonBtn.setPadding(10, 10, 10, 10);
            numeratorDeleteLessonBtn.setOnClickListener(
                    new DeleteLessonBtnListener(true, false));
            numeratorChangeRow.addView(numeratorDeleteLessonBtn);

            MaterialDivider divider = new MaterialDivider(context);
            this.addView(divider);

            LinearLayout thirdStroke = new LinearLayout(context);
            thirdStroke.setLayoutParams(paramsMatchWrap);
            thirdStroke.setOrientation(HORIZONTAL);
            thirdStroke.setPadding(0, 10, 0, 10);
            this.addView(thirdStroke);

            LinearLayout denominatorLessonData = new LinearLayout(context);
            denominatorLessonData.setLayoutParams(paramsLessonData);
            denominatorLessonData.setOrientation(VERTICAL);
            thirdStroke.addView(denominatorLessonData);

            LinearLayout denominatorChangeRow = new LinearLayout(context);
            denominatorChangeRow.setLayoutParams(paramsWrapWrap);
            denominatorChangeRow.setOrientation(HORIZONTAL);
            denominatorChangeRow.setPadding(25, 0, 0, 0);
            thirdStroke.addView(denominatorChangeRow);

            ImageButton denominatorChangeLessonBtn = new ImageButton(context);
            denominatorChangeLessonBtn.setLayoutParams(paramsChangeButtons);
            denominatorChangeLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_rename));
            denominatorChangeLessonBtn.setBackground(
                    getResources().getDrawable(R.drawable.secondary_color, context.getTheme())
            );
            denominatorChangeLessonBtn.setPadding(10, 10, 10, 10);
            denominatorChangeLessonBtn.setOnClickListener(
                    new ChangeLessonBtnListener(false, true));
            denominatorChangeRow.addView(denominatorChangeLessonBtn);
        } else if (denominator != null && denominator.lessonName != null
                && !denominator.lessonName.isEmpty()) {
            LinearLayout secondStroke = new LinearLayout(context);
            secondStroke.setLayoutParams(paramsMatchWrap);
            secondStroke.setOrientation(HORIZONTAL);
            secondStroke.setPadding(0, 10, 0, 10);
            this.addView(secondStroke);

            LinearLayout numeratorLessonData = new LinearLayout(context);
            numeratorLessonData.setLayoutParams(paramsLessonData);
            numeratorLessonData.setOrientation(VERTICAL);
            secondStroke.addView(numeratorLessonData);

            LinearLayout numeratorChangeRow = new LinearLayout(context);
            numeratorChangeRow.setLayoutParams(paramsWrapWrap);
            numeratorChangeRow.setOrientation(HORIZONTAL);
            numeratorChangeRow.setPadding(25, 0, 0, 0);
            secondStroke.addView(numeratorChangeRow);

            ImageButton numeratorChangeLessonBtn = new ImageButton(context);
            numeratorChangeLessonBtn.setLayoutParams(paramsChangeButtons);
            numeratorChangeLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_rename));
            numeratorChangeLessonBtn.setBackground(
                    getResources().getDrawable(R.drawable.secondary_color, context.getTheme())
            );
            numeratorChangeLessonBtn.setPadding(10, 10, 10, 10);
            numeratorChangeLessonBtn.setOnClickListener(
                    new ChangeLessonBtnListener(true, false));
            numeratorChangeRow.addView(numeratorChangeLessonBtn);

            MaterialDivider divider = new MaterialDivider(context);
            this.addView(divider);

            LinearLayout thirdStroke = new LinearLayout(context);
            thirdStroke.setLayoutParams(paramsMatchWrap);
            thirdStroke.setOrientation(HORIZONTAL);
            thirdStroke.setPadding(0, 10, 0, 10);
            this.addView(thirdStroke);

            LinearLayout denominatorLessonData = new LinearLayout(context);
            denominatorLessonData.setLayoutParams(paramsLessonData);
            denominatorLessonData.setOrientation(VERTICAL);
            thirdStroke.addView(denominatorLessonData);

            TextView denominatorLessonTV = new TextView(context);
            denominatorLessonTV.setLayoutParams(paramsMatchWrap);
            denominatorLessonTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            switch (SettingsStorage.textSize) {
                case 0:
                    denominatorLessonTV.setTextSize(8.0f);
                    break;
                case 2:
                    denominatorLessonTV.setTextSize(24.0f);
                    break;
                default:
                    denominatorLessonTV.setTextSize(16.0f);
                    break;
            }
            denominatorLessonTV.setText(denominator.lessonName);
            denominatorLessonTV.setPadding(50, 0, 0, 0);
            denominatorLessonData.addView(denominatorLessonTV);

            if (!denominator.cabinet.isEmpty() || !denominatorTeacher.isEmpty()) {
                TextView cabinetTV = new TextView(context);
                cabinetTV.setLayoutParams(paramsMatchWrap);
                cabinetTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                switch (SettingsStorage.textSize) {
                    case 0:
                        cabinetTV.setTextSize(8.0f);
                        break;
                    case 2:
                        cabinetTV.setTextSize(24.0f);
                        break;
                    default:
                        cabinetTV.setTextSize(16.0f);
                        break;
                }
                if (denominatorTeacher.isEmpty())
                    cabinetTV.setText(denominator.cabinet);
                else if (denominator.cabinet.isEmpty())
                    cabinetTV.setText(denominatorTeacher);
                else
                    cabinetTV.setText(
                            String.format("%s, %s", denominator.cabinet, denominatorTeacher));
                cabinetTV.setPadding(50, 0, 0, 25);
                denominatorLessonData.addView(cabinetTV);
            }

            LinearLayout denominatorChangeRow = new LinearLayout(context);
            denominatorChangeRow.setLayoutParams(paramsWrapWrap);
            denominatorChangeRow.setOrientation(HORIZONTAL);
            denominatorChangeRow.setPadding(25, 0, 0, 0);
            thirdStroke.addView(denominatorChangeRow);

            ImageButton denominatorChangeLessonBtn = new ImageButton(context);
            denominatorChangeLessonBtn.setLayoutParams(paramsChangeButtons);
            denominatorChangeLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_rename));
            denominatorChangeLessonBtn.setBackground(
                    getResources().getDrawable(R.drawable.secondary_color, context.getTheme())
            );
            denominatorChangeLessonBtn.setPadding(10, 10, 10, 10);
            denominatorChangeLessonBtn.setOnClickListener(
                    new ChangeLessonBtnListener(false, true));
            denominatorChangeRow.addView(denominatorChangeLessonBtn);

            ImageButton denominatorDeleteLessonBtn = new ImageButton(context);
            denominatorDeleteLessonBtn.setLayoutParams(paramsChangeButtons);
            denominatorDeleteLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_thrash));
            denominatorDeleteLessonBtn.setBackground(
                    getResources().getDrawable(R.drawable.secondary_color, context.getTheme())
            );
            denominatorDeleteLessonBtn.setPadding(10, 10, 10, 10);
            denominatorDeleteLessonBtn.setOnClickListener(
                    new DeleteLessonBtnListener(false, true));
            denominatorChangeRow.addView(denominatorDeleteLessonBtn);
        }
        else {
            LinearLayout secondStroke = new LinearLayout(context);
            secondStroke.setLayoutParams(paramsMatchWrap);
            secondStroke.setOrientation(HORIZONTAL);
            secondStroke.setPadding(0, 10, 0, 10);
            this.addView(secondStroke);

            LinearLayout lessonData = new LinearLayout(context);
            lessonData.setLayoutParams(paramsLessonData);
            lessonData.setOrientation(VERTICAL);
            secondStroke.addView(lessonData);

            LinearLayout changeRow = new LinearLayout(context);
            changeRow.setLayoutParams(paramsWrapWrap);
            changeRow.setOrientation(VERTICAL);
            changeRow.setPadding(25, 0, 0, 0);
            secondStroke.addView(changeRow);

            ImageButton changeLessonBtn = new ImageButton(context);
            changeLessonBtn.setLayoutParams(paramsChangeButtons);
            changeLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_rename));
            changeLessonBtn.setBackground(
                    getResources().getDrawable(R.drawable.secondary_color, context.getTheme())
            );
            changeLessonBtn.setPadding(10, 10, 10, 10);
            changeLessonBtn.setOnClickListener(
                    new ChangeLessonBtnListener(true, true));
            changeRow.addView(changeLessonBtn);
        }
    }

    private class DeleteLessonBtnListener implements View.OnClickListener {
        private final boolean isNumerator, isDenominator;

        public DeleteLessonBtnListener(boolean isNumerator, boolean isDenominator) {
            this.isNumerator = isNumerator;
            this.isDenominator = isDenominator;
        }

        @Override
        public void onClick(View v) {
            try {
                ScheduleDBHelper dbHelper = new ScheduleDBHelper(context);
                if (isNumerator) dbHelper.deleteSchedule(
                        flowLvl, course, group, subgroup, dayOfWeek, lessonNum, true
                );
                if (isDenominator) dbHelper.deleteSchedule(
                        flowLvl, course, group, subgroup, dayOfWeek, lessonNum, false
                );
                dbHelper.close();
                ChangeLessonView.this.removeAllViews();
                init(flowLvl, course, group, subgroup, dayOfWeek, lessonNum);
            } catch (Exception ignored) {}
        }
    }

    private class ChangeLessonBtnListener implements View.OnClickListener {
        private final boolean isNumerator, isDenominator;

        public ChangeLessonBtnListener(boolean isNumerator, boolean isDenominator) {
            this.isNumerator = isNumerator;
            this.isDenominator = isDenominator;
        }

        @Override
        public void onClick(View v) {
            String[] teacher = new ScheduleDBHelper(getContext()).getTeacher(
                    flowLvl, course, group, subgroup, dayOfWeek, lessonNum, isNumerator
            );
            Intent intent = new Intent(context, ChangeLessonActivity.class);
            intent.putExtra("flowLvl", flowLvl);
            intent.putExtra("course", course);
            intent.putExtra("group", group);
            intent.putExtra("subgroup", subgroup);
            intent.putExtra("dayOfWeek", dayOfWeek);
            intent.putExtra("lessonNum", lessonNum);
            intent.putExtra("isNumerator", isNumerator);
            intent.putExtra("isDenominator", isDenominator);
            intent.putExtra("lessonName", isNumerator && numerator != null ?
                    numerator.lessonName : isDenominator && denominator != null ?
                    denominator.lessonName : "");
            intent.putExtra("cabinet", isNumerator && numerator != null ?
                    numerator.cabinet : isDenominator && denominator != null ?
                    denominator.cabinet : "");
            intent.putExtra("surname", teacher == null ? "" : teacher[0]);
            intent.putExtra("teacherName", teacher == null ? "" : teacher[1]);
            intent.putExtra("patronymic", teacher == null ? "" : teacher[2]);
            context.startActivity(intent);
        }
    }
}
