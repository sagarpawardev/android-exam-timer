package dev.sagar.examtimer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class HomeActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FragmentManager fragmentMgr = getSupportFragmentManager();
        FragmentTransaction fragmentTxn = fragmentMgr.beginTransaction();
        Fragment homeFragment = new HomeFragment();
        fragmentTxn.replace(R.id.fragment_container, homeFragment);
        fragmentTxn.commit();
    }

}
