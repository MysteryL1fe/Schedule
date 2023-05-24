package com.example.schedule.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.schedule.R;
import com.example.schedule.SettingsStorage;
import com.example.schedule.Utils;

public class LessonView extends LinearLayout {
    private LinearLayout firstStroke;
    private TimerView timerView;

    public LessonView(Context context) {
        super(context);
        init(0, "", "", "");
    }

    public LessonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LessonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LessonView(Context context, int lessonNum) {
        super(context);
        init(lessonNum, "", "", "");
    }

    public LessonView(Context context, int lessonNum, String lessonName,
                      String lessonCabinet, String lessonTeacher) {
        super(context);
        init(lessonNum, lessonName, lessonCabinet, lessonTeacher);
    }

    private void init(int lessonNum, String lessonName,
                      String lessonCabinet, String lessonTeacher) {
        LinearLayout.LayoutParams paramsMatchWrap = new LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        LayoutParams paramsWrapWrap = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );

        this.setLayoutParams(paramsMatchWrap);
        this.setGravity(Gravity.START);
        this.setOrientation(VERTICAL);
        this.setBackground(getResources().getDrawable(R.drawable.lesson_background,
                getContext().getTheme()));

        firstStroke = new LinearLayout(getContext());
        firstStroke.setLayoutParams(paramsMatchWrap);
        firstStroke.setOrientation(HORIZONTAL);
        firstStroke.setPadding(0, 10, 0, 10);
        this.addView(firstStroke);

        TextView lessonNumTV = new TextView(getContext());
        lessonNumTV.setLayoutParams(paramsWrapWrap);
        lessonNumTV.setText(String.format("%s", lessonNum));
        lessonNumTV.setTextSize(12.0f);
        lessonNumTV.setGravity(Gravity.START);
        lessonNumTV.setBackgroundResource(R.drawable.behind_lesson_num);
        lessonNumTV.setPadding(75, 5, 15, 5);
        firstStroke.addView(lessonNumTV);

        TextView timeTV = new TextView(getContext());
        timeTV.setLayoutParams(paramsMatchWrap);
        timeTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_END);
        timeTV.setText(Utils.getTimeByLesson(lessonNum));
        timeTV.setGravity(Gravity.END);
        timeTV.setPadding(0, 0, 25, 0);
        firstStroke.addView(timeTV);

        switch (SettingsStorage.TEXT_SIZE) {
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

        if (!lessonName.isEmpty()) {
            TextView lessonTV = new TextView(getContext());
            lessonTV.setLayoutParams(paramsMatchWrap);
            lessonTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            switch (SettingsStorage.TEXT_SIZE) {
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
            lessonTV.setText(lessonName);
            lessonTV.setPadding(50, 0, 0, 0);
            this.addView(lessonTV);

            if (!lessonCabinet.isEmpty() || !lessonTeacher.isEmpty()) {
                TextView cabinetTV = new TextView(getContext());
                cabinetTV.setLayoutParams(paramsMatchWrap);
                cabinetTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                switch (SettingsStorage.TEXT_SIZE) {
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
                if (lessonTeacher.isEmpty())
                    cabinetTV.setText(String.format("%s", lessonCabinet));
                else if (lessonCabinet.isEmpty())
                    cabinetTV.setText(String.format("%s", lessonTeacher));
                else
                    cabinetTV.setText(String.format("%s, %s", lessonCabinet, lessonTeacher));
                cabinetTV.setPadding(50, 0, 0, 25);
                this.addView(cabinetTV);
            }
        }
    }

    public void addTimer(LessonsView lessonsView) {
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT,
                1.0f
        );

        timerView = new TimerView(
                getContext(), Utils.getTimeToNextLesson(), lessonsView, firstStroke
        );
        timerView.setLayoutParams(params);
        timerView.setGravity(Gravity.END);
        firstStroke.addView(timerView, 1);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus && timerView != null)
            firstStroke.removeView(timerView);
    }
}