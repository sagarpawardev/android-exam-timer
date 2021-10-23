package dev.sagar.examtimer.history;

import static java.time.format.FormatStyle.LONG;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;

import dev.sagar.examtimer.R;
import dev.sagar.examtimer.pojo.ExamLog;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(LONG);

    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_HISTORY = 1;

    private final List<ExamLog> examLogList;
    private final Activity activity;

    public HistoryListAdapter(Activity activity, List<ExamLog> examLogList) {
        this.activity = activity;
        this.examLogList = examLogList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = HistoryListViewFactory.getView(parent, viewType);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(examLogList.isEmpty())
            return;

        ExamLog examLog = examLogList.get(position);
        holder.tvTimeTaken.setText( getTimeTaken(examLog) );
        holder.tvDate.setText( getFormattedDate(examLog) );
        holder.tvAttempted.setText( getAttemptedQuestion(examLog) );
        holder.container.setOnClickListener(v ->
                Toast.makeText(activity, "Item Clicked "+ holder.getAdapterPosition(), Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemViewType(int position) {
        if( examLogList.isEmpty() ){
            return VIEW_TYPE_EMPTY;
        }

        return VIEW_TYPE_HISTORY;
    }

    @Override
    public int getItemCount() {
        if( examLogList.isEmpty() )
            return 1;
        else
            return examLogList.size();
    }

    private String getFormattedDate(ExamLog examLog){
        return examLog.getStartDateTime().format(dateFormatter);
    }

    private String getTimeTaken(ExamLog examLog){
        Duration duration = examLog.getTimeTaken();

        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        StringBuilder builder = new StringBuilder();
        if(hours > 0){
            builder.append(hours).append(" hr ");
        }

        if(minutes > 0){
            builder.append(minutes).append(" min ");
        }

        if(seconds > 0){
            builder.append(seconds).append(" sec ");
        }

        if(hours==0 && minutes==0 && seconds==0){
            builder.append("0 sec");
        }

        return builder.toString();
    }

    private String getAttemptedQuestion(ExamLog examLog){
        int attempted = examLog.getQuestionsAttempted();
        return "Attempted: " + attempted;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate;
        private final TextView tvAttempted;
        private final TextView tvTimeTaken;
        private final View container;

        public ViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.history_tv_date);
            tvAttempted = view.findViewById(R.id.history_tv_attempted);
            tvTimeTaken = view.findViewById(R.id.history_tv_time_taken);
            container = view;
        }
    }

    static private class HistoryListViewFactory {
        private HistoryListViewFactory(){}

        static View getView(ViewGroup parent, int viewType){
            View view;
            if( viewType == VIEW_TYPE_EMPTY ){
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.history_list_placeholder, parent, false);
            }
            else {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.history_list_row_item, parent, false);
            }

            return view;
        }
    }
}
