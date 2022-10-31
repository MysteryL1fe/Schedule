package com.example.schedule;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.divider.MaterialDivider;

import org.w3c.dom.Text;

import java.util.Calendar;

/**
 * TODO: document your custom view class.
 */
public class LessonsView extends LinearLayout {
    public LessonsView(Context context) {
        super(context);
    }

    public LessonsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LessonsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    int[] months = new int[] {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    int[] months_1 = new int[] {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    String[] daysOfWeekNames = new String[] {"Понедельник", "Вторник", "Среда",
            "Четверг", "Пятница", "Суббота", "Воскресенье"};
    String[] monthsNames = new String[] {"Января", "Февраля", "Марта", "Апреля", "Мая", "Июня",
            "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"};

    int day, month, year, dayOfWeek, addDays;
    boolean numerator;
    Schedule schedule;
    LessonView[] lessonViews = new LessonView[8];

    public void init(int addDays, Schedule schedule) {
        this.addDays = addDays;
        this.schedule = schedule;
        LoadLessons loadLessons = new LoadLessons();
        loadLessons.execute();
    }

    public void calcDay() {
        int dayOfYear = 0;
        if (year % 4 == 0) {
            for (int i = 0; i < month; i++) {
                dayOfYear += months_1[i];
            }
        } else {
            for (int i = 0; i < month; i++) {
                dayOfYear += months[i];
            }
        }
        dayOfYear += day;

        if (year == 2022) {
            numerator = ((dayOfYear + 4) / 7) % 2 != 0;
            dayOfWeek = (dayOfYear + 4) % 7;
        } else if (year == 2023) {
            numerator = ((dayOfYear + 5) / 7) % 2 != 0;
            dayOfWeek = (dayOfYear + 5) % 7;
        }
    }

    private class LoadLessons extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, addDays);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
            calcDay();

            for (int i = 0; i < 8; i++) {
                LessonStruct lesson = Utils.getLessonBySchedule(dayOfWeek, i, numerator, schedule);
                LessonView lessonView;
                if (lesson != null) {
                    lessonView = new LessonView(LessonsView.this.getContext(), i, lesson.name,
                            Utils.getTimeByLesson(i), lesson.cabinet, lesson.teacher);
                } else {
                    lessonView = new LessonView(LessonsView.this.getContext(),
                            i, Utils.getTimeByLesson(i));
                }
                lessonView.setLayoutParams(params);
                lessonViews[i] = lessonView;
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
            textView.setText(String.format("%s, %s %s %s", daysOfWeekNames[dayOfWeek],
                    day, monthsNames[month], year));
            LessonsView.this.addView(textView);

            for (int i = 0; i < 8; i++) {
                LessonsView.this.addView(lessonViews[i]);
                MaterialDivider divider = new MaterialDivider(LessonsView.this.getContext());
                LessonsView.this.addView(divider);
            }
        }
    }
}