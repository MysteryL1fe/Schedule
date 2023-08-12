package com.example.schedule.fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.schedule.R;
import com.example.schedule.SettingsStorage;
import com.example.schedule.Utils;
import com.example.schedule.activities.MainActivity;
import com.example.schedule.views.ChangeLessonsView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ChangeScheduleFragment extends Fragment {
    private static final String ARG_FLOW_LVL = "flowLvl";
    private static final String ARG_COURSE = "course";
    private static final String ARG_GROUP = "group";
    private static final String ARG_SUBGROUP = "subgroup";
    private int mFlowLvl = 0, mCourse = 0, mGroup = 0, mSubgroup = 0;

    private LinearLayout lessonsContainer;
    private Button dayOfWeekBtn;
    private int dayOfWeek = 1;

    public ChangeScheduleFragment() {}

    public static ChangeScheduleFragment newInstance(int flowLvl, int course, int group,
                                                     int subgroup) {
        ChangeScheduleFragment fragment = new ChangeScheduleFragment();
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
        View view = inflater.inflate(R.layout.fragment_change_schedule, container, false);

        lessonsContainer = view.findViewById(R.id.lessonsContainer);
        dayOfWeekBtn = view.findViewById(R.id.dayOfWeekBtn);
        dayOfWeekBtn.setOnClickListener(new DayOfWeekBtnListener());

        switch (SettingsStorage.textSize) {
            case 0:
                dayOfWeekBtn.setTextSize(10.0f);
                break;
            case 1:
                dayOfWeekBtn.setTextSize(20.0f);
                break;
            case 2:
                dayOfWeekBtn.setTextSize(30.0f);
                break;
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateFragment();
    }

    public void updateFragment() {
        dayOfWeekBtn.setText(Utils.dayOfWeekToStr(dayOfWeek));

        LoadLessons loadLessons = new LoadLessons();
        loadLessons.execute();
    }

    private class LoadLessons extends AsyncTask<Void, Void, Void> {
        private ChangeLessonsView changeLessonsView;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            lessonsContainer.removeAllViews();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            changeLessonsView = new ChangeLessonsView(
                    lessonsContainer.getContext(), mFlowLvl, mCourse, mGroup, mSubgroup,
                    dayOfWeek
            );
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            lessonsContainer.addView(changeLessonsView);
        }
    }

    private class DayOfWeekBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new MaterialAlertDialogBuilder(
                    getContext(), R.style.Theme_Schedule_Dialog
            )
                    .setTitle("Выберите день недели")
                    .setItems(Utils.daysOfWeekNames, new DialogInterfaceListener())
                    .show();
        }

        private class DialogInterfaceListener implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dayOfWeek = which + 1;
                updateFragment();
            }
        }
    }
}