package com.example.schedule.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.schedule.BackendService;
import com.example.schedule.R;
import com.example.schedule.RetrofitHelper;
import com.example.schedule.SettingsStorage;
import com.example.schedule.Utils;
import com.example.schedule.dto.ScheduleResponse;
import com.example.schedule.entity.Lesson;
import com.example.schedule.entity.Schedule;
import com.example.schedule.views.LessonsView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import retrofit2.Call;
import retrofit2.Response;

public class FindTeacherFragment extends Fragment {
    private EditText teacherEditText;
    private LinearLayout lessonsContainer;
    private TextView notFoundTV;
    private FindTeacherThread findTeacherThread;

    public FindTeacherFragment() {}

    public static FindTeacherFragment newInstance() {
        return new FindTeacherFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_teacher, container, false);

        teacherEditText = view.findViewById(R.id.teacherEditText);
        Button findTeacherBtn = view.findViewById(R.id.findBtn);
        lessonsContainer = view.findViewById(R.id.lessonsContainer);
        notFoundTV = view.findViewById(R.id.notFoundTV);

        findTeacherBtn.setOnClickListener(new FindTeacherBtnListener());

        switch (SettingsStorage.textSize) {
            case 0:
                teacherEditText.setTextSize(12.0f);
                findTeacherBtn.setTextSize(10.0f);
                notFoundTV.setTextSize(12.0f);
                break;
            case 2:
                teacherEditText.setTextSize(36.0f);
                findTeacherBtn.setTextSize(30.0f);
                notFoundTV.setTextSize(36.0f);
                break;
            default:
                teacherEditText.setTextSize(24.0f);
                findTeacherBtn.setTextSize(20.0f);
                notFoundTV.setTextSize(24.0f);
                break;
        }

        return view;
    }

    private class FindTeacherBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (findTeacherThread != null) findTeacherThread.stopThread();

            findTeacherThread = new FindTeacherThread();
            findTeacherThread.start();
        }
    }

    private class FindTeacherThread extends Thread {
        private boolean isActive = true;

        @Override
        public void run() {
            super.run();
            try {
                Activity activity = getActivity();
                if (activity == null) return;
                activity.runOnUiThread(() -> notFoundTV.setVisibility(View.GONE));
                BackendService backendService = RetrofitHelper.getBackendService();
                Call<List<ScheduleResponse>> call = backendService.getAllSchedulesByTeacher(
                        teacherEditText.getText().toString()
                );
                Response<List<ScheduleResponse>> response = call.execute();
                List<ScheduleResponse> schedules = response.body();
                Log.w("Backend", "schedules: " + schedules);

                if (!isActive) return;

                getActivity().runOnUiThread(() -> lessonsContainer.removeAllViews());

                if (schedules == null || schedules.isEmpty()) {
                    getActivity().runOnUiThread(
                            () -> notFoundTV.setVisibility(View.VISIBLE)
                    );
                    return;
                }

                PriorityQueue<ScheduleHelper> queue = new PriorityQueue<>();
                for (ScheduleResponse scheduleResponse : schedules) {
                    if (!isActive) return;

                    Lesson lesson = new Lesson();
                    lesson.setName(scheduleResponse.getLesson().getName());
                    lesson.setTeacher(scheduleResponse.getLesson().getTeacher());
                    lesson.setCabinet(scheduleResponse.getLesson().getCabinet());

                    Schedule schedule = new Schedule();
                    schedule.setDayOfWeek(scheduleResponse.getDayOfWeek());
                    schedule.setLessonNum(scheduleResponse.getLessonNum());
                    schedule.setNumerator(scheduleResponse.isNumerator());

                    ScheduleHelper scheduleHelper = new ScheduleHelper(
                            schedule, lesson,
                            Utils.getNearestLesson(
                                    schedule.getDayOfWeek(), schedule.getLessonNum(), schedule.isNumerator()
                            )
                    );
                    if (!queue.contains(scheduleHelper))
                        queue.add(scheduleHelper);
                }

                LessonsView lessonsView = null;
                LocalDate lastDate = null;
                List<LessonsView> lessonsViews = new ArrayList<>();

                while (!queue.isEmpty()) {
                    if (!isActive) return;

                    ScheduleHelper schedule = queue.poll();
                    if (lastDate == null || !lastDate.isEqual(schedule.dateTime.toLocalDate())) {
                        lastDate = schedule.dateTime.toLocalDate();
                        lessonsView = new LessonsView(getContext(), lastDate);
                        lessonsViews.add(lessonsView);
                    }
                    lessonsView.addLesson(
                            schedule.schedule.getLessonNum(),
                            schedule.lesson
                    );
                }

                if (!isActive) return;

                getActivity().runOnUiThread(() -> {
                    for (LessonsView view : lessonsViews) {
                        lessonsContainer.addView(view);
                    }
                });
            } catch (Exception e) {
                Log.e("Backend", e.toString());

                Activity activity = getActivity();
                if (activity == null) return;
                if (isActive) activity.runOnUiThread(
                        () -> notFoundTV.setVisibility(View.VISIBLE)
                );
            }
        }

        public void stopThread() {
            isActive = false;
        }

        private class ScheduleHelper implements Comparable<ScheduleHelper> {
            private final Schedule schedule;
            private final Lesson lesson;
            private final LocalDateTime dateTime;

            public ScheduleHelper(Schedule schedule, Lesson lesson, LocalDateTime dateTime) {
                this.schedule = schedule;
                this.lesson = lesson;
                this.dateTime = dateTime;
            }

            public Schedule getSchedule() {
                return schedule;
            }

            public LocalDateTime getDateTime() {
                return dateTime;
            }

            public Lesson getLesson() {
                return lesson;
            }

            @Override
            public int compareTo(ScheduleHelper o) {
                return this.dateTime.compareTo(o.dateTime);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                ScheduleHelper that = (ScheduleHelper) o;

                return dateTime.equals(that.dateTime)
                        && lesson.equals(that.lesson);
            }

            @Override
            public int hashCode() {
                return dateTime.hashCode();
            }
        }
    }
}