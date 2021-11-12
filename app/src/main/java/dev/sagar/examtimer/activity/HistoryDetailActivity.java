package dev.sagar.examtimer.activity;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.TimeZone;

import dev.sagar.examtimer.Constants;
import dev.sagar.examtimer.R;
import dev.sagar.examtimer.service.ExamLogService;
import dev.sagar.examtimer.pojo.ExamLog;
import dev.sagar.examtimer.utils.DurationUtil;

public class HistoryDetailActivity extends AppCompatActivity {

    public static final String KEY_EXAM_ID = "exam_log_id";
    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_detail_activity);

        ExamLog examLog = getExamLog();

        int totalQuestions = examLog.getQuestionLogList().size();
        int attemptedQuestions = examLog.getQuestionsAttempted();
        Duration activeTime = examLog.getQuestionLogList().stream()
                .map(ExamLog.QuestionLog::getDuration)
                .reduce(Duration.ofDays(0), (Duration::plus));
        Duration totalTime = Duration.between(examLog.getStartDateTime(), examLog.getEndDateTime());
        Duration idleTime = totalTime.minus(activeTime);

        // Date and Start Time
        TextView tvExamDate = findViewById(R.id.history_detail_date);
        String strStartDate = examLog.getStartDateTime().format(dateFormatter);
        tvExamDate.setText(strStartDate);

        // Exam Duration
        TextView tvDuration = findViewById(R.id.history_detail_time);
        String strStartTime = examLog.getStartDateTime().format(timeFormatter);
        String strEndTime = examLog.getEndDateTime().format(timeFormatter);
        String examDuration = String.format("%s - %s", strStartTime, strEndTime);
        tvDuration.setText(examDuration);

        // Total Questions
        TextView tvTotalQuestions = findViewById(R.id.history_detail_total_questions);
        tvTotalQuestions.setText(String.valueOf(totalQuestions));

        // Attempted Questions
        TextView tvAttemptedQuestion = findViewById(R.id.history_detail_attempted_questions);
        tvAttemptedQuestion.setText(String.valueOf(attemptedQuestions));

        // Idle Time
        TextView tvIdleTime = findViewById(R.id.history_detail_idle_time);
        String idleTimeStr = DurationUtil.getFormattedString(idleTime);
        tvIdleTime.setText(idleTimeStr);

        // Active Time
        TextView tvActiveTime = findViewById(R.id.history_detail_active_time);
        String activeTimeStr = DurationUtil.getFormattedString(activeTime);
        tvActiveTime.setText(activeTimeStr);

        // Populate Questions
        TableLayout table = findViewById(R.id.history_detail_table);
        populateRows(examLog, table);

        // Fab
        FloatingActionButton fabEmail = findViewById(R.id.fab_send_email);
        fabEmail.setOnClickListener(v -> sendEmail(examLog));
    }

    @NonNull
    private ExamLog getExamLog() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String examLogId = bundle.getString(KEY_EXAM_ID, EMPTY);
            ExamLog examLog = ExamLogService.getInstance(this).getExamLog(examLogId);
            if (examLog != null) {
                return examLog;
            }
            Log.e(Constants.TAG, "Invalid exam log id: " + examLogId);
        }

        Toast.makeText(this, "cannot find the exam log", Toast.LENGTH_SHORT).show();
        finish();
        Log.e(Constants.TAG, String.format("This activity requires %s as input in bundle", KEY_EXAM_ID));
        return ExamLog.Factory.getInstance(ExamLog.Factory.DUMMY);
    }

    private void populateRows(@NonNull ExamLog examLog, TableLayout table) {
        LayoutInflater inflater = LayoutInflater.from(this);

        for (ExamLog.QuestionLog questionLog : examLog.getQuestionLogList()) {
            View row = inflater.inflate(R.layout.history_detail_row_item, table, false);
            TextView tvQuestionNo = row.findViewById(R.id.history_detail_q_no);
            TextView tvDuration = row.findViewById(R.id.history_detail_duration);
            tvQuestionNo.setText(String.valueOf(questionLog.getIndex()));
            String strTime = DurationUtil.getFormattedString(questionLog.getDuration());
            tvDuration.setText(strTime);
            table.addView(row);
        }
    }

    private void sendEmail(ExamLog examLog) {
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        for (ExamLog.QuestionLog questionLog : examLog.getQuestionLogList()) {
            String strTime = DurationUtil.getFormattedString(questionLog.getDuration());
            String text = "Question " + questionLog.getIndex() + ": \t" + strTime + "\n";
            builder.append(text);
        }

        String text = builder.toString();
        String subject = "Results";
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);

        startActivity(Intent.createChooser(emailIntent, "Send Email Using: "));
    }
}