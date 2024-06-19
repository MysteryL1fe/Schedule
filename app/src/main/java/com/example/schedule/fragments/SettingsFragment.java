package com.example.schedule.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.schedule.R;
import com.example.schedule.SettingsStorage;
import com.example.schedule.activities.ScheduleActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SettingsFragment extends Fragment {
    private TextView fontSizeTV, countdownBeginningTV, displayModeTV;
    private Button chooseThemeBtn, chooseFlowBtn, countdownBeginningBtn;
    private ToggleButton displayModeToggleBtn;

    public SettingsFragment() {}

    public static SettingsFragment newInstance(int flowLvl, int course, int group, int subgroup) {
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
        countdownBeginningTV = view.findViewById(R.id.countdownBeginningTV);
        displayModeTV = view.findViewById(R.id.displayModeTV);
        chooseThemeBtn = view.findViewById(R.id.chooseThemeBtn);
        chooseFlowBtn = view.findViewById(R.id.chooseFlowBtn);
        countdownBeginningBtn = view.findViewById(R.id.countdownBeginningBtn);
        SeekBar seekBar = view.findViewById(R.id.textSizeSeekBar);
        displayModeToggleBtn = view.findViewById(R.id.displayModeToggleBtn);

        chooseThemeBtn.setOnClickListener(new ChooseThemeBtnListener());
        chooseFlowBtn.setOnClickListener(new ChooseFlowBtnListener());
        countdownBeginningBtn.setOnClickListener(new CountdownBeginningBtnListener());
        seekBar.setOnSeekBarChangeListener(new TextSizeSeekBarListener());
        seekBar.setProgress(SettingsStorage.textSize);
        displayModeToggleBtn.setOnCheckedChangeListener(new DisplayModeToggleBtnListener());

        displayModeToggleBtn.setChecked(SettingsStorage.displayModeFull);
        updateCountdownBeginningBtn();
        updateScreen();

        return view;
    }

    private void updateScreen() {
        switch (SettingsStorage.textSize) {
            case 0:
                fontSizeTV.setTextSize(12.0f);
                countdownBeginningTV.setTextSize(12.0f);
                displayModeTV.setTextSize(12.0f);
                chooseThemeBtn.setTextSize(10.0f);
                chooseFlowBtn.setTextSize(10.0f);
                countdownBeginningBtn.setTextSize(10.0f);
                displayModeToggleBtn.setTextSize(10.0f);
                break;
            case 2:
                fontSizeTV.setTextSize(36.0f);
                countdownBeginningTV.setTextSize(36.0f);
                displayModeTV.setTextSize(36.0f);
                chooseThemeBtn.setTextSize(30.0f);
                chooseFlowBtn.setTextSize(30.0f);
                countdownBeginningBtn.setTextSize(30.0f);
                displayModeToggleBtn.setTextSize(30.0f);
                break;
            default:
                fontSizeTV.setTextSize(24.0f);
                countdownBeginningTV.setTextSize(24.0f);
                displayModeTV.setTextSize(24.0f);
                chooseThemeBtn.setTextSize(20.0f);
                chooseFlowBtn.setTextSize(20.0f);
                countdownBeginningBtn.setTextSize(20.0f);
                displayModeToggleBtn.setTextSize(20.0f);
                break;
        }
    }

    private void updateCountdownBeginningBtn() {
        Calendar calendar = SettingsStorage.getCountdownBeginning();
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
        countdownBeginningBtn.setText(format.format(calendar.getTime()));
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

    private class CountdownBeginningBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(
                    getContext(), new DatePickerDialogListener(), calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        }
    }

    private class DatePickerDialogListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
            SettingsStorage.setCountdownBeginning(
                    calendar, getActivity().getSharedPreferences(
                            SettingsStorage.SCHEDULE_SAVES, Context.MODE_PRIVATE
                    )
            );
            updateCountdownBeginningBtn();
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
}