package dev.sagar.examtimer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final EditText etQuestions = view.findViewById(R.id.et_questions);
        Button btnStart = view.findViewById(R.id.btn_start);

        btnStart.setOnClickListener(btnView -> {
            String txt = etQuestions.getText().toString();
            int val = Integer.parseInt(txt);
            etQuestions.setText("");
            Bundle basket = new Bundle();
            basket.putInt("q_count", val);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtras(basket);
            startActivity(intent);
        });
    }
}