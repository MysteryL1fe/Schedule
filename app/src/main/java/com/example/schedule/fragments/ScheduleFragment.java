package com.example.schedule.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.schedule.R;
import com.example.schedule.SettingsStorage;
import com.example.schedule.views.LessonsView;

import java.util.ArrayList;
import java.util.Calendar;

public class ScheduleFragment extends Fragment {
    private static final String ARG_FLOW_LVL = "flowLvl";
    private static final String ARG_COURSE = "course";
    private static final String ARG_GROUP = "group";
    private static final String ARG_SUBGROUP = "subgroup";
    private int mFlowLvl = 0, mCourse = 0, mGroup = 0, mSubgroup = 0;

    private final ArrayList<LessonsView> lessonsViews = new ArrayList<>();
    private LinearLayout lessonsContainer;
    private boolean lessonsLoaded = false;

    public ScheduleFragment() {}

    public static ScheduleFragment newInstance(int flowLvl, int course, int group, int subgroup) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FLOW_LVL, flowLvl);
        args.putInt(ARG_COURSE, course);
        args.putInt(ARG_GROUP, group);
        args.putInt(ARG_SUBGROUP, subgroup);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFlowLvl = getArguments().getInt(ARG_FLOW_LVL);
            mCourse = getArguments().getInt(ARG_COURSE);
            mGroup = getArguments().getInt(ARG_GROUP);
            mSubgroup = getArguments().getInt(ARG_SUBGROUP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        lessonsContainer = view.findViewById(R.id.lessonsContainer);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        LoadLessons loadLessons = new LoadLessons();
        loadLessons.execute();
    }

    public void addTimer() {
        if (lessonsLoaded) {
            Calendar curTime = Calendar.getInstance();
            for (LessonsView lessonsView : lessonsViews) {
                if (lessonsView.addTimer(curTime)) {
                    break;
                }
            }
        }
    }

    private class LoadLessons extends AsyncTask<Void, Void, Void> {
        private TextView emptyLessonsViews;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lessonsLoaded = false;
            lessonsViews.clear();
            lessonsContainer.removeAllViews();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Calendar calendar = Calendar.getInstance();
            int year, month, day;

            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);

            for (int i = 0; i < 31; i++) {
                LessonsView lessonsView = new LessonsView(
                        lessonsContainer.getContext(), mFlowLvl, mCourse, mGroup, mSubgroup,
                        year, month, day
                );
                if (lessonsView.isShouldShow()) {
                    lessonsViews.add(lessonsView);
                }

                calendar.add(Calendar.DAY_OF_MONTH, 1);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                day = calendar.get(Calendar.DAY_OF_MONTH);
            }

            emptyLessonsViews = new TextView(lessonsContainer.getContext());
            emptyLessonsViews.setText("Занятия не найдены");
            emptyLessonsViews.setGravity(Gravity.CENTER);

            switch (SettingsStorage.textSize) {
                case 0:
                    emptyLessonsViews.setTextSize(12.0f);
                    break;
                case 2:
                    emptyLessonsViews.setTextSize(36.0f);
                    break;
                default:
                    emptyLessonsViews.setTextSize(24.0f);
                    break;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            if (lessonsViews.isEmpty()) {
                lessonsContainer.addView(emptyLessonsViews);
            }

            for (LessonsView lessonsView : lessonsViews) {
                lessonsContainer.addView(lessonsView);
            }

            lessonsLoaded = true;
            addTimer();
        }
    }
}