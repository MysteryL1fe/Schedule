package com.example.schedule.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.schedule.Homework;
import com.example.schedule.R;
import com.example.schedule.SettingsStorage;
import com.example.schedule.Utils;
import com.google.android.material.divider.MaterialDivider;

public class HomeworkView extends LinearLayout {

    public HomeworkView(Context context) {
        super(context);
    }

    public HomeworkView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeworkView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HomeworkView(Context context, Homework homework) {
        super(context);
        init(homework);
    }

    private void init(Homework homework) {
        LinearLayout.LayoutParams paramsMatchWrap = new LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        paramsMatchWrap.topMargin = 20;
        paramsMatchWrap.bottomMargin = 20;

        this.setLayoutParams(paramsMatchWrap);
        this.setGravity(Gravity.START);
        this.setOrientation(VERTICAL);
        this.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.lesson_background, getContext().getTheme()
        ));

        paramsMatchWrap.leftMargin = 10;
        paramsMatchWrap.rightMargin = 10;

        MaterialDivider startDivider = new MaterialDivider(getContext());
        startDivider.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.divider_color, getContext().getTheme()
        ));
        this.addView(startDivider);

        TextView dayTV = new TextView(getContext());
        dayTV.setLayoutParams(paramsMatchWrap);
        dayTV.setGravity(Gravity.START);
        dayTV.setText(String.format(
                "%s, %s %s %s", homework.lessonName, homework.day,
                Utils.monthToStr(homework.month), homework.year
        ));
        this.addView(dayTV);

        TextView homeworkTV = new TextView(getContext());
        homeworkTV.setLayoutParams(paramsMatchWrap);
        homeworkTV.setGravity(Gravity.START);
        homeworkTV.setText(homework.homework);
        this.addView(homeworkTV);

        switch (SettingsStorage.textSize) {
            case 0:
                dayTV.setTextSize(8.0f);
                homeworkTV.setTextSize(8.0f);
                break;
            case 2:
                dayTV.setTextSize(24.0f);
                homeworkTV.setTextSize(24.0f);
                break;
            default:
                dayTV.setTextSize(16.0f);
                homeworkTV.setTextSize(16.0f);
                break;
        }

        MaterialDivider endDivider = new MaterialDivider(getContext());
        endDivider.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.divider_color, getContext().getTheme()
        ));
        this.addView(endDivider);
    }
}