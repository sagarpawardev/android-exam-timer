package dev.sagar.examtimer.adapter;

import static java.time.format.FormatStyle.LONG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dev.sagar.examtimer.R;
import dev.sagar.examtimer.activity.HistoryDetailActivity;
import dev.sagar.examtimer.pojo.ExamLog;
import dev.sagar.examtimer.utils.DurationUtil;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(LONG);

    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_HISTORY = 1;

    private final List<ExamLog> examLogList;
    private final Activity activity;
    private final Callback callback;
    private final HashSet<Integer> selectedPositions;

    private boolean selectionModeActivated = false;

    public HistoryListAdapter(Activity activity, List<ExamLog> examLogList, Callback callback) {
        this.activity = activity;
        this.examLogList = examLogList;
        this.callback = callback;
        this.selectedPositions = new HashSet<>();
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
        holder.container.setOnClickListener(v -> {
            if(selectionModeActivated){
                setItemSelected(position, !isItemSelected(position));
            }
            else {
                Intent intent = new Intent(activity, HistoryDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(HistoryDetailActivity.KEY_EXAM_ID, examLog.getId());
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
        holder.container.setOnLongClickListener(v -> {
            setSelectionModeActivated(true);
            setItemSelected(position, true);
            return true;
        });

        holder.container.setSelected(isItemSelected(position));
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

    public void setSelectionModeActivated(boolean modeActivated){
        selectionModeActivated = modeActivated;
        if(modeActivated){
            callback.selectionModeActivated();
        }
        else{
            Set<Integer> selectedPositionCopy = new HashSet<>(selectedPositions);
            for(Integer position: selectedPositionCopy){
                setItemSelected(position, false);
            }
            callback.selectionModeDeactivated();
        }
    }

    public boolean isSelectionModeActivated(){
        return selectionModeActivated;
    }

    @NonNull
    public List<Integer> getSelectedPositions(){
        List<Integer> list = new ArrayList<>(selectedPositions);
        Collections.sort(list);
        return list;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifySelectionsRemoved(){
        selectedPositions.clear();
        notifyDataSetChanged();
        setSelectionModeActivated(false);
    }

    private void setItemSelected(int position, boolean select){
        if(select){
            selectedPositions.add(position);
        }
        else{
            selectedPositions.remove(position);
            if(selectedPositions.isEmpty()){
                setSelectionModeActivated(false);
            }
        }

        notifyItemChanged(position);
    }

    private boolean isItemSelected(int position){
        return selectedPositions.contains(position);
    }

    private String getFormattedDate(ExamLog examLog){
        return examLog.getStartDateTime().format(dateFormatter);
    }

    private String getTimeTaken(ExamLog examLog){
        Duration duration = examLog.getActiveTime();
        return DurationUtil.getFormattedString(duration);
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

    public interface Callback{
        void selectionModeActivated();
        void selectionModeDeactivated();
    }
}
