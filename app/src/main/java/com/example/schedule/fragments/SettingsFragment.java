package com.example.schedule.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.schedule.R;
import com.example.schedule.ScheduleDBHelper;
import com.example.schedule.SettingsStorage;
import com.example.schedule.activities.ScheduleActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SettingsFragment extends Fragment {
    private TextView fontSizeTV, displayModeTV;
    private Button chooseThemeBtn, chooseFlowBtn, clearDBBtn;
    private ToggleButton displayModeToggleBtn, serverToggleButton;

    public SettingsFragment() {}

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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

        fontSizeTV = view.findViewById(R.id.fontSizeTV);
        displayModeTV = view.findViewById(R.id.displayModeTV);
        chooseThemeBtn = view.findViewById(R.id.chooseThemeBtn);
        chooseFlowBtn = view.findViewById(R.id.chooseFlowBtn);
        SeekBar seekBar = view.findViewById(R.id.textSizeSeekBar);
        displayModeToggleBtn = view.findViewById(R.id.displayModeToggleBtn);
        serverToggleButton = view.findViewById(R.id.serverToggleBtn);
        clearDBBtn = view.findViewById(R.id.clearDBBtn);

        chooseThemeBtn.setOnClickListener(new ChooseThemeBtnListener());
        chooseFlowBtn.setOnClickListener(new ChooseFlowBtnListener());
        seekBar.setOnSeekBarChangeListener(new TextSizeSeekBarListener());
        seekBar.setProgress(SettingsStorage.textSize);
        displayModeToggleBtn.setOnCheckedChangeListener(new DisplayModeToggleBtnListener());
        serverToggleButton.setOnCheckedChangeListener(new ServerToggleBtnListener());
        clearDBBtn.setOnClickListener(new ClearDBBtnListener());

        displayModeToggleBtn.setChecked(SettingsStorage.displayModeFull);
        serverToggleButton.setChecked(SettingsStorage.useServer);
        updateScreen();

        return view;
    }

    private void updateScreen() {
        switch (SettingsStorage.textSize) {
            case 0:
                fontSizeTV.setTextSize(12.0f);
                displayModeTV.setTextSize(12.0f);
                chooseThemeBtn.setTextSize(10.0f);
                chooseFlowBtn.setTextSize(10.0f);
                displayModeToggleBtn.setTextSize(10.0f);
                serverToggleButton.setTextSize(10.0f);
                clearDBBtn.setTextSize(10.0f);
                break;
            case 2:
                fontSizeTV.setTextSize(36.0f);
                displayModeTV.setTextSize(36.0f);
                chooseThemeBtn.setTextSize(30.0f);
                chooseFlowBtn.setTextSize(30.0f);
                displayModeToggleBtn.setTextSize(30.0f);
                serverToggleButton.setTextSize(30.0f);
                clearDBBtn.setTextSize(30.0f);
                break;
            default:
                fontSizeTV.setTextSize(24.0f);
                displayModeTV.setTextSize(24.0f);
                chooseThemeBtn.setTextSize(20.0f);
                chooseFlowBtn.setTextSize(20.0f);
                displayModeToggleBtn.setTextSize(20.0f);
                serverToggleButton.setTextSize(20.0f);
                clearDBBtn.setTextSize(20.0f);
                break;
        }
    }

    private class ChooseThemeBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String[] items = new String[] {
                    getString(R.string.system_theme),
                    getString(R.string.light_theme),
                    getString(R.string.dark_theme)
            };
            new MaterialAlertDialogBuilder(getContext(), R.style.Theme_Schedule_Dialog)
                    .setTitle(getString(R.string.choose_theme))
                    .setItems(items, new DialogInterfaceListener())
                    .show();
        }

        private class DialogInterfaceListener implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences saves = getContext().getSharedPreferences(
                        SettingsStorage.SCHEDULE_SAVES, Context.MODE_PRIVATE
                );
                if (SettingsStorage.getTheme(saves) != which) {
                    SettingsStorage.setTheme(which, saves);
                    switch (which) {
                        case 0:
                            AppCompatDelegate.setDefaultNightMode(
                                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                            );
                            break;
                        case 1:
                            AppCompatDelegate.setDefaultNightMode(
                                    AppCompatDelegate.MODE_NIGHT_NO
                            );
                            break;
                        case 2:
                            AppCompatDelegate.setDefaultNightMode(
                                    AppCompatDelegate.MODE_NIGHT_YES
                            );
                            break;
                    }
                }
            }
        }
    }

    private class ChooseFlowBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ((ScheduleActivity) getContext()).finish();
        }
    }

    private class TextSizeSeekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                SettingsStorage.saveTextSize(progress, getContext().getSharedPreferences(
                        SettingsStorage.SCHEDULE_SAVES, Context.MODE_PRIVATE
                ));
                updateScreen();
                ScheduleActivity scheduleActivity = (ScheduleActivity) getActivity();
                scheduleActivity.updateTextSize();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    private class DisplayModeToggleBtnListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SettingsStorage.saveDisplayMode(isChecked, getActivity().getSharedPreferences(
                    SettingsStorage.SCHEDULE_SAVES, Context.MODE_PRIVATE
            ));
        }
    }

    private class ServerToggleBtnListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SettingsStorage.saveUseServer(isChecked, getActivity().getSharedPreferences(
                    SettingsStorage.SCHEDULE_SAVES, Context.MODE_PRIVATE
            ));
        }
    }

    private class ClearDBBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ScheduleDBHelper dbHelper = new ScheduleDBHelper(getContext());
            dbHelper.clearData();
            ((ScheduleActivity) getContext()).finish();
        }
    }
}