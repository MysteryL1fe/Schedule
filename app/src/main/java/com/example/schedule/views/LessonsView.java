package com.example.schedule.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.schedule.R;
import com.example.schedule.ScheduleDBHelper;
import com.example.schedule.SettingsStorage;
import com.example.schedule.Utils;
import com.example.schedule.entity.Homework;
import com.example.schedule.entity.Lesson;
import com.example.schedule.entity.Schedule;
import com.example.schedule.entity.TempSchedule;
import com.example.schedule.repo.FlowRepo;
import com.example.schedule.repo.HomeworkRepo;
import com.example.schedule.repo.LessonRepo;
import com.example.schedule.repo.ScheduleRepo;
import com.example.schedule.repo.TempScheduleRepo;
import com.google.android.material.divider.MaterialDivider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class LessonsView extends LinearLayout {
    private boolean shouldShow;
    private LocalDate date;
    private final LessonView[] lessonViews = new LessonView[8];

    private LessonRepo lessonRepo;
    private HomeworkRepo homeworkRepo;
    private ScheduleRepo scheduleRepo;
    private TempScheduleRepo tempScheduleRepo;

    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );
    LinearLayout.LayoutParams thisParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    public LessonsView(Context context) {
        super(context);
    }

    public LessonsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LessonsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LessonsView(Context context, LocalDate date) {
        super(context);
        init(date);
    }

    public LessonsView(
            Context context, int flowLvl, int course, int group, int subgroup, LocalDate date
    ) {
        super(context);
        init(date);
        loadLessons(flowLvl, course, group, subgroup, date);
    }

    public void init(LocalDate date) {
        thisParams.bottomMargin = 25;

        this.setOrientation(VERTICAL);
        this.setPadding(5, 15, 5, 15);
        this.setLayoutParams(thisParams);

        this.date = date;

        ScheduleDBHelper dbHelper = new ScheduleDBHelper(getContext());
        FlowRepo flowRepo = new FlowRepo(dbHelper);
        lessonRepo = new LessonRepo(dbHelper);
        homeworkRepo = new HomeworkRepo(dbHelper, flowRepo);
        scheduleRepo = new ScheduleRepo(dbHelper, flowRepo, lessonRepo);
        tempScheduleRepo = new TempScheduleRepo(dbHelper, flowRepo, lessonRepo);

        TextView textView = new TextView(getContext());
        textView.setText(date.format(
                DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
        ));
        switch (SettingsStorage.textSize) {
            case 0:
                textView.setTextSize(10.0f);
                break;
            case 2:
                textView.setTextSize(30.0f);
                break;
            default:
                textView.setTextSize(20.0f);
        }
        LessonsView.this.addView(textView);

        MaterialDivider firstDivider = new MaterialDivider(getContext());
        firstDivider.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.divider_color, getResources().newTheme()
        ));
        LessonsView.this.addView(firstDivider);
    }

    public void loadLessons(
            int flowLvl, int course, int group, int subgroup, LocalDate date
    ) {
        this.removeAllViews();

        boolean isDisplayModeFull = SettingsStorage.displayModeFull;
        shouldShow = isDisplayModeFull;

        TextView textView = new TextView(getContext());
        textView.setText(date.format(
                DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
        ));
        switch (SettingsStorage.textSize) {
            case 0:
                textView.setTextSize(10.0f);
                break;
            case 2:
                textView.setTextSize(30.0f);
                break;
            default:
                textView.setTextSize(20.0f);
        }
        LessonsView.this.addView(textView);

        MaterialDivider firstDivider = new MaterialDivider(getContext());
        firstDivider.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.divider_color, getResources().newTheme()
        ));
        LessonsView.this.addView(firstDivider);

        boolean isNumerator = Utils.isNumerator(date);

        for (int i = 1; i < 9; i++) {
            Schedule schedule = scheduleRepo.findByFlowAndDayOfWeekAndLessonNumAndNumerator(
                    flowLvl, course, group, subgroup, date.getDayOfWeek().getValue(), i, isNumerator
            );
            Lesson lesson = schedule == null ? null : lessonRepo.findById(schedule.getLesson());

            Homework homework = homeworkRepo.findByFlowAndLessonDateAndLessonNum(
                    flowLvl, course, group, subgroup, date, i
            );

            TempSchedule tempSchedule = tempScheduleRepo.findByFlowAndLessonDateAndLessonNum(
                    flowLvl, course, group, subgroup, date, i
            );
            Lesson tempLesson = tempSchedule == null ? null
                    : lessonRepo.findById(tempSchedule.getLesson());
            boolean willLessonBe = tempSchedule == null || tempSchedule.isWillLessonBe();

            if (isDisplayModeFull || schedule != null || homework != null || tempSchedule != null) {
                LessonView lessonView = new LessonView(
                        getContext(), i, lesson, homework, tempLesson, willLessonBe
                );
                lessonView.setLayoutParams(params);
                shouldShow = true;

                LessonsView.this.addView(lessonView);
                MaterialDivider divider = new MaterialDivider(getContext());
                divider.setBackground(ResourcesCompat.getDrawable(
                        getResources(), R.drawable.divider_color, getContext().getTheme()
                ));
                LessonsView.this.addView(divider);

                lessonViews[i - 1] = lessonView;
            }
        }
    }

    public void addLesson(int lessonNum, Lesson lesson) {
        LessonView lessonView = new LessonView(getContext(), lessonNum, lesson);
        lessonView.setLayoutParams(params);
        shouldShow = true;

        LessonsView.this.addView(lessonView);
        MaterialDivider divider = new MaterialDivider(getContext());
        divider.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.divider_color, getContext().getTheme()
        ));
        LessonsView.this.addView(divider);
    }

    public boolean addTimer(LocalDateTime time) {
        for (int i = 1; i < 8; i++) {
            if (lessonViews[i - 1] == null || !lessonViews[i - 1].isContainsLesson()) continue;

            LocalDateTime lessonBeginTime = LocalDateTime.of(date, LocalTime.of(
                    Utils.getLessonBeginningHour(i),
                    Utils.getLessonBeginningMinute(i)
            ));
            if (lessonBeginTime.isAfter(time)) {
                LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT,
                        1.0f
                );

                TimerView timerView = new TimerView(
                        getContext(),
                        lessonBeginTime.toEpochSecond(ZoneOffset.UTC)
                                - time.toEpochSecond(ZoneOffset.UTC)
                );
                timerView.setLayoutParams(params);
                timerView.setGravity(Gravity.CENTER);
                lessonViews[i - 1].addTimerView(timerView, true);

                return true;
            }

            LocalDateTime lessonEndTime = LocalDateTime.of(date, LocalTime.of(
                    Utils.getLessonEndingHour(i),
                    Utils.getLessonEndingMinute(i)
            ));
            if (lessonEndTime.isAfter(time)) {
                LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT,
                        1.0f
                );

                TimerView timerView = new TimerView(
                        getContext(),
                        lessonEndTime.toEpochSecond(ZoneOffset.UTC)
                                - time.toEpochSecond(ZoneOffset.UTC)
                );
                timerView.setLayoutParams(params);
                timerView.setGravity(Gravity.END);
                lessonViews[i - 1].addTimerView(timerView, false);

                return true;
            }
        }
        return false;
    }

    public boolean isShouldShow() {
        return shouldShow;
    }
}