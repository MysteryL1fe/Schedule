package com.example.schedule.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.example.schedule.R;
import com.google.android.material.divider.MaterialDivider;

public class ChangeLessonsView extends LinearLayout {
    private final ChangeLessonView[] changeLessonViews = new ChangeLessonView[8];
    private int flowLvl, course, group, subgroup, dayOfWeek;

    public ChangeLessonsView(Context context) {
        super(context);
    }

    public ChangeLessonsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChangeLessonsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChangeLessonsView(Context context, int flowLvl, int course, int group, int subgroup,
                             int dayOfWeek) {
        super(context);

        this.flowLvl = flowLvl;
        this.course = course;
        this.group = group;
        this.subgroup = subgroup;
        this.dayOfWeek = dayOfWeek;

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
                ChangeLessonView changeLessonView;
                changeLessonView = new ChangeLessonView(
                        getContext(), flowLvl, course, group, subgroup, dayOfWeek, i
                );
                changeLessonView.setLayoutParams(params);
                changeLessonViews[i - 1] = changeLessonView;
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

            ChangeLessonsView.this.setOrientation(VERTICAL);
            ChangeLessonsView.this.setPadding(5, 15, 5, 15);
            ChangeLessonsView.this.setLayoutParams(thisParams);
            ChangeLessonsView.this.removeAllViews();

            MaterialDivider firstDivider = new MaterialDivider(getContext());
            firstDivider.setBackground(getResources().getDrawable(
                    R.drawable.divider_color, getContext().getTheme()
            ));
            ChangeLessonsView.this.addView(firstDivider);

            for (int i = 0; i < 8; i++) {
                ChangeLessonsView.this.addView(changeLessonViews[i]);
                MaterialDivider divider = new MaterialDivider(ChangeLessonsView.this.getContext());
                divider.setBackground(getResources().getDrawable(
                        R.drawable.divider_color, getContext().getTheme()
                ));
                ChangeLessonsView.this.addView(divider);
            }
        }
    }
}
