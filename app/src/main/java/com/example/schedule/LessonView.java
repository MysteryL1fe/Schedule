package com.example.schedule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * TODO: document your custom view class.
 */
public class LessonView extends LinearLayout {

    public LessonView(Context context) {
        super(context);
    }

    public LessonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LessonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LessonView(Context context, int lessonNum, String lessonTime) {
        super(context);
        init(lessonNum, "", lessonTime, "", "");
    }

    public LessonView(Context context, int lessonNum, String lessonName,
                      String lessonTime, String lessonCabinet, String lessonTeacher) {
        super(context);
        init(lessonNum, lessonName, lessonTime, lessonCabinet, lessonTeacher);
    }

    public LessonView(Context context, AttributeSet attrs, int lessonNum, String lessonName,
                      String lessonTime, String lessonCabinet, String lessonTeacher) {
        super(context, attrs);
        init(lessonNum, lessonName, lessonTime, lessonCabinet, lessonTeacher);
    }

    public LessonView(Context context, AttributeSet attrs, int defStyle, int lessonNum,
                      String lessonName, String lessonTime,
                      String lessonCabinet, String lessonTeacher) {
        super(context, attrs, defStyle);
        init(lessonNum, lessonName, lessonTime, lessonCabinet, lessonTeacher);
    }

    private void init(int lessonNum, String lessonName,
                        String lessonTime, String lessonCabinet, String lessonTeacher) {
        LinearLayout.LayoutParams paramsMatchWrap = new LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        LayoutParams paramsWrapWrap = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );

        this.setLayoutParams(paramsMatchWrap);
        this.setGravity(Gravity.START);
        this.setOrientation(VERTICAL);
        this.setBackgroundColor(getResources().getColor(R.color.gray_600));

        LinearLayout firstStroke = new LinearLayout(getContext());
        firstStroke.setLayoutParams(paramsMatchWrap);
        firstStroke.setOrientation(HORIZONTAL);
        firstStroke.setPadding(0, 10, 0, 10);
        this.addView(firstStroke);

        TextView lessonNumTV = new TextView(getContext());
        lessonNumTV.setLayoutParams(paramsWrapWrap);
        lessonNumTV.setText(String.format("%s", lessonNum + 1));
        lessonNumTV.setTextSize(12.0f);
        lessonNumTV.setGravity(Gravity.START);
        lessonNumTV.setBackgroundResource(R.drawable.behind_lesson_num);
        lessonNumTV.setPadding(75, 5, 15, 5);
        firstStroke.addView(lessonNumTV);

        TextView timeTV = new TextView(getContext());
        timeTV.setLayoutParams(paramsMatchWrap);
        timeTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_END);
        timeTV.setText(lessonTime);
        timeTV.setGravity(Gravity.END);
        timeTV.setPadding(0, 0, 25, 0);
        firstStroke.addView(timeTV);

        if (!lessonName.isEmpty()) {
            TextView lessonTV = new TextView(getContext());
            lessonTV.setLayoutParams(paramsMatchWrap);
            lessonTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            lessonTV.setTextSize(14.0f);
            lessonTV.setText(lessonName);
            lessonTV.setPadding(50, 0, 0, 0);
            this.addView(lessonTV);

            TextView cabinetTV = new TextView(getContext());
            cabinetTV.setLayoutParams(paramsMatchWrap);
            cabinetTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            cabinetTV.setTextSize(12.0f);
            cabinetTV.setText(String.format("%s, %s", lessonCabinet, lessonTeacher));
            cabinetTV.setPadding(50, 0, 0, 25);
            this.addView(cabinetTV);
        }
    }

    public void createTimerBeforeLesson(int secondsToLesson) {
        TimerView timerView = new TimerView(getContext());
        timerView.parentView = this;
        timerView.init(secondsToLesson);
    }

    public void createTimerToEndLesson(int secondsToEnd) {
        TimerView timerView = new TimerView(getContext());
        timerView.parentView = this;
        timerView.init(secondsToEnd);
    }
}