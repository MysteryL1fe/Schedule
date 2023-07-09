package com.example.schedule.views;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.schedule.R;
import com.example.schedule.SettingsStorage;
import com.google.android.material.divider.MaterialDivider;

public class TimerView extends LinearLayout {
    private LessonsView lessonsView;
    private ImageView imageView;
    private TextView timerTV;
    private CountDownTimer timer;
    private ViewGroup parent;

    public TimerView(Context context) {
        super(context);
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TimerView(Context context, int timerSeconds, LessonsView lessonsView, ViewGroup parent) {
        super(context);
        this.lessonsView = lessonsView;
        this.parent = parent;
        init(timerSeconds);
    }

    public void init(int timerSeconds) {
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

        this.setGravity(Gravity.CENTER_HORIZONTAL);
        this.setBackground(getResources().getDrawable(
                R.drawable.lesson_background, getContext().getTheme()
        ));

        imageView = new ImageView(getContext());
        imageView.setImageDrawable(
                getResources().getDrawable(R.drawable.red_circle, getContext().getTheme())
        );
        imageView.setLayoutParams(imageParams);

        timerTV = new TextView(getContext());
        timerTV.setText(Integer.toString(timerSeconds));
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

        if (parent.getClass() == LessonsView.class) {
            this.setOrientation(VERTICAL);

            MaterialDivider divider = new MaterialDivider(getContext());
            divider.setBackground(getResources().getDrawable(
                    R.drawable.divider_color, getContext().getTheme()
            ));
            this.addView(divider);

            LinearLayout timerLayout = new LinearLayout(getContext());
            timerLayout.setOrientation(HORIZONTAL);
            timerLayout.setGravity(Gravity.CENTER);
            this.addView(timerLayout);

            timerLayout.addView(imageView);
            timerLayout.addView(timerTV);
        } else {
            this.setOrientation(HORIZONTAL);
            this.addView(imageView);
            this.addView(timerTV);
        }

        timer = new Timer(timerSeconds * 1000L, 1000);
        timer.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            timer.cancel();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        timer.cancel();
    }

    private class Timer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
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
            lessonsView.updateTimer();
            parent.removeView(TimerView.this);
            cancel();
        }
    }
}