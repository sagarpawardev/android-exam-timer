package dev.sagar.examtimer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dev.sagar.examtimer.history.HistoryListFragment;


public class HomeActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        final HomeFragment homeFragment = HomeFragment.newInstance();
        final HistoryListFragment historyListFragment = HistoryListFragment.newInstance();
        replaceFragment(homeFragment);

        BottomNavigationView btmNav = findViewById(R.id.home_bottom_nav);
        btmNav.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.menu_home){
                replaceFragment(homeFragment);
            }
            else if(item.getItemId() == R.id.menu_history){
                replaceFragment(historyListFragment);
            }
            return true;
        });
    }

    private void replaceFragment(@NonNull Fragment fragment){
        getSupportFragmentManager().
                beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

}
