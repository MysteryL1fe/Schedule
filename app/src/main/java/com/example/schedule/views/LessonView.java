package com.example.schedule.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.schedule.Homework;
import com.example.schedule.LessonStruct;
import com.example.schedule.R;
import com.example.schedule.ScheduleDBHelper;
import com.example.schedule.SettingsStorage;
import com.example.schedule.Utils;
import com.example.schedule.activities.ChangeHomeworkActivity;
import com.example.schedule.activities.ChangeLessonActivity;
import com.example.schedule.activities.ScheduleActivity;
import com.example.schedule.fragments.NewHomeworkFragment;
import com.example.schedule.fragments.ScheduleFragment;
import com.example.schedule.fragments.TempScheduleFragment;
import com.google.android.material.divider.MaterialDivider;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class LessonView extends LinearLayout {
    private LinearLayout firstStroke;
    private TimerView timerView;
    private int flowLvl, course, group, subgroup, day, month, year, lessonNum, dayOfWeek;
    private boolean isNumerator, isTempView, isHomeworkView, isTempLesson, willLessonBe = true;
    private boolean shouldShow, isTimerViewBefore;
    private String lessonName, homework;
    private TempScheduleFragment tempScheduleFragment;
    private NewHomeworkFragment newHomeworkFragment;

    public LessonView(Context context) {
        super(context);
    }

    public LessonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LessonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LessonView(Context context, int flowLvl, int course, int group, int subgroup, int year,
                      int month, int day, int dayOfWeek, boolean isNumerator, int lessonNum) {
        super(context);
        init(
                flowLvl, course, group, subgroup, year, month, day, dayOfWeek, isNumerator,
                lessonNum
        );
    }

    public LessonView(Context context, int flowLvl, int course, int group, int subgroup, int year,
                      int month, int day, int dayOfWeek, boolean isNumerator, int lessonNum,
                      TempScheduleFragment tempScheduleFragment) {
        super(context);
        this.isTempView = true;
        this.tempScheduleFragment = tempScheduleFragment;
        init(
                flowLvl, course, group, subgroup, year, month, day, dayOfWeek, isNumerator,
                lessonNum
        );
    }

    public LessonView(Context context, int flowLvl, int course, int group, int subgroup, int year,
                      int month, int day, int dayOfWeek, boolean isNumerator, int lessonNum,
                      NewHomeworkFragment newHomeworkFragment) {
        super(context);
        this.isHomeworkView = true;
        this.newHomeworkFragment = newHomeworkFragment;
        init(
                flowLvl, course, group, subgroup, year, month, day, dayOfWeek, isNumerator,
                lessonNum
        );
    }

    private void init(int flowLvl, int course, int group, int subgroup, int year, int month,
                      int day, int dayOfWeek, boolean isNumerator, int lessonNum) {
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

        this.flowLvl = flowLvl;
        this.course = course;
        this.group = group;
        this.subgroup = subgroup;
        this.year = year;
        this.month = month;
        this.day = day;
        this.dayOfWeek = dayOfWeek;
        this.lessonNum = lessonNum;
        this.isNumerator = isNumerator;
        this.isTempLesson = false;
        this.willLessonBe = true;

        shouldShow = SettingsStorage.displayModeFull;

        ScheduleDBHelper dbHelper = new ScheduleDBHelper(getContext());

        LessonStruct lesson = dbHelper.getLesson(
                flowLvl, course, group, subgroup, dayOfWeek, lessonNum, isNumerator
        );

        lessonName = "";
        String lessonCabinet = "", lessonTeacher = "";
        if (lesson != null) {
            lessonName = lesson.lessonName;
            lessonCabinet = lesson.cabinet;
            if (lesson.surname != null && !lesson.surname.isEmpty()) {
                lessonTeacher = lesson.surname.substring(0, 1).toUpperCase()
                        + lesson.surname.substring(1);
                if (lesson.teacherName != null && !lesson.teacherName.isEmpty()) {
                    lessonTeacher += " " + lesson.teacherName.substring(0, 1).toUpperCase() + ".";
                }
                if (lesson.patronymic != null && !lesson.patronymic.isEmpty()) {
                    lessonTeacher += " " + lesson.patronymic.substring(0, 1).toUpperCase() + ".";
                }
            }
            shouldShow = true;
        }

        LessonStruct tempLesson = dbHelper.getTempLesson(
                flowLvl, course, group, subgroup, year, month, day, lessonNum
        );
        if (tempLesson != null && tempLesson.willLessonBe) {
            lessonName = tempLesson.lessonName;
            lessonCabinet = tempLesson.cabinet;
            if (tempLesson.surname != null && !tempLesson.surname.isEmpty()) {
                lessonTeacher = tempLesson.surname.substring(0, 1).toUpperCase()
                        + tempLesson.surname.substring(1);
                if (tempLesson.teacherName != null && !tempLesson.teacherName.isEmpty()) {
                    lessonTeacher += " " + tempLesson.teacherName.substring(0, 1).toUpperCase() + ".";
                }
                if (tempLesson.patronymic != null && !tempLesson.patronymic.isEmpty()) {
                    lessonTeacher += " " + tempLesson.patronymic.substring(0, 1).toUpperCase() + ".";
                }
            }
            isTempLesson = true;
            shouldShow = true;
        } else if (tempLesson != null) {
            willLessonBe = false;
            shouldShow = true;
        }

        Homework homework = dbHelper.getHomework(
                flowLvl, course, group, subgroup, year, month, day, lessonNum
        );
        if (homework != null) this.homework = homework.homework;

        dbHelper.close();

        this.setLayoutParams(paramsMatchWrap);
        this.setGravity(Gravity.START);
        this.setOrientation(VERTICAL);
        this.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.lesson_background, getContext().getTheme()
        ));

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

        if (isTempLesson) {
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(paramsWrapWrap);
            imageView.setImageDrawable(ResourcesCompat.getDrawable(
                    getResources(), R.drawable.ic_temporary, getContext().getTheme()
            ));
            firstStroke.addView(imageView);
        } else if (!willLessonBe) {
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(paramsWrapWrap);
            imageView.setImageResource(R.drawable.ic_cross);
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

        if (homework == null && isHomeworkView) {
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
        } else if (isHomeworkView) {
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
                    "Домашнее задание, %s:\n%s", homework.lessonName, homework.homework
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
        } else if (homework != null && !isTempView) {
            shouldShow = true;

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
                    "Домашнее задание, %s:\n%s", homework.lessonName, homework.homework
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

        if (isTempView) {
            MaterialDivider divider = new MaterialDivider(getContext());
            divider.setBackground(ResourcesCompat.getDrawable(
                    getResources(), R.drawable.divider_color, getContext().getTheme()
            ));
            this.addView(divider);

            LinearLayout tempLessonLayout = new LinearLayout(getContext());
            tempLessonLayout.setLayoutParams(paramsMatchWrap);
            tempLessonLayout.setOrientation(HORIZONTAL);
            this.addView(tempLessonLayout);

            if (isTempLesson || !willLessonBe) {
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

            if (!lessonName.isEmpty() && willLessonBe && !isTempLesson) {
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
    }

    public boolean addTimer(Calendar curTime) {
        if (lessonName.isEmpty()) return false;

        int hourLessonBegin = Utils.getLessonBeginningHour(lessonNum);
        int minuteLessonBegin = Utils.getLessonBeginningMinute(lessonNum);
        Calendar lessonBegin = new GregorianCalendar(
                year, month - 1, day, hourLessonBegin, minuteLessonBegin
        );
        if (lessonBegin.after(curTime)) {
            LayoutParams params = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT,
                    1.0f
            );

            timerView = new TimerView(
                    getContext(),
                    (lessonBegin.getTime().getTime() - curTime.getTime().getTime()) / 1000
            );
            timerView.setLayoutParams(params);
            timerView.setGravity(Gravity.CENTER);
            this.addView(timerView, 0);

            MaterialDivider divider = new MaterialDivider(getContext());
            divider.setBackground(ResourcesCompat.getDrawable(
                    getResources(), R.drawable.divider_color, getContext().getTheme()
            ));
            this.addView(divider, 1);

            isTimerViewBefore = true;

            return true;
        }

        int hourLessonEnd = Utils.getLessonEndingHour(lessonNum);
        int minuteLessonEnd = Utils.getLessonEndingMinute(lessonNum);
        Calendar lessonEnd = new GregorianCalendar(
                year, month - 1, day, hourLessonEnd, minuteLessonEnd
        );
        if (lessonEnd.after(curTime)) {
            LayoutParams params = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT,
                    1.0f
            );

            timerView = new TimerView(
                    getContext(),
                    (lessonEnd.getTime().getTime() - curTime.getTime().getTime()) / 1000
            );
            timerView.setLayoutParams(params);
            timerView.setGravity(Gravity.END);
            firstStroke.addView(timerView, willLessonBe && !isTempLesson ? 1 : 2);

            isTimerViewBefore = false;

            return true;
        }
        return false;
    }

    public boolean isShouldShow() {
        return shouldShow;
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

    private class ChangeHomeworkBtnListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), ChangeHomeworkActivity.class);
            intent.putExtra("flowLvl", flowLvl);
            intent.putExtra("course", course);
            intent.putExtra("group", group);
            intent.putExtra("subgroup", subgroup);
            intent.putExtra("year", year);
            intent.putExtra("month", month);
            intent.putExtra("day", day);
            intent.putExtra("lessonNum", lessonNum);
            intent.putExtra("lessonName", lessonName);
            intent.putExtra("homework", homework);
            getContext().startActivity(intent);
        }
    }

    private class DeleteHomeworkBtnListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            new ScheduleDBHelper(getContext()).deleteHomework(
                    flowLvl, course, group, subgroup, year, month, day, lessonNum
            );
            homework = "";
            boolean updateTimer = timerView != null;
            LessonView.this.removeAllViews();
            init(
                    flowLvl, course, group, subgroup, day, month, year, dayOfWeek,
                    isNumerator, lessonNum
            );
            Context context = getContext();
            if (updateTimer && context instanceof ScheduleActivity) {
                ((ScheduleActivity) context).updateTimer();
            }
        }
    }

    private class CancelTempLessonBtnListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            new ScheduleDBHelper(getContext()).deleteTempSchedule(
                    flowLvl, course, group, subgroup, year, month, day, lessonNum
            );
            tempScheduleFragment.updateFragment();
        }
    }

    private class LessonWontBeBtnListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            new ScheduleDBHelper(getContext()).setLessonWontBe(
                    flowLvl, course, group, subgroup, year, month, day, lessonNum
            );
            tempScheduleFragment.updateFragment();
        }
    }

    private class ReplaceLessonBtnListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), ChangeLessonActivity.class);
            intent.putExtra("flowLvl", flowLvl);
            intent.putExtra("course", course);
            intent.putExtra("group", group);
            intent.putExtra("subgroup", subgroup);
            intent.putExtra("year", year);
            intent.putExtra("month", month);
            intent.putExtra("day", day);
            intent.putExtra("lessonNum", lessonNum);
            intent.putExtra("isTempView", isTempView);
            getContext().startActivity(intent);
        }
    }
}