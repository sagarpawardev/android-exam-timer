package dev.sagar.examtimer.history;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.sagar.examtimer.R;
import dev.sagar.examtimer.pojo.ExamLog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryListFragment extends Fragment {
    public HistoryListFragment() {
        // Required empty public constructor
    }

    public static HistoryListFragment newInstance() {
        return new HistoryListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView historyList = view.findViewById(R.id.history_list);
        historyList.setLayoutManager( new LinearLayoutManager(getActivity()));
        historyList.setAdapter( getAdapter(view) );
        historyList.addItemDecoration(getDividerDecoration(historyList));

        ((FloatingActionButton) view.findViewById(R.id.history_delete_fab)).hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.history_list_fragment, container, false);
    }

    private HistoryListAdapter getAdapter(View view){
        ExamLogService examLogService = ExamLogService.getInstance(requireActivity());
        List<ExamLog> examLogs = examLogService.getLogList();
        FloatingActionButton fabDelete = view.findViewById(R.id.history_delete_fab);
        HistoryListAdapter.Callback callback = new HistoryListAdapter.Callback() {
            @Override
            public void selectionModeActivated() {
                fabDelete.show();
            }

            @Override
            public void selectionModeDeactivated() {
                fabDelete.hide();
            }
        };

        HistoryListAdapter adapter = new HistoryListAdapter(requireActivity(), examLogs, callback);
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(adapter.isSelectionModeActivated()){
                    adapter.setSelectionModeActivated(false);
                    return;
                }

                requireActivity().finish();
            }
        });

        fabDelete.setOnClickListener(v -> {
            List<Integer> selectedPositions =  adapter.getSelectedPositions();
            List<ExamLog> selectedExamLogs = new ArrayList<>(selectedPositions.size());
            selectedPositions.forEach( pos -> {
                ExamLog examLog = examLogs.get(pos);
                selectedExamLogs.add(examLog);
            });

            examLogService.deleteAll(selectedExamLogs);

            selectedExamLogs.forEach(examLogs::remove);
            adapter.notifySelectionsRemoved();
        });
        return adapter;
    }

    private DividerItemDecoration getDividerDecoration(RecyclerView recyclerView){
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), VERTICAL);
        Activity activity = requireActivity();
        Drawable dividerDrawable = Objects.requireNonNull(ContextCompat.getDrawable(activity, R.drawable.history_divider));
        dividerItemDecoration.setDrawable(dividerDrawable);
        return dividerItemDecoration;
    }
}