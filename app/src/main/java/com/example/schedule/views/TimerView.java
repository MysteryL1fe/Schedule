package com.example.schedule.views;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.schedule.R;

/**
 * TODO: document your custom view class.
 */
public class TimerView extends LinearLayout {
    TextView timerTV;
    LinearLayout parentView;

    public TimerView(Context context) {
        super(context);
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(int timerSeconds) {
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        this.setOrientation(HORIZONTAL);

        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.red_circle));
        imageView.setLayoutParams(params);
        this.addView(imageView);

        timerTV = new TextView(getContext());
        CountDownTimer timer = new Timer(timerSeconds * 1000L, 1000);
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
            timerTV.setText(String.format("%s:%s", remainedSecs / 60, remainedSecs % 60));
        }

        @Override
        public void onFinish() {
            cancel();
            //parentView.updateTimer;
            parentView.removeView(TimerView.this);
        }
    }
}