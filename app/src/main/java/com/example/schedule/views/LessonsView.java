package com.example.schedule.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.schedule.R;
import com.example.schedule.SettingsStorage;
import com.example.schedule.Utils;
import com.google.android.material.divider.MaterialDivider;

import java.util.Calendar;

public class LessonsView extends LinearLayout {
    private boolean shouldShow;

    public LessonsView(Context context) {
        super(context);
    }

    public LessonsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LessonsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LessonsView(Context context, int flowLvl, int course, int group, int subgroup,
                       int year, int month, int day) {
        super(context);
        init(flowLvl, course, group, subgroup, year, month, day);
    }

    public void init(int flowLvl, int course, int group, int subgroup,
                     int year, int month, int day) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout.LayoutParams thisParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thisParams.bottomMargin = 25;

        this.setOrientation(VERTICAL);
        this.setPadding(5, 15, 5, 15);
        this.setLayoutParams(thisParams);
        this.removeAllViews();

        int dayOfWeek = Utils.getDayOfWeek(year, month, day);
        boolean isNumerator = Utils.isNumerator(year, month, day);
        boolean isDisplayModeFull = SettingsStorage.displayModeFull;
        shouldShow = isDisplayModeFull;

        TextView textView = new TextView(LessonsView.this.getContext());
        textView.setText(String.format("%s, %s %s %s", Utils.dayOfWeekToStr(dayOfWeek),
                day, Utils.monthToStr(month), year));
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
                    getContext(), flowLvl, course, group, subgroup, year, month, day,
                    dayOfWeek, isNumerator, i
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

    public boolean addTimer(Calendar curTime) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof LessonView) {
                LessonView lessonView = (LessonView) child;
                if (lessonView.addTimer(curTime)) {
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