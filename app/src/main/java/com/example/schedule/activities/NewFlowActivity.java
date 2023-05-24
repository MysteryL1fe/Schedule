package com.example.schedule.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.schedule.R;
import com.example.schedule.SettingsStorage;

public class NewFlowActivity extends AppCompatActivity {
    private Button cancelBtn, nextBtn;
    private EditText newFlowTextEdit;
    private TextView errorTv;
    private String flow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_flow);

        Intent thisIntent = getIntent();
        flow = thisIntent.getStringExtra("flow");
        newFlowTextEdit = findViewById(R.id.newFlowTextEdit);
        errorTv = findViewById(R.id.errorTV);
        cancelBtn = findViewById(R.id.cancelBtn);
        nextBtn = findViewById(R.id.nextBtn);

        switch (SettingsStorage.TEXT_SIZE) {
            case 0:
                newFlowTextEdit.setTextSize(12.0f);
                errorTv.setTextSize(12.0f);
                cancelBtn.setTextSize(10.0f);
                nextBtn.setTextSize(10.0f);
                break;
            case 1:
                newFlowTextEdit.setTextSize(24.0f);
                errorTv.setTextSize(24.0f);
                cancelBtn.setTextSize(20.0f);
                nextBtn.setTextSize(20.0f);
                break;
            case 2:
                newFlowTextEdit.setTextSize(36.0f);
                errorTv.setTextSize(36.0f);
                cancelBtn.setTextSize(30.0f);
                nextBtn.setTextSize(30.0f);
                break;
        }

        switch (flow) {
            case "course":
                newFlowTextEdit.setHint("Новый курс");
                break;
            case "group":
                newFlowTextEdit.setHint("Новая группа");
                break;
            case "subgroup":
                newFlowTextEdit.setHint("Новая подгруппа");
                break;
        }

        cancelBtn.setOnClickListener(new CancelBtnListener());
        nextBtn.setOnClickListener(new NextBtnListener());
    }

    private class CancelBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private class NextBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                int result = Integer.parseInt(newFlowTextEdit.getText().toString());
                Intent data = new Intent();
                data.putExtra("flow", flow);
                data.putExtra("result", result);
                setResult(RESULT_OK, data);
                finish();
            } catch (NumberFormatException e) {
                switch (flow) {
                    case "course":
                        errorTv.setText("Курс должен быть числом");
                        break;
                    case "group":
                        errorTv.setText("Группа должна быть числом");
                        break;
                    case "subgroup":
                        errorTv.setText("Подгруппа должна быть числом");
                        break;
                }
                errorTv.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    }
}