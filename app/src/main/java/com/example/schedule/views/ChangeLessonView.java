package com.example.schedule.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.schedule.activities.ChangeLessonActivity;
import com.example.schedule.R;
import com.example.schedule.activities.ScheduleActivity;
import com.example.schedule.ScheduleStorage;
import com.example.schedule.Utils;

public class ChangeLessonView extends LinearLayout {
    private int lessonNum;
    private String lessonName, teacher, cabinet;
    private TextView lessonTV, cabinetTV;
    private LinearLayout lessonData;

    public ChangeLessonView(Context context) {
        super(context);
    }

    public ChangeLessonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChangeLessonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChangeLessonView(Context context, int lessonNum) {
        super(context);
        init(lessonNum, "", "", "");
    }

    public ChangeLessonView(Context context, int lessonNum, String lessonName,
                            String lessonCabinet, String lessonTeacher) {
        super(context);
        init(lessonNum, lessonName, lessonCabinet, lessonTeacher);
    }

    public void init(int lessonNum, String lessonName, String lessonCabinet,
                     String lessonTeacher) {
        this.lessonNum = lessonNum;
        this.lessonName = lessonName;
        this.teacher = lessonTeacher;
        this.cabinet = lessonCabinet;

        LayoutParams paramsMatchWrap = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );
        LayoutParams paramsWrapWrap = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        LayoutParams paramsLessonData = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT,
                1.0f
        );

        this.setLayoutParams(paramsMatchWrap);
        this.setGravity(Gravity.START);
        this.setOrientation(VERTICAL);
        this.setBackgroundColor(getResources().getColor(R.color.gray_600));

        LinearLayout firstStroke = new LinearLayout(getContext());
        firstStroke.setLayoutParams(paramsMatchWrap);
        firstStroke.setOrientation(HORIZONTAL);
        firstStroke.setPadding(0, 10, 0, 10);
        this.addView(firstStroke);

        TextView lessonNumTV = new TextView(getContext());
        lessonNumTV.setLayoutParams(paramsWrapWrap);
        lessonNumTV.setText(String.format("%s", lessonNum));
        lessonNumTV.setTextSize(12.0f);
        lessonNumTV.setGravity(Gravity.START);
        lessonNumTV.setBackgroundResource(R.drawable.behind_lesson_num);
        lessonNumTV.setPadding(75, 5, 15, 5);
        firstStroke.addView(lessonNumTV);

        TextView timeTV = new TextView(getContext());
        timeTV.setLayoutParams(paramsMatchWrap);
        timeTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_END);
        timeTV.setText(Utils.getTimeByLesson(lessonNum));
        timeTV.setGravity(Gravity.END);
        timeTV.setPadding(0, 0, 25, 0);
        firstStroke.addView(timeTV);

        LinearLayout secondStroke = new LinearLayout(getContext());
        secondStroke.setLayoutParams(paramsMatchWrap);
        secondStroke.setOrientation(HORIZONTAL);
        secondStroke.setPadding(0, 10, 0, 10);
        this.addView(secondStroke);

        lessonData = new LinearLayout(getContext());
        lessonData.setLayoutParams(paramsLessonData);
        lessonData.setOrientation(VERTICAL);
        secondStroke.addView(lessonData);

        if (!lessonName.isEmpty()) {
            lessonTV = new TextView(getContext());
            lessonTV.setLayoutParams(paramsMatchWrap);
            lessonTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            lessonTV.setTextSize(14.0f);
            lessonTV.setText(lessonName);
            lessonTV.setPadding(50, 0, 0, 0);
            lessonData.addView(lessonTV);

            if (!lessonCabinet.isEmpty() || !lessonTeacher.isEmpty()) {
                cabinetTV = new TextView(getContext());
                cabinetTV.setLayoutParams(paramsMatchWrap);
                cabinetTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                cabinetTV.setTextSize(12.0f);
                if (lessonTeacher.isEmpty())
                    cabinetTV.setText(String.format("%s", lessonCabinet));
                else if (lessonCabinet.isEmpty())
                    cabinetTV.setText(String.format("%s", lessonTeacher));
                else
                    cabinetTV.setText(String.format("%s, %s", lessonCabinet, lessonTeacher));
                cabinetTV.setPadding(50, 0, 0, 25);
                lessonData.addView(cabinetTV);
            }
        }

        LinearLayout changeColumn = new LinearLayout(getContext());
        changeColumn.setLayoutParams(paramsWrapWrap);
        changeColumn.setOrientation(VERTICAL);
        changeColumn.setPadding(25, 0, 0, 0);
        secondStroke.addView(changeColumn);

        ImageButton changeLessonBtn = new ImageButton(getContext());
        changeLessonBtn.setLayoutParams(paramsWrapWrap);
        changeLessonBtn.setImageDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.ic_rename));
        changeLessonBtn.setOnClickListener(new ChangeLessonBtnListener());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.w("HUI", getContext().getTheme().toString());
        }
        changeColumn.addView(changeLessonBtn);

        ImageButton deleteLessonBtn = new ImageButton(getContext());
        deleteLessonBtn.setLayoutParams(paramsWrapWrap);
        deleteLessonBtn.setImageDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.ic_thrash));
        deleteLessonBtn.setOnClickListener(new DeleteLessonBtnListener());
        changeColumn.addView(deleteLessonBtn);
    }

    private class DeleteLessonBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ChangeLessonsView changeLessonsView = (ChangeLessonsView) getParent();
            ScheduleActivity scheduleActivity = (ScheduleActivity) getContext();
            try {
                ScheduleStorage.changeLesson(scheduleActivity.getFlowLvl(),
                        scheduleActivity.getCourse(), scheduleActivity.getGroup(),
                        scheduleActivity.getSubgroup(), changeLessonsView.getDayOfWeek(),
                        lessonNum, changeLessonsView.isNumerator(), "", "",
                        "", scheduleActivity.getSharedPreferences("ScheduleSaves",
                                Context.MODE_PRIVATE));
                lessonName = "";
                teacher = "";
                cabinet = "";
                lessonData.removeView(lessonTV);
                lessonData.removeView(cabinetTV);
            } catch (Exception ignored) {}
        }
    }

    private class ChangeLessonBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ChangeLessonsView changeLessonsView = (ChangeLessonsView) getParent();
            Context context = getContext();
            ScheduleActivity scheduleActivity = (ScheduleActivity) context;
            Intent intent = new Intent(context, ChangeLessonActivity.class);
            intent.putExtra("flowLvl", scheduleActivity.getFlowLvl());
            intent.putExtra("course", scheduleActivity.getCourse());
            intent.putExtra("group", scheduleActivity.getGroup());
            intent.putExtra("subgroup", scheduleActivity.getSubgroup());
            intent.putExtra("dayOfWeek", changeLessonsView.getDayOfWeek());
            intent.putExtra("lessonNum", lessonNum);
            intent.putExtra("isNumerator", changeLessonsView.isNumerator());
            intent.putExtra("lessonName", lessonName);
            intent.putExtra("teacher", teacher);
            intent.putExtra("cabinet", cabinet);
            context.startActivity(intent);
        }
    }
}
