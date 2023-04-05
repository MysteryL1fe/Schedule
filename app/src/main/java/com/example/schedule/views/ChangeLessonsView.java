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

public class ChangeLessonsView extends LinearLayout {
    private final ChangeLessonView[] changeLessonViews = new ChangeLessonView[8];
    private Schedule schedule;
    private int dayOfWeek;
    private boolean isNumerator;

    public ChangeLessonsView(Context context) {
        super(context);
    }

    public ChangeLessonsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChangeLessonsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChangeLessonsView(Context context, Schedule schedule, int dayOfWeek,
                             boolean isNumerator) {
        super(context);

        this.schedule = schedule;
        this.dayOfWeek = dayOfWeek;
        this.isNumerator = isNumerator;

        LoadLessons loadLessons = new LoadLessons();
        loadLessons.execute();
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public boolean isNumerator() {
        return isNumerator;
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

                ChangeLessonView changeLessonView;
                if (lesson != null) {
                    changeLessonView = new ChangeLessonView(ChangeLessonsView.this.getContext(), i,
                            lesson.name, lesson.cabinet, lesson.teacher);
                } else {
                    changeLessonView = new ChangeLessonView(ChangeLessonsView.this.getContext(), i);
                }
                changeLessonView.setLayoutParams(params);
                changeLessonViews[i - 1] = changeLessonView;
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

            ChangeLessonsView.this.setOrientation(VERTICAL);
            ChangeLessonsView.this.setPadding(5, 15, 5, 15);
            ChangeLessonsView.this.setLayoutParams(thisParams);
            ChangeLessonsView.this.removeAllViews();

            TextView textView = new TextView(ChangeLessonsView.this.getContext());
            textView.setText(String.format("%s, %s", Utils.dayOfWeekToStr(dayOfWeek),
                    isNumerator ? "Числитель" : "Знаменатель"));
            ChangeLessonsView.this.addView(textView);

            for (int i = 0; i < 8; i++) {
                ChangeLessonsView.this.addView(changeLessonViews[i]);
                MaterialDivider divider = new MaterialDivider(ChangeLessonsView.this.getContext());
                ChangeLessonsView.this.addView(divider);
            }
        }
    }
}
