package dev.sagar.examtimer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class HomeActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final EditText etQuestions = findViewById(R.id.et_questions);
        Button btnStart = findViewById(R.id.btn_start);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt = etQuestions.getText().toString();
                int val = Integer.parseInt(txt);
                etQuestions.setText("");
                Bundle basket = new Bundle();
                basket.putInt("q_count", val);
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.putExtras(basket);
                startActivity(intent);
            }
        });

    }

}
