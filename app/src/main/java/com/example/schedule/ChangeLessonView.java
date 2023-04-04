package com.example.schedule;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;

public class ChangeLessonView extends LinearLayout {
    private int lessonNum;
    private String lessonName, teacher, cabinet;
    TextView lessonTV, cabinetTV;

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

        LinearLayout.LayoutParams paramsMatchWrap = new LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        LayoutParams paramsWrapWrap = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
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

        if (!lessonName.isEmpty()) {
            lessonTV = new TextView(getContext());
            lessonTV.setLayoutParams(paramsMatchWrap);
            lessonTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            lessonTV.setTextSize(14.0f);
            lessonTV.setText(lessonName);
            lessonTV.setPadding(50, 0, 0, 0);
            this.addView(lessonTV);

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
                this.addView(cabinetTV);
            }
        }

        LinearLayout changeStroke = new LinearLayout(getContext());
        changeStroke.setLayoutParams(paramsMatchWrap);
        changeStroke.setOrientation(HORIZONTAL);
        changeStroke.setPadding(0, 10, 0, 10);
        this.addView(changeStroke);

        Button deleteLessonBtn = new Button(getContext());
        deleteLessonBtn.setText("Удалить");
        deleteLessonBtn.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        deleteLessonBtn.setOnClickListener(new DeleteLessonBtnListener());
        changeStroke.addView(deleteLessonBtn);

        Button changeLessonBtn = new Button(getContext());
        changeLessonBtn.setText("Изменить");
        changeLessonBtn.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        changeLessonBtn.setOnClickListener(new ChangeLessonBtnListener());
        changeStroke.addView(changeLessonBtn);
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
                removeView(lessonTV);
                removeView(cabinetTV);
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
