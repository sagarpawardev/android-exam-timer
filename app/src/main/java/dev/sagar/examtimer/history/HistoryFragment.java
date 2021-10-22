package dev.sagar.examtimer.history;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import dev.sagar.examtimer.R;
import dev.sagar.examtimer.pojo.ExamLog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView historyList = view.findViewById(R.id.history_list);
        historyList.setLayoutManager( new LinearLayoutManager(getActivity()));
        historyList.setAdapter( getAdapter() );
        historyList.addItemDecoration(getDividerDecoration(historyList));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    private HistoryListAdapter getAdapter(){
        List<ExamLog> examLogs = ExamLogService.getInstance().getLogList();
        return new HistoryListAdapter(examLogs);
    }

    private DividerItemDecoration getDividerDecoration(RecyclerView recyclerView){
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), VERTICAL);
        Activity activity = Objects.requireNonNull(getActivity());
        Drawable dividerDrawable = Objects.requireNonNull(ContextCompat.getDrawable(activity, R.drawable.history_divider));
        dividerItemDecoration.setDrawable(dividerDrawable);
        return dividerItemDecoration;
    }
}