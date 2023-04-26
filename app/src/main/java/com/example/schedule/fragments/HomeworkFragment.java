package com.example.schedule.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.schedule.R;
import com.example.schedule.activities.ScheduleActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeworkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeworkFragment extends Fragment {

    public HomeworkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomewWorkFragment.
     */
    public static HomeworkFragment newInstance() {
        HomeworkFragment fragment = new HomeworkFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_work, container, false);

        Button newHomeworkBtn = view.findViewById(R.id.new_homework_btn);
        newHomeworkBtn.setOnClickListener(new NewHomeworkBtnListener());

        return view;
    }

    private class NewHomeworkBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ScheduleActivity scheduleActivity = (ScheduleActivity) getContext();
            scheduleActivity.getSupportFragmentManager().beginTransaction().replace(
                    R.id.fragment_view, NewHomeworkFragment.newInstance()).commit();
        }
    }
}