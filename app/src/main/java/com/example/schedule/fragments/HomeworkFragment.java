package com.example.schedule.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.schedule.Homework;
import com.example.schedule.R;
import com.example.schedule.ScheduleDBHelper;
import com.example.schedule.SettingsStorage;
import com.example.schedule.views.HomeworkView;

import java.util.ArrayList;

public class HomeworkFragment extends Fragment {
    private static final String ARG_FLOW_LVL = "flowLvl";
    private static final String ARG_COURSE = "course";
    private static final String ARG_GROUP = "group";
    private static final String ARG_SUBGROUP = "subgroup";
    private int mFlowLvl = 0, mCourse = 0, mGroup = 0, mSubgroup = 0;

    public HomeworkFragment() {}

    public static HomeworkFragment newInstance(int flowLvl, int course, int group, int subgroup) {
        HomeworkFragment fragment = new HomeworkFragment();
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
        View view = inflater.inflate(R.layout.fragment_homework, container, false);
        LinearLayout homeworkContainer = view.findViewById(R.id.homework_container);
        ArrayList<Homework> homeworks = new ScheduleDBHelper(getContext())
                .getAllHomeworks(
                        mFlowLvl, mCourse, mGroup, mSubgroup, getActivity().getSharedPreferences(
                                SettingsStorage.SCHEDULE_SAVES, Context.MODE_PRIVATE
                        )
                );
        if (homeworks.isEmpty()) {
            TextView textView = new TextView(getContext());
            textView.setText("Домашних заданий нету");
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            homeworkContainer.addView(textView);
            switch (SettingsStorage.textSize) {
                case 0:
                    textView.setTextSize(9.0f);
                    break;
                case 2:
                    textView.setTextSize(27.0f);
                    break;
                default:
                    textView.setTextSize(18.0f);
                    break;
            }
        } else {
            for (Homework homework : homeworks) {
                homeworkContainer.addView(new HomeworkView(getContext(), homework));
            }
        }

        return view;
    }
}