package com.example.schedule.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.schedule.R;
import com.example.schedule.SettingsStorage;
import com.example.schedule.Utils;
import com.example.schedule.entity.Homework;
import com.example.schedule.entity.Lesson;
import com.google.android.material.divider.MaterialDivider;

public class LessonView extends LinearLayout {
    private LinearLayout firstStroke;
    private int lessonNum;
    private Lesson lesson, tempLesson = null;
    private Homework homework = null;
    private boolean willLessonBe = true;
    private TimerView timerView;
    private boolean isTimerViewBefore, containsLesson = false;

    public LessonView(Context context) {
        super(context);
    }

    public LessonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LessonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LessonView(Context context, int lessonNum, Lesson lesson) {
        super(context);

        this.lessonNum = lessonNum;
        this.lesson = lesson;

        init();
    }

    public LessonView(
            Context context, int lessonNum, Lesson lesson, Homework homework,
            Lesson tempLesson, boolean willLessonBe
    ) {
        super(context);

        this.lessonNum = lessonNum;
        this.lesson = lesson;
        this.homework = homework;
        this.tempLesson = tempLesson;
        this.willLessonBe = willLessonBe;

        init();
    }

    private void init() {
        LinearLayout.LayoutParams paramsMatchWrap = new LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        LayoutParams paramsWrapWrap = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        LayoutParams paramsHomeworkButtons = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        paramsHomeworkButtons.rightMargin = 10;

        this.setLayoutParams(paramsMatchWrap);
        this.setGravity(Gravity.START);
        this.setOrientation(VERTICAL);
        this.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.lesson_background, getContext().getTheme()
        ));

        this.containsLesson = lesson != null || homework != null || tempLesson != null;

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

        firstStroke = new LinearLayout(getContext());
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

        if (homework != null) {
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
        }
    }

    public void addTimerView(TimerView timerView, boolean isBefore) {
        this.timerView = timerView;
        if (isBefore) {
            this.addView(timerView, 0);

            MaterialDivider divider = new MaterialDivider(getContext());
            divider.setBackground(ResourcesCompat.getDrawable(
                    getResources(), R.drawable.divider_color, getContext().getTheme()
            ));
            this.addView(divider, 1);

            isTimerViewBefore = true;
        } else {
            firstStroke.addView(timerView, willLessonBe && tempLesson == null ? 1 : 2);

            isTimerViewBefore = false;
        }
    }

    public void removeTimer() {
        if (timerView != null && isTimerViewBefore) {
            LessonView.this.removeViewAt(0);
            LessonView.this.removeViewAt(0);
            timerView = null;
        } else if (timerView != null) {
            firstStroke.removeView(timerView);
            timerView = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeTimer();
    }

    public boolean isContainsLesson() {
        return containsLesson;
    }
}