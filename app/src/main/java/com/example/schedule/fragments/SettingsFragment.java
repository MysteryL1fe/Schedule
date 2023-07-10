package com.example.schedule.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schedule.R;
import com.example.schedule.ScheduleDBHelper;
import com.example.schedule.SettingsStorage;
import com.example.schedule.activities.ScheduleActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SettingsFragment extends Fragment {
    private static final String ARG_FLOW_LVL = "flowLvl";
    private static final String ARG_COURSE = "course";
    private static final String ARG_GROUP = "group";
    private static final String ARG_SUBGROUP = "subgroup";
    private int mFlowLvl = 0, mCourse = 0, mGroup = 0, mSubgroup = 0;

    private ActivityResultLauncher<Intent> fileChooserActivity;
    private ActivityResultLauncher<String> requestReadPermissionLauncher;
    private ActivityResultLauncher<String> requestWritePermissionLauncher;
    private TextView fontSizeTV, countdownBeginningTV;
    private Button chooseThemeBtn, chooseFlowBtn, importBtn, exportBtn, countdownBeginningBtn;

    public SettingsFragment() {}

    public static SettingsFragment newInstance(int flowLvl, int course, int group, int subgroup) {
        SettingsFragment fragment = new SettingsFragment();
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
        fileChooserActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new FileChooserActivityResultCallback()
        );
        requestReadPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                result -> {
                    if (result) importSchedule();
                }
        );
        requestWritePermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                result -> {
                    if (result) exportSchedule();
                }
        );

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        fontSizeTV = view.findViewById(R.id.fontSizeTV);
        countdownBeginningTV = view.findViewById(R.id.countdownBeginningTV);
        chooseThemeBtn = view.findViewById(R.id.chooseThemeBtn);
        chooseFlowBtn = view.findViewById(R.id.chooseFlowBtn);
        importBtn = view.findViewById(R.id.importBtn);
        exportBtn = view.findViewById(R.id.exportBtn);
        countdownBeginningBtn = view.findViewById(R.id.countdownBeginningBtn);
        SeekBar seekBar = view.findViewById(R.id.textSizeSeekBar);

        chooseThemeBtn.setOnClickListener(new ChooseThemeBtnListener());
        chooseFlowBtn.setOnClickListener(new ChooseFlowBtnListener());
        importBtn.setOnClickListener(new ImportBtnListener());
        exportBtn.setOnClickListener(new ExportBtnListener());
        countdownBeginningBtn.setOnClickListener(new CountdownBeginningBtnListener());
        seekBar.setOnSeekBarChangeListener(new TextSizeSeekBarListener());
        seekBar.setProgress(SettingsStorage.textSize);

        updateCountdownBeginningBtn();
        updateScreen();

        return view;
    }

    private void updateScreen() {
        switch (SettingsStorage.textSize) {
            case 0:
                fontSizeTV.setTextSize(12.0f);
                countdownBeginningTV.setTextSize(12.0f);
                chooseThemeBtn.setTextSize(10.0f);
                chooseFlowBtn.setTextSize(10.0f);
                importBtn.setTextSize(10.0f);
                exportBtn.setTextSize(10.0f);
                countdownBeginningBtn.setTextSize(10.0f);
                break;
            case 1:
                fontSizeTV.setTextSize(24.0f);
                countdownBeginningTV.setTextSize(24.0f);
                chooseThemeBtn.setTextSize(20.0f);
                chooseFlowBtn.setTextSize(20.0f);
                importBtn.setTextSize(20.0f);
                exportBtn.setTextSize(20.0f);
                countdownBeginningBtn.setTextSize(20.0f);
                break;
            case 2:
                fontSizeTV.setTextSize(36.0f);
                countdownBeginningTV.setTextSize(36.0f);
                chooseThemeBtn.setTextSize(30.0f);
                chooseFlowBtn.setTextSize(30.0f);
                importBtn.setTextSize(30.0f);
                exportBtn.setTextSize(30.0f);
                countdownBeginningBtn.setTextSize(30.0f);
                break;
        }
    }

    private void updateCountdownBeginningBtn() {
        Calendar calendar = SettingsStorage.getCountdownBeginning(
                getActivity().getSharedPreferences(
                        SettingsStorage.SCHEDULE_SAVES, Context.MODE_PRIVATE
                )
        );
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
        countdownBeginningBtn.setText(format.format(calendar.getTime()));
    }

    private void importSchedule() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        fileChooserActivity.launch(intent);
    }

    private void exportSchedule() {
        if (new ScheduleDBHelper(getContext())
                .exportSchedule(mFlowLvl, mCourse, mGroup, mSubgroup)) {
            Toast.makeText(getContext(), "Файл успешно добавлен в Загрузки",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Произошла ошибка",
                    Toast.LENGTH_LONG).show();
        }
    }

    private class ChooseThemeBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String[] items = new String[] {"Системная", "Светлая", "Тёмная"};
            new MaterialAlertDialogBuilder(getContext(), R.style.Theme_Schedule_Dialog)
                    .setTitle("Выберите тему")
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

    private class ImportBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R
                    && ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                return;
            }
            importSchedule();
        }
    }

    private class ExportBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R
                    && ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestWritePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                return;
            }
            exportSchedule();
        }
    }

    private class FileChooserActivityResultCallback
            implements ActivityResultCallback<ActivityResult> {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data == null) return;
                Uri uri = data.getData();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    new ScheduleDBHelper(getContext()).importScheduleAfter28(
                            uri, getContext().getContentResolver(),
                            mFlowLvl, mCourse, mGroup, mSubgroup
                    );
                } else {
                    new ScheduleDBHelper(getContext()).importScheduleBefore29(
                            uri.getPath().replace("/document/raw:/", ""),
                            mFlowLvl, mCourse, mGroup, mSubgroup
                    );
                }
            }
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
}