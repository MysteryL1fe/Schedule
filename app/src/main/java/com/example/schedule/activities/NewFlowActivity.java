package com.example.schedule.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.schedule.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class NewFlowActivity extends AppCompatActivity {
    private Button cancelBtn, nextBtn;
    private EditText newFlowTextNum;
    private TextView errorTv, newFlowTV;
    private int flowLvl = 0;
    private String[] flowLvlStr = new String[] {"Бакалавриат/Специалитет", "Магистратура",
            "Аспирантура"};
    private String flow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_flow);

        Intent thisIntent = getIntent();
        flow = thisIntent.getStringExtra("flow");

        newFlowTV = findViewById(R.id.newFlowTV);
        newFlowTextNum = findViewById(R.id.newFlowTextNum);
        errorTv = findViewById(R.id.errorTV);
        cancelBtn = findViewById(R.id.cancelBtn);
        nextBtn = findViewById(R.id.nextBtn);

        switch (flow) {
            case "course":
                newFlowTV.setText("Введите новый курс");
                break;
            case "group":
                newFlowTV.setText("Введите новую группу");
                break;
            case "subgroup":
                newFlowTV.setText("Введите новую подгруппу");
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
                int result = Integer.parseInt(newFlowTextNum.getText().toString());
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
            } catch (Exception e) {
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    }
}