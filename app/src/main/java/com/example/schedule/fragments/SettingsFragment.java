package com.example.schedule.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.schedule.R;
import com.example.schedule.ScheduleStorage;
import com.example.schedule.SettingsStorage;
import com.example.schedule.activities.ScheduleActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    private ActivityResultLauncher<Intent> fileChooserActivity;
    private ActivityResultLauncher<String> requestReadPermissionLauncher;
    private ActivityResultLauncher<String> requestWritePermissionLauncher;

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

        Button chooseThemeBtn = view.findViewById(R.id.choose_theme_btn);
        Button chooseFlowBtn = view.findViewById(R.id.choose_flow_btn);
        Button importBtn = view.findViewById(R.id.import_btn);
        Button exportBtn = view.findViewById(R.id.export_btn);

        chooseThemeBtn.setOnClickListener(new ChooseThemeBtnListener());
        chooseFlowBtn.setOnClickListener(new ChooseFlowBtnListener());
        importBtn.setOnClickListener(new ImportBtnListener());
        exportBtn.setOnClickListener(new ExportBtnListener());

        return view;
    }

    private void importSchedule() {
        Intent intent = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            intent = new Intent(
                    Intent.ACTION_GET_CONTENT//,
                    //MediaStore.Downloads.EXTERNAL_CONTENT_URI
            );
//        }
        intent.setType("*/*");
        fileChooserActivity.launch(intent);
    }

    private void exportSchedule() {
        if (ScheduleStorage.exportSchedule(getActivity()
                .getSharedPreferences(SettingsStorage.SCHEDULE_SAVES, Context.MODE_PRIVATE))) {
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
                    ScheduleStorage.importScheduleAfter28(uri, getContext().getContentResolver(),
                            getActivity().getSharedPreferences(SettingsStorage.SCHEDULE_SAVES,
                                    Context.MODE_PRIVATE));
                } else {
                    ScheduleStorage.importScheduleBefore29(
                            uri.getPath().replace("/document/raw:/", ""),
                            getContext()
                                    .getSharedPreferences(SettingsStorage.SCHEDULE_SAVES,
                                            Context.MODE_PRIVATE));
                }
            }
        }
    }
}