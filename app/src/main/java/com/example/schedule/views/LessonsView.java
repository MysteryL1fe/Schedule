package com.example.schedule.views;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.schedule.LessonStruct;
import com.example.schedule.Schedule;
import com.example.schedule.Utils;
import com.example.schedule.exceptions.ScheduleException;
import com.google.android.material.divider.MaterialDivider;

public class LessonsView extends LinearLayout {
    private final LessonView[] lessonViews = new LessonView[8];
    private Schedule schedule;
    private int dayOfWeek, day, month, year;
    private boolean isNumerator;

    public LessonsView(Context context) {
        super(context);
    }

    public LessonsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LessonsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LessonsView(Context context, Schedule schedule,
                       int day, int month, int year, int dayOfWeek, boolean isNumerator) {
        super(context);
        init(schedule, day, month, year, dayOfWeek, isNumerator);
    }

    public void init(Schedule schedule, int day, int month, int year, int dayOfWeek,
                     boolean isNumerator) {
        this.schedule = schedule;
        this.dayOfWeek = dayOfWeek;
        this.day = day;
        this.month = month;
        this.year = year;
        this.isNumerator = isNumerator;
        LoadLessons loadLessons = new LoadLessons();
        loadLessons.execute();
    }

    private class LoadLessons extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            for (int i = 1; i < 9; i++) {
                LessonStruct lesson = null;
                try {
                    lesson = schedule.getLesson(dayOfWeek, i, isNumerator);
                } catch (ScheduleException ignored) {}
                LessonView lessonView;
                if (lesson != null) {
                    lessonView = new LessonView(LessonsView.this.getContext(), i, lesson.name,
                            lesson.cabinet, lesson.teacher);
                } else {
                    lessonView = new LessonView(LessonsView.this.getContext(), i);
                }
                lessonView.setLayoutParams(params);
                lessonViews[i - 1] = lessonView;
            }

            return null;
        }

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
            LessonsView.this.addView(textView);

            for (int i = 0; i < 8; i++) {
                LessonsView.this.addView(lessonViews[i]);
                MaterialDivider divider = new MaterialDivider(LessonsView.this.getContext());
                LessonsView.this.addView(divider);
            }
        }
    }
}