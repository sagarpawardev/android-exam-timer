package dev.sagar.examtimer;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final HomeFragment homeFragment = new HomeFragment();
        replaceFragment(homeFragment);

        BottomNavigationView btmNav = findViewById(R.id.home_bottom_nav);
        btmNav.setOnNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.menu_home){
                replaceFragment(homeFragment);
            }
            else if(item.getItemId() == R.id.menu_history){
                replaceFragment(homeFragment); //TODO: Put history fragment here
                Toast.makeText(HomeActivity.this, "Here Clicked", Toast.LENGTH_SHORT).show();
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
