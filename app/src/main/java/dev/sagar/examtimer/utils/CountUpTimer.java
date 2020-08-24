package dev.sagar.examtimer.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.widget.TextView;

import dev.sagar.examtimer.R;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class CountUpTimer {
    private Context context;
    long timeLeft = 0;
    long timeSpent = 0;
    private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    final long MAX_TIME = 3*60*60*1000;
    public boolean isRunning = false;
    private TextView tvTime;

    CountDownTimer timer = null;
    private Drawable drawablePlay, drawablePause;
    public CountUpTimer(Context context, TextView tv){
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        timeLeft = MAX_TIME;
        timeSpent = 0;
        this.context = context;
        this.tvTime = tv;

        String strTime = format.format(timeSpent);
        tvTime.setText(strTime);

        drawablePlay = context.getResources().getDrawable(R.drawable.round_play_arrow_24, null);
        drawablePlay.setBounds( 0, 0, 60, 60 );
        drawablePlay.setTint(Color.GREEN);
        drawablePause = context.getResources().getDrawable(R.drawable.round_pause_24, null);
        drawablePause.setBounds( 0, 0, 60, 60 );
        drawablePause.setColorFilter(Color.parseColor("#1565C0") ,PorterDuff.Mode.MULTIPLY);
    }

    public void start(){
        isRunning = true;
        timer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long l) {
                timeSpent += 1000;
                String strTime = format.format(timeSpent);
                tvTime.setText(strTime);
                tvTime.setCompoundDrawables(drawablePlay, null, null, null);
            }

            @Override
            public void onFinish() {
                timeLeft = MAX_TIME - timeSpent;
                tvTime.setCompoundDrawables(null, null, null, null);
            }
        };
        timer.start();
    }

    public long getSpentTime(){
        return timeSpent;
    }

    public void pause(){
        timer.cancel();
        timer = null;
        isRunning = false;
        tvTime.setCompoundDrawables(drawablePause, null, null, null);
    }

}
