package dev.sagar.examtimer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dev.sagar.examtimer.utils.CountUpTimer;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private CountUpTimer prevTimer = null;
    private CountUpTimer[] timers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Default Counter
        int count = 10;
        Bundle basket = getIntent().getExtras();
        if (basket != null) {
            count = basket.getInt("q_count");
        }
        timers = new CountUpTimer[count];

        LinearLayout parent = findViewById(R.id.grid_view);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        // Setup Fab
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if(prevTimer!= null && prevTimer.isRunning) {
                prevTimer.pause();
            }
            prepareFinish();
        });

        // Setup Grid Rows
        for(int qNo=1; qNo<=count; qNo+=2){
            View gridRow = inflater.inflate(R.layout.inner_grid, parent, false);

            // Prepare Grid 1
            View grid1 = gridRow.findViewById(R.id.grid1);
            TextView tvQNo1 = gridRow.findViewById(R.id.tv_q_number);
            TextView tvTime1 = gridRow.findViewById(R.id.tv_time);
            tvQNo1.setText(String.valueOf(qNo));
            CountUpTimer timer1 = new CountUpTimer(this, tvTime1);
            grid1.setOnClickListener(new TimerOnClickListener(timer1));
            timers[qNo-1] = timer1;

            // Prepare Grid 2
            View grid2 = gridRow.findViewById(R.id.grid2);
            if(qNo+1<=count) {
                TextView tvQNo2 = gridRow.findViewById(R.id.tv_q_number2);
                TextView tvTime2 = gridRow.findViewById(R.id.tv_time2);
                tvQNo2.setText(String.valueOf(qNo + 1));
                CountUpTimer timer2 = new CountUpTimer(this, tvTime2);
                grid2.setOnClickListener(new TimerOnClickListener(timer2));
                timers[qNo] = timer2;
            }
            else{
                grid2.setVisibility(View.GONE);
            }

            parent.addView(gridRow);
        }
    }


    class TimerOnClickListener implements View.OnClickListener {
        private final CountUpTimer timer;
        private TimerOnClickListener(CountUpTimer timer){
            this.timer = timer;
        }

        @Override
        public void onClick(View view) {
            if (timer.isRunning) {
                timer.pause();
                Toast.makeText(MainActivity.this, "Paused...", Toast.LENGTH_SHORT).show();
            } else {
                if(prevTimer!=null && prevTimer.isRunning){
                    prevTimer.pause();
                }
                prevTimer = timer;
                timer.start();
            }
        }
    }

    public void prepareFinish(){
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        for(int i=0; i<timers.length; i++){
            CountUpTimer timer = timers[i];
            String strTime = format.format(timer.getSpentTime());
            String text = "Question "+(i+1)+": \t"+strTime+"\n";
            builder.append(text);
        }

        String text = builder.toString();
        String subject = "Results";
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);

        finish();
        startActivity(Intent.createChooser(emailIntent, "Send Email Using: "));
    }

    @Override
    public void onBackPressed() {

    }
}
