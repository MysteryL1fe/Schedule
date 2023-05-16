package com.example.schedule.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.schedule.LessonStruct;
import com.example.schedule.Schedule;
import com.example.schedule.SettingsStorage;
import com.example.schedule.activities.ChangeLessonActivity;
import com.example.schedule.R;
import com.example.schedule.activities.ScheduleActivity;
import com.example.schedule.ScheduleStorage;
import com.example.schedule.Utils;
import com.example.schedule.exceptions.ScheduleException;
import com.google.android.material.divider.MaterialDivider;

public class ChangeLessonView extends LinearLayout {
    private int dayOfWeek, lessonNum;
    private LessonStruct numerator, denominator;
    private ScheduleActivity scheduleActivity;
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

    public ChangeLessonView(Context context, int dayOfWeek, int lessonNum) {
        super(context);
        init(dayOfWeek, lessonNum);
    }

    public void init(int dayOfWeek, int lessonNum) {
        context = getContext();
        scheduleActivity = (ScheduleActivity) context;
        this.dayOfWeek = dayOfWeek;
        this.lessonNum = lessonNum;
        try {
            Schedule schedule = ScheduleStorage.getSchedule(
                    scheduleActivity.getFlowLvl(),
                    scheduleActivity.getCourse(),
                    scheduleActivity.getGroup(),
                    scheduleActivity.getSubgroup(),
                    scheduleActivity.
                            getSharedPreferences(SettingsStorage.SCHEDULE_SAVES,
                                    Context.MODE_PRIVATE)
            );
            this.numerator = schedule.getLesson(dayOfWeek, lessonNum, true);
            this.denominator = schedule.getLesson(dayOfWeek, lessonNum, false);
        } catch (Exception e) {
            this.numerator = null;
            this.denominator = null;
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
        lessonNumTV.setTextSize(12.0f);
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

        if (numerator != null && numerator.name != null && !numerator.name.isEmpty()
                && denominator != null && denominator.name != null && !denominator.name.isEmpty()
                && numerator.equals(denominator)) {
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
            lessonTV.setTextSize(14.0f);
            lessonTV.setText(numerator.name);
            lessonTV.setPadding(50, 0, 0, 0);
            lessonData.addView(lessonTV);

            if (!numerator.cabinet.isEmpty() || !numerator.teacher.isEmpty()) {
                TextView cabinetTV = new TextView(context);
                cabinetTV.setLayoutParams(paramsMatchWrap);
                cabinetTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                cabinetTV.setTextSize(12.0f);
                if (numerator.teacher.isEmpty())
                    cabinetTV.setText(String.format("%s", numerator.cabinet));
                else if (numerator.cabinet.isEmpty())
                    cabinetTV.setText(String.format("%s", numerator.teacher));
                else
                    cabinetTV.setText(
                            String.format("%s, %s", numerator.cabinet, numerator.teacher));
                cabinetTV.setPadding(50, 0, 0, 25);
                lessonData.addView(cabinetTV);
            }

            LinearLayout changeRow = new LinearLayout(context);
            changeRow.setLayoutParams(paramsWrapWrap);
            changeRow.setOrientation(HORIZONTAL);
            changeRow.setPadding(25, 0, 0, 0);
            secondStroke.addView(changeRow);

            ImageButton changeLessonBtn = new ImageButton(context);
            changeLessonBtn.setLayoutParams(paramsWrapWrap);
            changeLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_rename));
            changeLessonBtn.setOnClickListener(
                    new ChangeLessonBtnListener(true,true));
            changeRow.addView(changeLessonBtn);

            ImageButton deleteLessonBtn = new ImageButton(context);
            deleteLessonBtn.setLayoutParams(paramsWrapWrap);
            deleteLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_thrash));
            deleteLessonBtn.setOnClickListener(
                    new DeleteLessonBtnListener(true, true));
            changeRow.addView(deleteLessonBtn);
        }
        else if (numerator != null && numerator.name != null && !numerator.name.isEmpty()
                && denominator != null && denominator.name != null && !denominator.name.isEmpty()) {
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
            numeratorLessonTV.setTextSize(14.0f);
            numeratorLessonTV.setText(numerator.name);
            numeratorLessonTV.setPadding(50, 0, 0, 0);
            numeratorLessonData.addView(numeratorLessonTV);

            if (!numerator.cabinet.isEmpty() || !numerator.teacher.isEmpty()) {
                TextView cabinetTV = new TextView(context);
                cabinetTV.setLayoutParams(paramsMatchWrap);
                cabinetTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                cabinetTV.setTextSize(12.0f);
                if (numerator.teacher.isEmpty())
                    cabinetTV.setText(String.format("%s", numerator.cabinet));
                else if (numerator.cabinet.isEmpty())
                    cabinetTV.setText(String.format("%s", numerator.teacher));
                else
                    cabinetTV.setText(
                            String.format("%s, %s", numerator.cabinet, numerator.teacher));
                cabinetTV.setPadding(50, 0, 0, 25);
                numeratorLessonData.addView(cabinetTV);
            }

            LinearLayout numeratorChangeRow = new LinearLayout(context);
            numeratorChangeRow.setLayoutParams(paramsWrapWrap);
            numeratorChangeRow.setOrientation(HORIZONTAL);
            numeratorChangeRow.setPadding(25, 0, 0, 0);
            secondStroke.addView(numeratorChangeRow);

            ImageButton numeratorChangeLessonBtn = new ImageButton(context);
            numeratorChangeLessonBtn.setLayoutParams(paramsWrapWrap);
            numeratorChangeLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_rename));
            numeratorChangeLessonBtn.setOnClickListener(
                    new ChangeLessonBtnListener(true, false));
            numeratorChangeRow.addView(numeratorChangeLessonBtn);

            ImageButton numeratorDeleteLessonBtn = new ImageButton(context);
            numeratorDeleteLessonBtn.setLayoutParams(paramsWrapWrap);
            numeratorDeleteLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_thrash));
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
            denominatorLessonTV.setTextSize(14.0f);
            denominatorLessonTV.setText(denominator.name);
            denominatorLessonTV.setPadding(50, 0, 0, 0);
            denominatorLessonData.addView(denominatorLessonTV);

            if (!denominator.cabinet.isEmpty() || !denominator.teacher.isEmpty()) {
                TextView cabinetTV = new TextView(context);
                cabinetTV.setLayoutParams(paramsMatchWrap);
                cabinetTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                cabinetTV.setTextSize(12.0f);
                if (denominator.teacher.isEmpty())
                    cabinetTV.setText(String.format("%s", denominator.cabinet));
                else if (denominator.cabinet.isEmpty())
                    cabinetTV.setText(String.format("%s", denominator.teacher));
                else
                    cabinetTV.setText(
                            String.format("%s, %s", denominator.cabinet, denominator.teacher));
                cabinetTV.setPadding(50, 0, 0, 25);
                denominatorLessonData.addView(cabinetTV);
            }

            LinearLayout denominatorChangeRow = new LinearLayout(context);
            denominatorChangeRow.setLayoutParams(paramsWrapWrap);
            denominatorChangeRow.setOrientation(HORIZONTAL);
            denominatorChangeRow.setPadding(25, 0, 0, 0);
            thirdStroke.addView(denominatorChangeRow);

            ImageButton denominatorChangeLessonBtn = new ImageButton(context);
            denominatorChangeLessonBtn.setLayoutParams(paramsWrapWrap);
            denominatorChangeLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_rename));
            denominatorChangeLessonBtn.setOnClickListener(
                    new ChangeLessonBtnListener(false, true));
            denominatorChangeRow.addView(denominatorChangeLessonBtn);

            ImageButton denominatorDeleteLessonBtn = new ImageButton(context);
            denominatorDeleteLessonBtn.setLayoutParams(paramsWrapWrap);
            denominatorDeleteLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_thrash));
            denominatorDeleteLessonBtn.setOnClickListener(
                    new DeleteLessonBtnListener(false, true));
            denominatorChangeRow.addView(denominatorDeleteLessonBtn);
        }
        else if (numerator != null && numerator.name != null && !numerator.name.isEmpty()) {
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
            numeratorLessonTV.setTextSize(14.0f);
            numeratorLessonTV.setText(numerator.name);
            numeratorLessonTV.setPadding(50, 0, 0, 0);
            numeratorLessonData.addView(numeratorLessonTV);

            if (!numerator.cabinet.isEmpty() || !numerator.teacher.isEmpty()) {
                TextView cabinetTV = new TextView(context);
                cabinetTV.setLayoutParams(paramsMatchWrap);
                cabinetTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                cabinetTV.setTextSize(12.0f);
                if (numerator.teacher.isEmpty())
                    cabinetTV.setText(String.format("%s", numerator.cabinet));
                else if (numerator.cabinet.isEmpty())
                    cabinetTV.setText(String.format("%s", numerator.teacher));
                else
                    cabinetTV.setText(
                            String.format("%s, %s", numerator.cabinet, numerator.teacher));
                cabinetTV.setPadding(50, 0, 0, 25);
                numeratorLessonData.addView(cabinetTV);
            }

            LinearLayout numeratorChangeRow = new LinearLayout(context);
            numeratorChangeRow.setLayoutParams(paramsWrapWrap);
            numeratorChangeRow.setOrientation(HORIZONTAL);
            numeratorChangeRow.setPadding(25, 0, 0, 0);
            secondStroke.addView(numeratorChangeRow);

            ImageButton numeratorChangeLessonBtn = new ImageButton(context);
            numeratorChangeLessonBtn.setLayoutParams(paramsWrapWrap);
            numeratorChangeLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_rename));
            numeratorChangeLessonBtn.setOnClickListener(
                    new ChangeLessonBtnListener(true, false));
            numeratorChangeRow.addView(numeratorChangeLessonBtn);

            ImageButton numeratorDeleteLessonBtn = new ImageButton(context);
            numeratorDeleteLessonBtn.setLayoutParams(paramsWrapWrap);
            numeratorDeleteLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_thrash));
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
            denominatorChangeLessonBtn.setLayoutParams(paramsWrapWrap);
            denominatorChangeLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_rename));
            denominatorChangeLessonBtn.setOnClickListener(
                    new ChangeLessonBtnListener(false, true));
            denominatorChangeRow.addView(denominatorChangeLessonBtn);
        }
        else if (denominator != null && denominator.name != null && !denominator.name.isEmpty()) {
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
            numeratorChangeLessonBtn.setLayoutParams(paramsWrapWrap);
            numeratorChangeLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_rename));
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
            denominatorLessonTV.setTextSize(14.0f);
            denominatorLessonTV.setText(denominator.name);
            denominatorLessonTV.setPadding(50, 0, 0, 0);
            denominatorLessonData.addView(denominatorLessonTV);

            if (!denominator.cabinet.isEmpty() || !denominator.teacher.isEmpty()) {
                TextView cabinetTV = new TextView(context);
                cabinetTV.setLayoutParams(paramsMatchWrap);
                cabinetTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                cabinetTV.setTextSize(12.0f);
                if (denominator.teacher.isEmpty())
                    cabinetTV.setText(String.format("%s", denominator.cabinet));
                else if (denominator.cabinet.isEmpty())
                    cabinetTV.setText(String.format("%s", denominator.teacher));
                else
                    cabinetTV.setText(
                            String.format("%s, %s", denominator.cabinet, denominator.teacher));
                cabinetTV.setPadding(50, 0, 0, 25);
                denominatorLessonData.addView(cabinetTV);
            }

            LinearLayout denominatorChangeRow = new LinearLayout(context);
            denominatorChangeRow.setLayoutParams(paramsWrapWrap);
            denominatorChangeRow.setOrientation(HORIZONTAL);
            denominatorChangeRow.setPadding(25, 0, 0, 0);
            thirdStroke.addView(denominatorChangeRow);

            ImageButton denominatorChangeLessonBtn = new ImageButton(context);
            denominatorChangeLessonBtn.setLayoutParams(paramsWrapWrap);
            denominatorChangeLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_rename));
            denominatorChangeLessonBtn.setOnClickListener(
                    new ChangeLessonBtnListener(false, true));
            denominatorChangeRow.addView(denominatorChangeLessonBtn);

            ImageButton denominatorDeleteLessonBtn = new ImageButton(context);
            denominatorDeleteLessonBtn.setLayoutParams(paramsWrapWrap);
            denominatorDeleteLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_thrash));
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
            changeLessonBtn.setLayoutParams(paramsWrapWrap);
            changeLessonBtn.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_rename));
            changeLessonBtn.setOnClickListener(
                    new ChangeLessonBtnListener(true, true));
            changeRow.addView(changeLessonBtn);
        }
    }

    private class DeleteLessonBtnListener implements View.OnClickListener {
        private boolean isNumerator, isDenominator;

        public DeleteLessonBtnListener(boolean isNumerator, boolean isDenominator) {
            this.isNumerator = isNumerator;
            this.isDenominator = isDenominator;
        }

        @Override
        public void onClick(View v) {
            try {
                if (isNumerator) ScheduleStorage.changeLesson(scheduleActivity.getFlowLvl(),
                        scheduleActivity.getCourse(), scheduleActivity.getGroup(),
                        scheduleActivity.getSubgroup(), dayOfWeek,
                        lessonNum, true, "", "", "",
                        scheduleActivity.getSharedPreferences(SettingsStorage.SCHEDULE_SAVES,
                                Context.MODE_PRIVATE));
                if (isDenominator) ScheduleStorage.changeLesson(scheduleActivity.getFlowLvl(),
                        scheduleActivity.getCourse(), scheduleActivity.getGroup(),
                        scheduleActivity.getSubgroup(), dayOfWeek,
                        lessonNum, false, "", "", "",
                        scheduleActivity.getSharedPreferences(SettingsStorage.SCHEDULE_SAVES,
                                Context.MODE_PRIVATE));
                ChangeLessonView.this.removeAllViews();
                init(dayOfWeek, lessonNum);
            } catch (Exception ignored) {}
        }
    }

    private class ChangeLessonBtnListener implements View.OnClickListener {
        private boolean isNumerator, isDenominator;

        public ChangeLessonBtnListener(boolean isNumerator, boolean isDenominator) {
            this.isNumerator = isNumerator;
            this.isDenominator = isDenominator;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ChangeLessonActivity.class);
            intent.putExtra("flowLvl", scheduleActivity.getFlowLvl());
            intent.putExtra("course", scheduleActivity.getCourse());
            intent.putExtra("group", scheduleActivity.getGroup());
            intent.putExtra("subgroup", scheduleActivity.getSubgroup());
            intent.putExtra("dayOfWeek", dayOfWeek);
            intent.putExtra("lessonNum", lessonNum);
            intent.putExtra("isNumerator", isNumerator);
            intent.putExtra("isDenominator", isDenominator);
            intent.putExtra("lessonName", isNumerator && numerator != null ?
                    numerator.name : isDenominator && denominator != null ?
                    denominator.name : "");
            intent.putExtra("teacher", isNumerator && numerator != null ?
                    numerator.teacher : isDenominator && denominator != null ?
                    denominator.teacher : "");
            intent.putExtra("cabinet", isNumerator && numerator != null ?
                    numerator.cabinet : isDenominator && denominator != null ?
                    denominator.cabinet : "");
            context.startActivity(intent);
        }
    }
}
