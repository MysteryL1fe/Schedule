package com.example.schedule.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.schedule.R;
import com.example.schedule.ScheduleStorage;
import com.example.schedule.activities.ScheduleActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    private ActivityResultLauncher<Intent> fileChooserActivity;

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
                new FileChooserActivityResultCallback());

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

    private class ImportBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            fileChooserActivity.launch(intent);
        }
    }

    private class ExportBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (ScheduleStorage.exportSchedule()) {
                Toast.makeText(getContext(), "Файл успешно добавлен в Загрузки",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Произошла ошибка",
                        Toast.LENGTH_LONG).show();
            }
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
                ScheduleStorage.importSchedule(
                        uri.getPath().replace("/document/raw:/", ""),
                        getContext()
                                .getSharedPreferences("ScheduleSaves", Context.MODE_PRIVATE));
            }
        }
    }
}