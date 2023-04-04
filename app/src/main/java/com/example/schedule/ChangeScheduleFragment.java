package com.example.schedule;

import static android.content.Context.MODE_PRIVATE;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.schedule.exceptions.ScheduleException;

import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangeScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeScheduleFragment extends Fragment {

    private static final String ARG_FLOW_LVL = "flowLvl";
    private static final String ARG_COURSE = "course";
    private static final String ARG_GROUP = "group";
    private static final String ARG_SUBGROUP = "subgroup";

    private int mFlowLvl = 0, mCourse = 0, mGroup = 0, mSubgroup = 0;
    private Schedule mSchedule = null;
    private LinearLayout mLessonsContainer;

    public ChangeScheduleFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChangeScheduleFragment.
     */
    public static ChangeScheduleFragment newInstance(int flowLvl, int course, int group,
                                                     int subgroup) {
        ChangeScheduleFragment fragment = new ChangeScheduleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FLOW_LVL, flowLvl);
        args.putInt(ARG_COURSE, course);
        args.putInt(ARG_GROUP, group);
        args.putInt(ARG_SUBGROUP, subgroup);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFlowLvl = getArguments().getInt(ARG_FLOW_LVL);
            mCourse = getArguments().getInt(ARG_COURSE);
            mGroup = getArguments().getInt(ARG_GROUP);
            mSubgroup = getArguments().getInt(ARG_SUBGROUP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Set<Schedule> storage = ScheduleStorage.getStorage();
        for (Schedule schedule : storage) {
            if (schedule.getFlowLvl() == mFlowLvl
                    && schedule.getCourse() == mCourse
                    && schedule.getGroup() == mGroup
                    && schedule.getSubgroup() == mSubgroup) {
                mSchedule = schedule;
                break;
            }
        }

        View view = inflater.inflate(R.layout.fragment_change_schedule, container, false);

        if (mSchedule == null) {
            try {
                mSchedule = new Schedule(mFlowLvl, mCourse, mGroup, mSubgroup);
                ScheduleStorage.addSchedule(mSchedule, this.getActivity()
                        .getSharedPreferences("ScheduleSaves", MODE_PRIVATE));
            } catch (ScheduleException e) {
                return view;
            }
        }

        mLessonsContainer = view.findViewById(R.id.lessons_container);

        LoadLessons loadLessons = new LoadLessons();
        loadLessons.execute();

        return view;
    }

    private class LoadLessons extends AsyncTask<Void, Void, Void> {
        private final ChangeLessonsView[] changeLessonsViews = new ChangeLessonsView[14];

        @Override
        protected Void doInBackground(Void... voids) {
            int dayOfWeek = 1;
            boolean isNumerator = true;

            for (int i = 0; i < 12; i++) {
                ChangeLessonsView changeLessonsView = new ChangeLessonsView(
                        mLessonsContainer.getContext(), mSchedule, dayOfWeek, isNumerator);
                changeLessonsViews[i] = changeLessonsView;
                if (++dayOfWeek == 7) {
                    dayOfWeek = 1;
                    isNumerator = !isNumerator;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            for (int i = 0; i < 12; i++) {
                mLessonsContainer.addView(changeLessonsViews[i]);
            }
        }
    }
}