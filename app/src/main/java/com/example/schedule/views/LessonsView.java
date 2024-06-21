package com.example.schedule.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.schedule.R;
import com.example.schedule.SettingsStorage;
import com.example.schedule.entity.ScheduleJoined;
import com.google.android.material.divider.MaterialDivider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LessonsView extends LinearLayout {
    private boolean shouldShow;
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );
    LinearLayout.LayoutParams thisParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    public LessonsView(Context context) {
        super(context);
    }

    public LessonsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LessonsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LessonsView(Context context, LocalDate date) {
        super(context);
        init(date);
    }

    public LessonsView(
            Context context, int flowLvl, int course, int group, int subgroup, LocalDate date
    ) {
        super(context);
        init(date);
        loadLessons(flowLvl, course, group, subgroup, date);
    }

    public void init(LocalDate date) {
        thisParams.bottomMargin = 25;

        this.setOrientation(VERTICAL);
        this.setPadding(5, 15, 5, 15);
        this.setLayoutParams(thisParams);

        TextView textView = new TextView(getContext());
        textView.setText(date.format(
                DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
        ));
        switch (SettingsStorage.textSize) {
            case 0:
                textView.setTextSize(10.0f);
                break;
            case 2:
                textView.setTextSize(30.0f);
                break;
            default:
                textView.setTextSize(20.0f);
        }
        LessonsView.this.addView(textView);

        MaterialDivider firstDivider = new MaterialDivider(getContext());
        firstDivider.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.divider_color, getResources().newTheme()
        ));
        LessonsView.this.addView(firstDivider);
    }

    public void loadLessons(
            int flowLvl, int course, int group, int subgroup, LocalDate date
    ) {
        this.removeAllViews();

        boolean isDisplayModeFull = SettingsStorage.displayModeFull;
        shouldShow = isDisplayModeFull;

        TextView textView = new TextView(getContext());
        textView.setText(date.format(
                DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
        ));
        switch (SettingsStorage.textSize) {
            case 0:
                textView.setTextSize(10.0f);
                break;
            case 2:
                textView.setTextSize(30.0f);
                break;
            default:
                textView.setTextSize(20.0f);
        }
        LessonsView.this.addView(textView);

        MaterialDivider firstDivider = new MaterialDivider(getContext());
        firstDivider.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.divider_color, getResources().newTheme()
        ));
        LessonsView.this.addView(firstDivider);

        for (int i = 1; i < 9; i++) {
            LessonView lessonView = new LessonView(
                    getContext(), flowLvl, course, group, subgroup, date, i
            );
            if (isDisplayModeFull || lessonView.isShouldShow()) {
                lessonView.setLayoutParams(params);
                shouldShow = true;

                LessonsView.this.addView(lessonView);
                MaterialDivider divider = new MaterialDivider(getContext());
                divider.setBackground(ResourcesCompat.getDrawable(
                        getResources(), R.drawable.divider_color, getContext().getTheme()
                ));
                LessonsView.this.addView(divider);
            }
        }
    }

    public void addLesson(ScheduleJoined schedule) {
        LessonView lessonView = new LessonView(getContext(), schedule);
        lessonView.setLayoutParams(params);
        shouldShow = true;

        LessonsView.this.addView(lessonView);
        MaterialDivider divider = new MaterialDivider(getContext());
        divider.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.divider_color, getContext().getTheme()
        ));
        LessonsView.this.addView(divider);
    }

    public boolean addTimer(LocalDateTime time) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof LessonView) {
                LessonView lessonView = (LessonView) child;
                if (lessonView.addTimer(time)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isShouldShow() {
        return shouldShow;
    }
}