package com.example.schedule.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.schedule.R;
import com.example.schedule.SettingsStorage;
import com.example.schedule.entity.Homework;
import com.google.android.material.divider.MaterialDivider;

import java.time.format.DateTimeFormatter;

public class HomeworkView extends LinearLayout {
    public HomeworkView(Context context) {
        super(context);
    }

    public HomeworkView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeworkView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HomeworkView(Context context, int flowLvl, int course, int group, int subgroup,
                        Homework homework) {
        super(context);
        init(flowLvl, course, group, subgroup, homework);
    }

    private void init(int flowLvl, int course, int group, int subgroup, Homework homework) {
        LayoutParams paramsMatchWrap = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT,
                1
        );
        paramsMatchWrap.topMargin = 20;
        paramsMatchWrap.bottomMargin = 20;

        this.setLayoutParams(paramsMatchWrap);
        this.setGravity(Gravity.START);
        this.setOrientation(VERTICAL);
        this.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.lesson_background, getContext().getTheme()
        ));

        paramsMatchWrap.leftMargin = 10;
        paramsMatchWrap.rightMargin = 10;

        MaterialDivider startDivider = new MaterialDivider(getContext());
        startDivider.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.divider_color, getContext().getTheme()
        ));
        this.addView(startDivider);

        TextView dayTV = new TextView(getContext());
        dayTV.setLayoutParams(paramsMatchWrap);
        dayTV.setGravity(Gravity.START);
        dayTV.setText(String.format(
                "%s, %s", homework.getLessonName(), homework.getLessonDate().format(
                        DateTimeFormatter.ofPattern("dd MMM yyyy")
                )
        ));
        this.addView(dayTV);

        TextView homeworkTV = new TextView(getContext());
        homeworkTV.setLayoutParams(paramsMatchWrap);
        homeworkTV.setGravity(Gravity.START);
        homeworkTV.setText(homework.getHomework());
        this.addView(homeworkTV);

        MaterialDivider divider = new MaterialDivider(getContext());
        divider.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.divider_color, getContext().getTheme()
        ));
        this.addView(divider);

        /*LinearLayout btnLayout = new LinearLayout(getContext());
        btnLayout.setOrientation(HORIZONTAL);
        btnLayout.setLayoutParams(paramsMatchWrap);
        this.addView(btnLayout);

        Button deleteBtn = new Button(getContext());
        deleteBtn.setText(R.string.delete);
        deleteBtn.setLayoutParams(paramsMatchWrap);
        deleteBtn.setGravity(Gravity.CENTER);
        deleteBtn.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        deleteBtn.setOnClickListener(new DeleteBtnListener());
        btnLayout.addView(deleteBtn);

        Button changeBtn = new Button(getContext());
        changeBtn.setText(R.string.change);
        changeBtn.setLayoutParams(paramsMatchWrap);
        changeBtn.setGravity(Gravity.CENTER);
        changeBtn.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        changeBtn.setOnClickListener(new ChangeBtnListener());
        btnLayout.addView(changeBtn);*/

        switch (SettingsStorage.textSize) {
            case 0:
                dayTV.setTextSize(8.0f);
                homeworkTV.setTextSize(8.0f);
                break;
            case 2:
                dayTV.setTextSize(24.0f);
                homeworkTV.setTextSize(24.0f);
                break;
            default:
                dayTV.setTextSize(16.0f);
                homeworkTV.setTextSize(16.0f);
                break;
        }

        /*MaterialDivider endDivider = new MaterialDivider(getContext());
        endDivider.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.divider_color, getContext().getTheme()
        ));
        this.addView(endDivider);*/
    }

    /*private class DeleteBtnListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            new ScheduleDBHelper(getContext()).deleteHomework(
                    flowLvl, course, group, subgroup,
                    homework.year, homework.month, homework.day, homework.lessonNum
            );
            ((LinearLayout) HomeworkView.this.getParent()).removeView(HomeworkView.this);
        }
    }

    private class ChangeBtnListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), ChangeHomeworkActivity.class);
            intent.putExtra("flowLvl", flowLvl);
            intent.putExtra("course", course);
            intent.putExtra("group", group);
            intent.putExtra("subgroup", subgroup);
            intent.putExtra("year", homework.year);
            intent.putExtra("month", homework.month);
            intent.putExtra("day", homework.day);
            intent.putExtra("lessonNum", homework.lessonNum);
            intent.putExtra("lessonName", homework.lessonName);
            intent.putExtra("homework", homework.homework);
            getContext().startActivity(intent);
        }
    }*/
}