package com.example.schedule.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.schedule.R;
import com.example.schedule.SettingsStorage;
import com.example.schedule.views.LessonsView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ScheduleFragment extends Fragment {
    private static final String ARG_FLOW_LVL = "flowLvl";
    private static final String ARG_COURSE = "course";
    private static final String ARG_GROUP = "group";
    private static final String ARG_SUBGROUP = "subgroup";
    private int mFlowLvl = 0, mCourse = 0, mGroup = 0, mSubgroup = 0;

    private final ArrayList<LessonsView> lessonsViews = new ArrayList<>();
    private LinearLayout lessonsContainer;
    private boolean lessonsLoaded = false;
    private TextView emptyLessonsViews;
    private LoadLessonsThread loadLessonsThread;

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
        emptyLessonsViews = view.findViewById(R.id.notFoundTV);

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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (loadLessonsThread != null) loadLessonsThread.stopThread();
        loadLessonsThread = new LoadLessonsThread();
        loadLessonsThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        loadLessonsThread.stopThread();
    }

    public void addTimer() {
        if (lessonsLoaded) {
            LocalDateTime time = LocalDateTime.now();
            for (LessonsView lessonsView : lessonsViews) {
                if (lessonsView.addTimer(time)) break;
            }
        }
    }

    private class LoadLessonsThread extends Thread {
        private boolean isActive = true;

        @Override
        public void run() {
            super.run();

            lessonsLoaded = false;

            Activity activity = getActivity();
            if (activity == null) return;
            activity.runOnUiThread(() -> {
                lessonsViews.clear();
                lessonsContainer.removeAllViews();
            });

            LocalDate date = LocalDate.now();

            AtomicBoolean timerAdded = new AtomicBoolean(false);

            for (int i = 0; i < 14; i++) {
                if (!isActive) return;
                LessonsView lessonsView = new LessonsView(
                        lessonsContainer.getContext(), mFlowLvl, mCourse, mGroup, mSubgroup, date
                );
                if (lessonsView.isShouldShow()) {
                    lessonsViews.add(lessonsView);
                    activity = getActivity();
                    if (activity == null) return;
                    if (!timerAdded.get()) activity.runOnUiThread(() -> timerAdded.set(
                            lessonsView.addTimer(LocalDateTime.now()))
                    );
                    activity.runOnUiThread(() -> lessonsContainer.addView(lessonsView));
                }

                date = date.plusDays(1);
            }

            if (!isActive) return;

            if (lessonsViews.isEmpty()) {
                activity = getActivity();
                if (activity == null) return;
                activity.runOnUiThread(() -> emptyLessonsViews.setVisibility(View.VISIBLE));
            }

            if (!isActive) return;

            lessonsLoaded = true;
        }

        public void stopThread() {
            this.isActive = false;
        }
    }
}