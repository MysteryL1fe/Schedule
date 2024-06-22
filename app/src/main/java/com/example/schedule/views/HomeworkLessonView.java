package com.example.schedule.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.schedule.R;
import com.example.schedule.SettingsStorage;
import com.example.schedule.Utils;
import com.example.schedule.activities.ChangeHomeworkActivity;
import com.example.schedule.entity.Flow;
import com.example.schedule.entity.Homework;
import com.example.schedule.entity.Lesson;
import com.example.schedule.repo.HomeworkRepo;
import com.google.android.material.divider.MaterialDivider;

import java.time.LocalDate;

public class HomeworkLessonView extends LinearLayout {
    private Flow flow;
    private LocalDate date;
    private int lessonNum;
    private Lesson lesson, tempLesson;
    private Homework homework;
    private boolean willLessonBe;
    private HomeworkRepo homeworkRepo;

    public HomeworkLessonView(Context context) {
        super(context);
    }

    public HomeworkLessonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeworkLessonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HomeworkLessonView(
            Context context, Flow flow, LocalDate date, int lessonNum, Lesson lesson,
            Homework homework, Lesson tempLesson, boolean willLessonBe
    ) {
        super(context);

        this.flow = flow;
        this.date = date;
        this.lessonNum = lessonNum;
        this.lesson = lesson;
        this.homework = homework;
        this.tempLesson = tempLesson;
        this.willLessonBe = willLessonBe;
        this.homeworkRepo = new HomeworkRepo(getContext());

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

        LinearLayout lessonData = new LinearLayout(getContext());
        lessonData.setLayoutParams(paramsMatchWrap);
        lessonData.setOrientation(VERTICAL);
        secondStroke.addView(lessonData);

        if (!lessonName.isEmpty()) {
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

        if (homework == null) {
            LinearLayout changeRow = new LinearLayout(getContext());
            changeRow.setLayoutParams(paramsWrapWrap);
            changeRow.setOrientation(HORIZONTAL);
            changeRow.setPadding(25, 0, 0, 0);
            secondStroke.addView(changeRow);

            ImageButton changeHomeworkBtn = new ImageButton(getContext());
            changeHomeworkBtn.setLayoutParams(paramsHomeworkButtons);
            changeHomeworkBtn.setImageDrawable(ContextCompat.getDrawable(
                    getContext(), R.drawable.ic_homework
            ));
            changeHomeworkBtn.setBackground(ResourcesCompat.getDrawable(
                    getResources(), R.drawable.secondary_color, getContext().getTheme()
            ));
            changeHomeworkBtn.setPadding(10, 10, 10, 10);
            changeHomeworkBtn.setOnClickListener(new ChangeHomeworkBtnListener());
            changeRow.addView(changeHomeworkBtn);
        } else {
            MaterialDivider divider = new MaterialDivider(getContext());
            divider.setBackground(ResourcesCompat.getDrawable(
                    getResources(), R.drawable.divider_color, getContext().getTheme()
            ));
            this.addView(divider);

            LinearLayout thirdStroke = new LinearLayout(getContext());
            thirdStroke.setLayoutParams(paramsMatchWrap);
            thirdStroke.setOrientation(HORIZONTAL);
            thirdStroke.setPadding(25, 0, 0, 0);
            this.addView(thirdStroke);

            TextView homeworkTV = new TextView(getContext());
            homeworkTV.setLayoutParams(paramsMatchWrap);
            homeworkTV.setText(String.format(
                    "Домашнее задание, %s:\n%s", homework.getLessonName(), homework.getHomework()
            ));
            thirdStroke.addView(homeworkTV);

            switch (SettingsStorage.textSize) {
                case 0:
                    homeworkTV.setTextSize(7.0f);
                    break;
                case 2:
                    homeworkTV.setTextSize(21.0f);
                    break;
                default:
                    homeworkTV.setTextSize(14.0f);
                    break;
            }

            ImageButton changeHomeworkBtn = new ImageButton(getContext());
            changeHomeworkBtn.setLayoutParams(paramsHomeworkButtons);
            changeHomeworkBtn.setImageDrawable(ResourcesCompat.getDrawable(
                    getResources(), R.drawable.ic_homework, getContext().getTheme()
            ));
            changeHomeworkBtn.setBackground(ResourcesCompat.getDrawable(
                    getResources(), R.drawable.secondary_color, getContext().getTheme()
            ));
            changeHomeworkBtn.setPadding(10, 10, 10, 10);
            changeHomeworkBtn.setOnClickListener(new ChangeHomeworkBtnListener());
            thirdStroke.addView(changeHomeworkBtn);

            ImageButton deleteHomeworkBtn = new ImageButton(getContext());
            deleteHomeworkBtn.setLayoutParams(paramsHomeworkButtons);
            deleteHomeworkBtn.setImageDrawable(ResourcesCompat.getDrawable(
                    getResources(), R.drawable.ic_thrash, getContext().getTheme()
            ));
            deleteHomeworkBtn.setBackground(ResourcesCompat.getDrawable(
                    getResources(), R.drawable.secondary_color, getContext().getTheme()
            ));
            deleteHomeworkBtn.setPadding(10, 10, 10, 10);
            deleteHomeworkBtn.setOnClickListener(new DeleteHomeworkBtnListener());
            thirdStroke.addView(deleteHomeworkBtn);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private class ChangeHomeworkBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), ChangeHomeworkActivity.class);
            intent.putExtra("flowLvl", flow.getFlowLvl());
            intent.putExtra("course", flow.getCourse());
            intent.putExtra("group", flow.getFlow());
            intent.putExtra("subgroup", flow.getSubgroup());
            intent.putExtra("year", date.getYear());
            intent.putExtra("month", date.getMonthValue());
            intent.putExtra("day", date.getDayOfMonth());
            intent.putExtra("lessonNum", lessonNum);
            intent.putExtra(
                    "lessonName", tempLesson != null ? tempLesson.getName()
                            : lesson != null ? lesson.getName() : ""
            );
            intent.putExtra("homework", homework == null ? "" : homework.getHomework());
            getContext().startActivity(intent);
        }
    }

    private class DeleteHomeworkBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            homeworkRepo.deleteByFlowAndLessonDateAndLessonNum(
                    flow.getFlowLvl(), flow.getCourse(), flow.getFlow(), flow.getSubgroup(),
                    date, lessonNum
            );
            HomeworkLessonView.this.homework = null;
            HomeworkLessonView.this.removeAllViews();
            init();
        }
    }
}
