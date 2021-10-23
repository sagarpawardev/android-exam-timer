package dev.sagar.examtimer.history;

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

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import dev.sagar.examtimer.Constants;
import dev.sagar.examtimer.R;
import dev.sagar.examtimer.pojo.ExamLog;
import dev.sagar.examtimer.utils.DurationUtil;

public class HistoryDetailActivity extends AppCompatActivity {

    public static final String EXAM_LOG_ID = "exam_log_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_detail_activity);

        ExamLog examLog = getExamLog();
        TableLayout table = findViewById(R.id.history_detail_table);
        populateRows(examLog, table);

        FloatingActionButton fabEmail = findViewById(R.id.fab_send_email);
        fabEmail.setOnClickListener(v -> sendEmail(examLog));
    }

    @NonNull
    private ExamLog getExamLog(){
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            String examLogId = bundle.getString(EXAM_LOG_ID, EMPTY);
            ExamLog examLog = ExamLogService.getInstance().getExamLog(examLogId);
            if(examLog!=null) {
                return examLog;
            }
            Log.e(Constants.TAG, "Invalid exam log id: " + examLogId);
        }

        Toast.makeText(this, "cannot find the exam log", Toast.LENGTH_SHORT).show();
        finish();
        Log.e(Constants.TAG, String.format("This activity requires %s as input in bundle", EXAM_LOG_ID));
        return ExamLog.Factory.getInstance(ExamLog.Factory.DUMMY);
    }

    private void populateRows(@NonNull ExamLog examLog, TableLayout table){
        LayoutInflater inflater = LayoutInflater.from(this);

        for( ExamLog.QuestionLog questionLog: examLog.getQuestionLogList()){
            View row = inflater.inflate(R.layout.history_detail_row_item, table, false);
            TextView tvQuestionNo = row.findViewById(R.id.history_detail_q_no);
            TextView tvDuration = row.findViewById(R.id.history_detail_duration);
            tvQuestionNo.setText(String.valueOf(questionLog.getIndex()));
            String strTime = DurationUtil.getFormattedString(questionLog.getDuration());
            tvDuration.setText(strTime);
            table.addView(row);
        }
    }

    private void sendEmail(ExamLog examLog){
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        for(ExamLog.QuestionLog questionLog: examLog.getQuestionLogList()){
            String strTime = DurationUtil.getFormattedString(questionLog.getDuration());
            String text = "Question "+questionLog.getIndex()+": \t"+strTime+"\n";
            builder.append(text);
        }

        String text = builder.toString();
        String subject = "Results";
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);

        startActivity(Intent.createChooser(emailIntent, "Send Email Using: "));
    }
}