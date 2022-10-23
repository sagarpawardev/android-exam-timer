package dev.sagar.examtimer.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import dev.sagar.examtimer.R;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class CountUpTimer {
    long timeLeft;
    long timeSpent;
    private final SimpleDateFormat format;

    final long MAX_TIME = 3*60*60*1000;
    public boolean isRunning = false;
    private final TextView tvTime;
    private final View grid;
    private final int greenColor, orangeColor;

    CountDownTimer timer = null;
    private final Drawable drawablePlay, drawablePause;

    public CountUpTimer(Context context, View grid) {
        this(context, grid, 0);
    }

    public CountUpTimer(Context context, View grid, int offset){
        format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        this.greenColor = ResourcesCompat.getColor(context.getResources(), R.color.greenColor, null);
        this.orangeColor = ResourcesCompat.getColor(context.getResources(), R.color.orangeColor, null);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        timeLeft = MAX_TIME;
        timeSpent = offset;
        this.grid = grid;

        if(grid.findViewById(R.id.tv_time) != null){
            this.tvTime = grid.findViewById(R.id.tv_time);
        }
        else{
            this.tvTime = grid.findViewById(R.id.tv_time2);
        }

        String strTime = format.format(timeSpent);
        tvTime.setText(strTime);

        drawablePlay = ResourcesCompat.getDrawable(context.getResources(), R.drawable.round_play_arrow_24, null);
        drawablePlay.setBounds( 0, 0, 60, 60 );
        drawablePlay.setTint(greenColor);
        drawablePause = ResourcesCompat.getDrawable(context.getResources(), R.drawable.round_pause_24, null);
        drawablePause.setBounds( 0, 0, 60, 60 );
        drawablePause.setColorFilter(Color.parseColor("#1565C0") ,PorterDuff.Mode.MULTIPLY);
    }

    public void start(){
        isRunning = true;
        this.grid.setBackgroundColor(orangeColor);

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

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
        String strTime = format.format(timeSpent);
        tvTime.setText(strTime);
    }

    public long getSpentTime(){
        return timeSpent;
    }

    public void pause(){
        if(timer!=null) {
            timer.cancel();
            timer = null;
        }
        isRunning = false;
        this.grid.setBackgroundColor(greenColor);
        tvTime.setCompoundDrawables(drawablePause, null, null, null);
    }

}
