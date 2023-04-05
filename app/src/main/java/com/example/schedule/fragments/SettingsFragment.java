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
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    public SettingsFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button chooseThemeBtn = view.findViewById(R.id.choose_theme_btn);
        Button chooseFlowBtn = view.findViewById(R.id.choose_flow_btn);

        chooseThemeBtn.setOnClickListener(new ChooseThemeBtnListener());
        chooseFlowBtn.setOnClickListener(new ChooseFlowBtnListener());

        return view;
    }

    private class ChooseThemeBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    private class ChooseFlowBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ((ScheduleActivity) getContext()).finish();
        }
    }
}