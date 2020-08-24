package dev.sagar.examtimer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private CountUpTimer prevTimer = null;
    private CountUpTimer[] timers;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int count = 10;
        Bundle basket = getIntent().getExtras();
        if (basket != null) {
            count = basket.getInt("q_count");
        }
        timers = new CountUpTimer[count];

        LinearLayout parent = findViewById(R.id.grid_view);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prevTimer!= null && prevTimer.isRunning) {
                    prevTimer.pause();
                }
                prepareFinish();
            }
        });

        for(int i=1; i<=count; i+=2){
            View child = inflater.inflate(R.layout.inner_grid, null);
            View grid1 = child.findViewById(R.id.grid1);

            TextView tv1 = child.findViewById(R.id.tv_q_number);
            TextView tvt1 = child.findViewById(R.id.tv_time);
            String strQNo = String.valueOf(i);
            tv1.setText(strQNo);
            CountUpTimer timer1 = new CountUpTimer(this, tvt1);
            grid1.setOnClickListener(new TimerOnClickListener(timer1));
            timers[i-1] = timer1;

            View grid2 = child.findViewById(R.id.grid2);
            if(i+1<=count) {
                //TextView 2
                TextView tv2 = child.findViewById(R.id.tv_q_number2);
                TextView tvt2 = child.findViewById(R.id.tv_time2);
                strQNo = String.valueOf(i + 1);
                tv2.setText(strQNo);
                CountUpTimer timer2 = new CountUpTimer(this, tvt2);
                grid2.setOnClickListener(new TimerOnClickListener(timer2));
                timers[i] = timer2;
            }
            else{
                grid2.setVisibility(View.GONE);
            }

            parent.addView(child);
        }

        /*grid.setNumColumns(2);
        grid.setHorizontalSpacing(23);
        MyGridAdapter adapter = new MyGridAdapter(this, 16);
        grid.setAdapter(adapter);*/
    }


    class TimerOnClickListener implements View.OnClickListener {
        private CountUpTimer timer;
        private TimerOnClickListener(CountUpTimer timer){
            this.timer = timer;
        }

        @Override
        public void onClick(View view) {
            Log.i("My Tag", "Clicked");
            if (timer.isRunning) {
                timer.pause();
                Toast.makeText(MainActivity.this, "Paused...", Toast.LENGTH_SHORT).show();
            } else {
                if(prevTimer!=null && prevTimer.isRunning){
                    prevTimer.pause();
                }
                prevTimer = timer;
                timer.start();
                //Toast.makeText(MainActivity.this, "Running...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void prepareFinish(){
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
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
        //startActivity(Intent.createChooser(emailIntent, "Send email..."));

        finish();
        startActivity(Intent.createChooser(emailIntent, "Send Email Using: "));
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
