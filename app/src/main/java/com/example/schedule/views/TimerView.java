package com.example.schedule.views;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.schedule.R;
import com.example.schedule.SettingsStorage;
import com.example.schedule.activities.ScheduleActivity;

public class TimerView extends LinearLayout {
    private ImageView imageView;
    private TextView timerTV;
    private CountDownTimer timer;

    public TimerView(Context context) {
        super(context);
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TimerView(Context context, long timerSeconds) {
        super(context);
        init(timerSeconds);
    }

    public void init(long timerSeconds) {
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        LayoutParams imageParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        imageParams.width = 32;
        imageParams.height = 32;
        imageParams.gravity = Gravity.CENTER;
        imageParams.rightMargin = 25;

        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER_HORIZONTAL);
        this.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.lesson_background, getContext().getTheme()
        ));

        timerSeconds++;

        imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.red_circle);
        imageView.setLayoutParams(imageParams);
        this.addView(imageView);

        timerTV = new TextView(getContext());
        timerTV.setText(String.format("%s", timerSeconds));
        switch (SettingsStorage.textSize) {
            case 0:
                timerTV.setTextSize(8.0f);
                break;
            case 2:
                timerTV.setTextSize(24.0f);
                break;
            default:
                timerTV.setTextSize(16.0f);
                break;
        }
        timerTV.setLayoutParams(params);
        this.addView(timerTV);

        timer = new Timer(timerSeconds * 1000L, 1000);
        timer.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        timer.cancel();
    }

    private class Timer extends CountDownTimer {
        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long remainedSecs = millisUntilFinished / 1000;
            if (remainedSecs / 3600 > 0) timerTV.setText(String.format(
                    "%2s:%2s:%2s", remainedSecs / 3600, remainedSecs / 60 % 60, remainedSecs % 60
                            ).replace(' ', '0')
            );
            else timerTV.setText(String.format(
                    "%2s:%2s", remainedSecs / 60, remainedSecs % 60
                    ).replace(' ', '0')
            );
            imageView.setVisibility(imageView.getVisibility() == VISIBLE ? INVISIBLE : VISIBLE);
        }

        @Override
        public void onFinish() {
            ViewParent parent = getParent();
            while (parent != null && !(parent instanceof LessonView)) {
                parent = parent.getParent();
            }
            if (parent != null) {
                ((LessonView) parent).removeTimer();
            }
            Context context = getContext();
            if (context instanceof ScheduleActivity) ((ScheduleActivity) context).updateTimer();
            cancel();
        }
    }
}