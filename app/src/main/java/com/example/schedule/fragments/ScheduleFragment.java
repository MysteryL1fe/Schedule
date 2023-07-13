package com.example.schedule.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.schedule.R;
import com.example.schedule.SettingsStorage;
import com.example.schedule.Utils;
import com.example.schedule.views.LessonsView;

import java.util.Calendar;

public class ScheduleFragment extends Fragment {

    private static final String ARG_FLOW_LVL = "flowLvl";
    private static final String ARG_COURSE = "course";
    private static final String ARG_GROUP = "group";
    private static final String ARG_SUBGROUP = "subgroup";
    private int mFlowLvl = 0, mCourse = 0, mGroup = 0, mSubgroup = 0;

    private LinearLayout lessonsContainer;

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

        lessonsContainer = view.findViewById(R.id.lessons_container);

        LoadLessons loadLessons = new LoadLessons();
        loadLessons.execute();

        return view;
    }

    private class LoadLessons extends AsyncTask<Void, Void, Void> {
        private final LessonsView[] lessonsViews = new LessonsView[21];

        @Override
        protected Void doInBackground(Void... voids) {
            Calendar calendar = Calendar.getInstance();
            int day, month, year, dayOfWeek;
            boolean isNumerator;

            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            dayOfWeek = Utils.getDayOfWeek(year, month, day);
            isNumerator = Utils.isNumerator(year, month, day);

            for (int i = 0; i < 21; i++) {
                LessonsView lessonsView = new LessonsView(lessonsContainer.getContext(), mFlowLvl,
                        mCourse, mGroup, mSubgroup, day, month, year, dayOfWeek, isNumerator);
                lessonsViews[i] = lessonsView;

                calendar.add(Calendar.DAY_OF_MONTH, 1);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH) + 1;
                year = calendar.get(Calendar.YEAR);
                if (++dayOfWeek == 8) {
                    dayOfWeek = 1;
                    isNumerator = !isNumerator;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            for (int i = 0; i < 21; i++) {
                lessonsContainer.addView(lessonsViews[i]);
            }
        }
    }
}