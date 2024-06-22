package com.example.schedule.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.schedule.R;
import com.example.schedule.SettingsStorage;
import com.example.schedule.Utils;
import com.example.schedule.activities.ChangeLessonActivity;
import com.example.schedule.entity.Flow;
import com.example.schedule.entity.Lesson;
import com.example.schedule.repo.TempScheduleRepo;
import com.google.android.material.divider.MaterialDivider;

import java.time.LocalDate;

public class TempLessonView extends LinearLayout {
    private Flow flow;
    private LocalDate date;
    private int lessonNum;
    private Lesson lesson, tempLesson;
    private boolean willLessonBe;
    private TempScheduleRepo tempScheduleRepo;

    public TempLessonView(Context context) {
        super(context);
    }

    public TempLessonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TempLessonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TempLessonView(
            Context context, Flow flow, LocalDate date, int lessonNum, Lesson lesson,
            Lesson tempLesson, boolean willLessonBe
    ) {
        super(context);
        this.flow = flow;
        this.date = date;
        this.lessonNum = lessonNum;
        this.lesson = lesson;
        this.tempLesson = tempLesson;
        this.willLessonBe = willLessonBe;
        this.tempScheduleRepo = new TempScheduleRepo(getContext());

        init();
    }

    private void init() {
        LinearLayout.LayoutParams paramsMatchWrap = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        LinearLayout.LayoutParams paramsWrapWrap = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout.LayoutParams paramsHomeworkButtons = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        paramsHomeworkButtons.rightMargin = 10;

        this.setLayoutParams(paramsMatchWrap);
        this.setGravity(Gravity.START);
        this.setOrientation(VERTICAL);
        this.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.lesson_background, getContext().getTheme()
        ));

        String lessonName = "";
        String lessonTeacher = "";
        String lessonCabinet = "";
        if (lesson != null) {
            lessonName = lesson.getName();
            lessonTeacher = lesson.getTeacher();
            lessonCabinet = lesson.getCabinet();
        }

        if (tempLesson != null && willLessonBe) {
            lessonName = tempLesson.getName();
            lessonTeacher = tempLesson.getTeacher();
            lessonCabinet = tempLesson.getCabinet();
        }

        LinearLayout firstStroke = new LinearLayout(getContext());
        firstStroke.setLayoutParams(paramsMatchWrap);
        firstStroke.setOrientation(HORIZONTAL);
        firstStroke.setPadding(0, 10, 0, 10);
        this.addView(firstStroke);

        TextView lessonNumTV = new TextView(getContext());
        lessonNumTV.setLayoutParams(paramsWrapWrap);
        lessonNumTV.setText(String.format("%s", lessonNum));
        lessonNumTV.setGravity(Gravity.START);
        lessonNumTV.setBackgroundResource(R.drawable.behind_lesson_num);
        lessonNumTV.setPadding(75, 5, 15, 5);
        firstStroke.addView(lessonNumTV);

        if (!willLessonBe) {
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(paramsWrapWrap);
            imageView.setImageResource(R.drawable.ic_cross);
            firstStroke.addView(imageView);
        } else if (tempLesson != null) {
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(paramsWrapWrap);
            imageView.setImageDrawable(ResourcesCompat.getDrawable(
                    getResources(), R.drawable.ic_temporary, getContext().getTheme()
            ));
            firstStroke.addView(imageView);
        }

        TextView timeTV = new TextView(getContext());
        timeTV.setLayoutParams(paramsMatchWrap);
        timeTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_END);
        timeTV.setText(Utils.getTimeByLesson(lessonNum));
        timeTV.setGravity(Gravity.END);
        timeTV.setPadding(0, 0, 25, 0);
        firstStroke.addView(timeTV);

        switch (SettingsStorage.textSize) {
            case 0:
                lessonNumTV.setTextSize(8.0f);
                timeTV.setTextSize(8.0f);
                break;
            case 2:
                lessonNumTV.setTextSize(24.0f);
                timeTV.setTextSize(24.0f);
                break;
            default:
                lessonNumTV.setTextSize(16.0f);
                timeTV.setTextSize(16.0f);
                break;
        }

        LinearLayout secondStroke = new LinearLayout(getContext());
        secondStroke.setLayoutParams(paramsMatchWrap);
        secondStroke.setOrientation(HORIZONTAL);
        secondStroke.setPadding(0, 10, 0, 10);
        this.addView(secondStroke);

        if (!lessonName.isEmpty()) {
            LinearLayout lessonData = new LinearLayout(getContext());
            lessonData.setLayoutParams(paramsMatchWrap);
            lessonData.setOrientation(VERTICAL);
            secondStroke.addView(lessonData);

            TextView lessonTV = new TextView(getContext());
            lessonTV.setLayoutParams(paramsMatchWrap);
            lessonTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            switch (SettingsStorage.textSize) {
                case 0:
                    lessonTV.setTextSize(8.0f);
                    break;
                case 2:
                    lessonTV.setTextSize(24.0f);
                    break;
                default:
                    lessonTV.setTextSize(16.0f);
                    break;
            }
            lessonTV.setText(lessonName);
            lessonTV.setPadding(50, 0, 0, 0);
            lessonData.addView(lessonTV);

            if (!lessonCabinet.isEmpty() || !lessonTeacher.isEmpty()) {
                TextView cabinetTV = new TextView(getContext());
                cabinetTV.setLayoutParams(paramsMatchWrap);
                cabinetTV.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                switch (SettingsStorage.textSize) {
                    case 0:
                        cabinetTV.setTextSize(8.0f);
                        break;
                    case 2:
                        cabinetTV.setTextSize(24.0f);
                        break;
                    default:
                        cabinetTV.setTextSize(16.0f);
                        break;
                }
                if (lessonTeacher.isEmpty())
                    cabinetTV.setText(lessonCabinet);
                else if (lessonCabinet.isEmpty())
                    cabinetTV.setText(lessonTeacher);
                else
                    cabinetTV.setText(String.format("%s, %s", lessonCabinet, lessonTeacher));
                cabinetTV.setPadding(50, 0, 0, 25);
                lessonData.addView(cabinetTV);
            }
        }

        MaterialDivider divider = new MaterialDivider(getContext());
        divider.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.divider_color, getContext().getTheme()
        ));
        this.addView(divider);

        LinearLayout tempLessonLayout = new LinearLayout(getContext());
        tempLessonLayout.setLayoutParams(paramsMatchWrap);
        tempLessonLayout.setOrientation(HORIZONTAL);
        this.addView(tempLessonLayout);

        if (tempLesson != null || !willLessonBe) {
            Button cancelTempLessonBtn = new Button(getContext());
            cancelTempLessonBtn.setLayoutParams(paramsMatchWrap);
            cancelTempLessonBtn.setText(getResources().getString(R.string.cancel));
            cancelTempLessonBtn.setOnClickListener(new CancelTempLessonBtnListener());
            tempLessonLayout.addView(cancelTempLessonBtn);

            switch (SettingsStorage.textSize) {
                case 0:
                    cancelTempLessonBtn.setTextSize(8.0f);
                    break;
                case 2:
                    cancelTempLessonBtn.setTextSize(24.0f);
                    break;
                default:
                    cancelTempLessonBtn.setTextSize(16.0f);
                    break;
            }
        }

        if (!lessonName.isEmpty() && willLessonBe && tempLesson == null) {
            Button lessonWontBeBtn = new Button(getContext());
            lessonWontBeBtn.setLayoutParams(paramsMatchWrap);
            lessonWontBeBtn.setText(getResources().getString(R.string.lesson_wont_be));
            lessonWontBeBtn.setOnClickListener(new LessonWontBeBtnListener());
            tempLessonLayout.addView(lessonWontBeBtn);

            switch (SettingsStorage.textSize) {
                case 0:
                    lessonWontBeBtn.setTextSize(8.0f);
                    break;
                case 2:
                    lessonWontBeBtn.setTextSize(24.0f);
                    break;
                default:
                    lessonWontBeBtn.setTextSize(16.0f);
                    break;
            }
        }

        Button replaceLessonBtn = new Button(getContext());
        replaceLessonBtn.setLayoutParams(paramsMatchWrap);
        replaceLessonBtn.setText(getResources().getString(R.string.replace));
        replaceLessonBtn.setOnClickListener(new ReplaceLessonBtnListener());
        tempLessonLayout.addView(replaceLessonBtn);

        switch (SettingsStorage.textSize) {
            case 0:
                replaceLessonBtn.setTextSize(8.0f);
                break;
            case 2:
                replaceLessonBtn.setTextSize(24.0f);
                break;
            default:
                replaceLessonBtn.setTextSize(16.0f);
                break;
        }

        divider = new MaterialDivider(getContext());
        divider.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.divider_color, getContext().getTheme()
        ));
        this.addView(divider);
    }

    private class CancelTempLessonBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            tempScheduleRepo.deleteByFlowAndLessonDateAndLessonNum(
                    flow.getFlowLvl(), flow.getCourse(), flow.getFlow(), flow.getSubgroup(),
                    date, lessonNum
            );
            TempLessonView.this.tempLesson = null;
            TempLessonView.this.willLessonBe = true;
            TempLessonView.this.removeAllViews();
            init();
        }
    }

    private class LessonWontBeBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            tempScheduleRepo.addOrUpdate(
                    flow.getFlowLvl(), flow.getCourse(), flow.getFlow(), flow.getSubgroup(),
                    lesson.getName(), lesson.getTeacher(), lesson.getCabinet(),
                    date, lessonNum, false
            );
            TempLessonView.this.tempLesson = lesson;
            TempLessonView.this.willLessonBe = false;
            TempLessonView.this.removeAllViews();
            init();
        }
    }

    private class ReplaceLessonBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), ChangeLessonActivity.class);
            intent.putExtra("flowLvl", flow.getFlowLvl());
            intent.putExtra("course", flow.getCourse());
            intent.putExtra("group", flow.getFlow());
            intent.putExtra("subgroup", flow.getSubgroup());
            intent.putExtra("year", date.getYear());
            intent.putExtra("month", date.getMonthValue());
            intent.putExtra("day", date.getDayOfMonth());
            intent.putExtra("lessonNum", lessonNum);
            intent.putExtra("isTempView", true);
            getContext().startActivity(intent);
        }
    }
}
