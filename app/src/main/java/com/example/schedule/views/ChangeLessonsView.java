package com.example.schedule.views;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Xml;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.schedule.LessonStruct;
import com.example.schedule.R;
import com.example.schedule.Schedule;
import com.example.schedule.Utils;
import com.example.schedule.exceptions.ScheduleException;
import com.google.android.material.divider.MaterialDivider;

import org.xmlpull.v1.XmlPullParser;

public class ChangeLessonsView extends LinearLayout {
    private final ChangeLessonView[] changeLessonViews = new ChangeLessonView[8];
    private Schedule schedule;
    private int dayOfWeek;

    public ChangeLessonsView(Context context) {
        super(context);
    }

    public ChangeLessonsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChangeLessonsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChangeLessonsView(Context context, Schedule schedule, int dayOfWeek) {
        super(context);

        this.schedule = schedule;
        this.dayOfWeek = dayOfWeek;

        LoadLessons loadLessons = new LoadLessons();
        loadLessons.execute();
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    private class LoadLessons extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            for (int i = 1; i < 9; i++) {
                ChangeLessonView changeLessonView;
                changeLessonView = new ChangeLessonView(getContext(), dayOfWeek, i);
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

            TimerView timerView = new TimerView(getContext(), 1000);
            ChangeLessonsView.this.addView(timerView);

            TextView textView = new TextView(ChangeLessonsView.this.getContext());
            textView.setText(String.format("%s", Utils.dayOfWeekToStr(dayOfWeek)));
            ChangeLessonsView.this.addView(textView);

            for (int i = 0; i < 8; i++) {
                ChangeLessonsView.this.addView(changeLessonViews[i]);
                MaterialDivider divider = new MaterialDivider(ChangeLessonsView.this.getContext());
                ChangeLessonsView.this.addView(divider);
            }
        }
    }
}
