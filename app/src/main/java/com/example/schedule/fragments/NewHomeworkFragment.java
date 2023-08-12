package com.example.schedule.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.schedule.R;
import com.example.schedule.SettingsStorage;
import com.example.schedule.Utils;
import com.example.schedule.views.LessonView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class NewHomeworkFragment extends Fragment {
    private static final String ARG_FLOW_LVL = "flowLvl";
    private static final String ARG_COURSE = "course";
    private static final String ARG_GROUP = "group";
    private static final String ARG_SUBGROUP = "subgroup";
    private int mFlowLvl = 0, mCourse = 0, mGroup = 0, mSubgroup = 0;

    private LinearLayout homeworkLessonContainer;
    private Button chooseDayBtn;
    private Calendar calendar = Calendar.getInstance();

    public NewHomeworkFragment() {}

    public static NewHomeworkFragment newInstance(int flowLvl, int course, int group,
                                                  int subgroup) {
        NewHomeworkFragment fragment = new NewHomeworkFragment();
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
        View view = inflater.inflate(R.layout.fragment_new_homework, container, false);

        calendar = Calendar.getInstance();

        homeworkLessonContainer = view.findViewById(R.id.homeworkLessonContainer);
        TextView chooseDayTV = view.findViewById(R.id.chooseDayTV);
        chooseDayBtn = view.findViewById(R.id.chooseDayBtn);

        switch (SettingsStorage.textSize) {
            case 0:
                chooseDayTV.setTextSize(12.0f);
                chooseDayBtn.setTextSize(10.0f);
                break;
            case 1:
                chooseDayTV.setTextSize(24.0f);
                chooseDayBtn.setTextSize(20.0f);
                break;
            case 2:
                chooseDayTV.setTextSize(36.0f);
                chooseDayBtn.setTextSize(30.0f);
                break;
        }

        chooseDayBtn.setOnClickListener(new ChooseDayBtnListener());

        updateFragment();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateFragment();
    }

    public void updateFragment() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 20);
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
        chooseDayBtn.setText(format.format(calendar.getTime()));

        homeworkLessonContainer.removeAllViews();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = Utils.getDayOfWeek(year, month, day);
        boolean isNumerator = Utils.isNumerator(year, month, day);
        for (int i = 1; i < 9; i++) {
            LessonView lessonView = new LessonView(
                    getContext(), mFlowLvl, mCourse, mGroup, mSubgroup, year, month, day,
                    dayOfWeek, isNumerator, i, this
            );
            lessonView.setLayoutParams(params);
            homeworkLessonContainer.addView(lessonView);
        }
    }

    private class ChooseDayBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new DatePickerDialog(
                    getContext(), new DatePickerDialogListener(), calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        }
    }

    private class DatePickerDialogListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar = new GregorianCalendar(year, month, dayOfMonth);
            updateFragment();
        }
    }
}