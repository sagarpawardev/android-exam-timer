package dev.sagar.examtimer.fragment;

import static dev.sagar.examtimer.Constants.PROP_HOME_MAX_QUESTION;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import dev.sagar.examtimer.R;
import dev.sagar.examtimer.activity.ExamActivity;
import dev.sagar.examtimer.utils.ConfigReader;

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
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final EditText etQuestions = view.findViewById(R.id.et_questions);
        final Button btnStart = view.findViewById(R.id.btn_start);
        etQuestions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                int maxSupportedQuestion = ConfigReader.getInstance(getActivity()).getInt(PROP_HOME_MAX_QUESTION, 400);
                if(s.length() > 3) {
                    Toast.makeText(getActivity(), "Max Supported Questions: " + maxSupportedQuestion, Toast.LENGTH_SHORT).show();
                    btnStart.setEnabled( false );
                    return;
                }

                btnStart.setEnabled( StringUtils.isNumeric(s) );
            }
        });

        btnStart.setOnClickListener(btnView -> {
            int maxSupportedQuestion = ConfigReader.getInstance(getActivity()).getInt(PROP_HOME_MAX_QUESTION, 400);
            String txt = etQuestions.getText().toString();
            int val = Integer.parseInt(txt);
            etQuestions.setText("");

            if(maxSupportedQuestion < val){
                Toast.makeText(getActivity(), "Max Supported Questions: "+maxSupportedQuestion, Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle basket = new Bundle();
            basket.putInt("q_count", val);
            Intent intent = new Intent(getActivity(), ExamActivity.class);
            intent.putExtras(basket);
            startActivity(intent);
        });
    }
}