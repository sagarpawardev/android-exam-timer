package dev.sagar.examtimer.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dev.sagar.examtimer.R;
import dev.sagar.examtimer.converter.TimerQuestionStateConverter;
import dev.sagar.examtimer.dto.ExamStateDto;
import dev.sagar.examtimer.dto.QuestionStateDto;
import dev.sagar.examtimer.service.ExamLogService;
import dev.sagar.examtimer.pojo.ExamLog;
import dev.sagar.examtimer.utils.CountUpTimer;

public class ExamActivity extends AppCompatActivity {

    private static final String EXAM_STATE = "keyExamState";

    private CountUpTimer prevTimer = null;
    private CountUpTimer[] timers;
    private LocalDateTime startTime;
    private long backPressedTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_activity);

        startTime = LocalDateTime.now();

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
            if (prevTimer != null && prevTimer.isRunning) {
                prevTimer.pause();
            }
            showConfirmationDialog();
        });

        // Setup Grid Rows
        for (int qNo = 1; qNo <= count; qNo += 2) {
            View gridRow = inflater.inflate(R.layout.inner_grid, parent, false);

            // Prepare Grid 1
            View grid1 = gridRow.findViewById(R.id.grid1);
            TextView tvQNo1 = gridRow.findViewById(R.id.tv_q_number);
            tvQNo1.setText(String.valueOf(qNo));
            CountUpTimer timer1 = new CountUpTimer(this, grid1);
            grid1.setOnClickListener(new TimerOnClickListener(timer1));
            timers[qNo - 1] = timer1;

            // Prepare Grid 2
            View grid2 = gridRow.findViewById(R.id.grid2);
            if (qNo + 1 <= count) {
                TextView tvQNo2 = gridRow.findViewById(R.id.tv_q_number2);
                tvQNo2.setText(String.valueOf(qNo + 1));
                CountUpTimer timer2 = new CountUpTimer(this, grid2);
                grid2.setOnClickListener(new TimerOnClickListener(timer2));
                timers[qNo] = timer2;
            } else {
                grid2.setVisibility(View.GONE);
            }

            parent.addView(gridRow);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Serializable examStateSerializable = savedInstanceState.getSerializable(EXAM_STATE);
        if(examStateSerializable instanceof ExamStateDto){
            ExamStateDto examState = (ExamStateDto) examStateSerializable;

            //Add start time
            startTime = examState.getStartTime();

            //Reload timers
            List<QuestionStateDto> questionStateList = examState.getQuestionStateList();
            if(questionStateList.size() == this.timers.length){
                for(int i=0; i<questionStateList.size(); i++){
                    QuestionStateDto questionState = questionStateList.get(i);
                    this.timers[i].setTimeSpent(questionState.getTimeSpent());
                    if(questionState.isRunning()) {
                        this.prevTimer = this.timers[i];
                        this.timers[i].start();
                    }
                    if(questionState.isVisited() && !questionState.isRunning()){
                        this.timers[i].pause();
                    }
                }
            }
            else{
                Log.e("ExamActivity", String.format("Timer and saved state size doesn't match (timers:%s, savedState:%s)", this.timers.length, questionStateList.size()));
            }
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        ExamStateDto examState = new ExamStateDto();
        List<QuestionStateDto> questionState = TimerQuestionStateConverter.toQuestionState(this.timers);
        examState.setQuestionStateList(questionState);
        examState.setStartTime(startTime);
        outState.putSerializable(EXAM_STATE, examState);
    }

    class TimerOnClickListener implements View.OnClickListener {
        private final CountUpTimer timer;

        private TimerOnClickListener(CountUpTimer timer) {
            this.timer = timer;
        }

        @Override
        public void onClick(View view) {
            if (timer.isRunning) {
                timer.pause();
                Toast.makeText(ExamActivity.this, "Paused...", Toast.LENGTH_SHORT).show();
            } else {
                if (prevTimer != null && prevTimer.isRunning) {
                    prevTimer.pause();
                }
                prevTimer = timer;
                timer.start();
            }
        }
    }

    public void prepareFinish() {
        int attemptedQuestion = 0;
        final LocalDateTime endTime = LocalDateTime.now();

        // Create Exam Log
        ExamLog examLog = new ExamLog();
        List<ExamLog.QuestionLog> questionLogs = new ArrayList<>(timers.length);
        for (int i = 0; i < timers.length; i++) {
            ExamLog.QuestionLog questionLog = new ExamLog.QuestionLog();
            CountUpTimer timer = timers[i];
            Duration duration = Duration.ofMillis(timer.getSpentTime());
            questionLog.setDuration(duration);
            questionLog.setIndex(i + 1);
            questionLogs.add(questionLog);

            if (!duration.isZero()) {
                attemptedQuestion++;
            }
        }
        examLog.setQuestionLogList(questionLogs);
        examLog.setQuestionsAttempted(attemptedQuestion);
        examLog.setEndDateTime(endTime);
        examLog.setStartDateTime(startTime);
        examLog.setActiveTime(Duration.between(startTime, endTime));

        // Save Exam Log
        examLog = ExamLogService.getInstance(this).createExamLog(examLog);

        // Start Activity
        Intent intent = new Intent(this, HistoryDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(HistoryDetailActivity.KEY_EXAM_ID, examLog.getId());
        intent.putExtras(bundle);

        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            showConfirmationDialog();
            return;
        }
        backPressedTime = System.currentTimeMillis();
    }

    public void showConfirmationDialog(){
        new AlertDialog.Builder(this)
                .setMessage(R.string.exam_stop)
                .setPositiveButton(R.string.stop, (dialog, id) -> prepareFinish())
                .setNegativeButton(R.string.no, (dialog, id) -> dialog.cancel())
                .create()
                .show();
    }
}
