package com.example.schedule.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.schedule.R;
import com.example.schedule.SettingsStorage;
import com.example.schedule.Utils;
import com.google.android.material.divider.MaterialDivider;

import java.util.Calendar;

public class LessonsView extends LinearLayout {
    private final LessonView[] lessonViews = new LessonView[8];
    private int flowLvl, course, group, subgroup, dayOfWeek, day, month, year;
    private boolean isNumerator, hasTimerView = false;
    private TimerView timerView;

    public LessonsView(Context context) {
        super(context);
    }

    public LessonsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LessonsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LessonsView(Context context, int flowLvl, int course, int group, int subgroup, int day,
                       int month, int year, int dayOfWeek, boolean isNumerator) {
        super(context);
        init(flowLvl, course, group, subgroup, day, month, year, dayOfWeek, isNumerator);
    }

    public void init(int flowLvl, int course, int group, int subgroup, int day, int month, int year,
                     int dayOfWeek, boolean isNumerator) {
        this.flowLvl = flowLvl;
        this.course = course;
        this.group = group;
        this.subgroup = subgroup;
        this.dayOfWeek = dayOfWeek;
        this.day = day;
        this.month = month;
        this.year = year;
        this.isNumerator = isNumerator;
        LoadLessons loadLessons = new LoadLessons();
        loadLessons.execute();
    }

    public void addTimer() {
        int lessonNum = Utils.getLesson();
        if (lessonNum >= 0) {
            lessonViews[lessonNum].addTimer(this);
        } else if (lessonNum > -9) {
            timerView = new TimerView(
                    getContext(), Utils.getTimeToNextLesson(), this, this
            );
            this.addView(timerView, Math.abs(lessonNum + 1) * 2 + 1);
        } else {
            timerView = new TimerView(
                    getContext(), Utils.getTimeToNextLesson(), this, this
            );
            this.addView(timerView, 1);
        }
        hasTimerView = true;
    }

    public void updateTimer() {
        if (timerView != null) removeView(timerView);
        hasTimerView = false;
        int lessonNum = Utils.getLesson();
        if (lessonNum >= 0) {
            lessonViews[lessonNum].addTimer(this);
        } else if (lessonNum > -9) {
            timerView = new TimerView(
                    getContext(), Utils.getTimeToNextLesson(), this, this
            );
            this.addView(timerView, Math.abs(lessonNum) * 2);
            hasTimerView = true;
        } else {
            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.DAY_OF_MONTH) + 1 == day) {
                addTimer();
                hasTimerView = true;
            }
            else {
                ViewGroup parent = (ViewGroup) getParent();
                ((LessonsView) parent.getChildAt(1)).addTimer();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (timerView != null)
            removeView(timerView);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        ViewGroup parent;
        if (hasWindowFocus && !hasTimerView && this.getChildCount() > 0
                && ((parent = (ViewGroup) this.getParent())
                .indexOfChild(this) == 0 && Utils.getLesson() > -9
                || parent.indexOfChild(this) == 1 && Utils.getLesson() == -9))
            addTimer();
        else if (!hasWindowFocus) {
            hasTimerView = false;
            if (timerView != null) {
                removeView(timerView);
            }
        }
    }

    private class LoadLessons extends AsyncTask<Void, Void, Void> {
        @SuppressLint("Range")
        @Override
        protected Void doInBackground(Void... voids) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            for (int i = 1; i < 9; i++) {
                LessonView lessonView = new LessonView(
                        getContext(), flowLvl, course, group, subgroup, day, month, year,
                        dayOfWeek, isNumerator, i, false
                );
                lessonView.setLayoutParams(params);
                lessonViews[i - 1] = lessonView;
            }
            return null;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            LinearLayout.LayoutParams thisParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            thisParams.bottomMargin = 25;

            LessonsView.this.setOrientation(VERTICAL);
            LessonsView.this.setPadding(5, 15, 5, 15);
            LessonsView.this.setLayoutParams(thisParams);
            LessonsView.this.removeAllViews();

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
            firstDivider.setBackground(getResources().getDrawable(
                    R.drawable.divider_color, getContext().getTheme()
            ));
            LessonsView.this.addView(firstDivider);

            for (int i = 0; i < 8; i++) {
                LessonsView.this.addView(lessonViews[i]);
                MaterialDivider divider = new MaterialDivider(getContext());
                divider.setBackground(getResources().getDrawable(
                        R.drawable.divider_color, getContext().getTheme()
                ));
                LessonsView.this.addView(divider);
            }

            ViewGroup parent;
            if (((parent = (ViewGroup) LessonsView.this.getParent())
                    .indexOfChild(LessonsView.this) == 0 && Utils.getLesson() > -9
                    || parent.indexOfChild(LessonsView.this) == 1 && Utils.getLesson() == -9))
                addTimer();
        }
    }
}