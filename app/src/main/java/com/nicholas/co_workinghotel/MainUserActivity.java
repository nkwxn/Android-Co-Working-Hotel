package com.nicholas.co_workinghotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainUserActivity extends AppCompatActivity {
    ActionBar bar;
    BottomNavigationView bottomNav;
    private long UserID;

    private void initUI() {
        bar = getSupportActionBar();
        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        UserID = Long.parseLong(getIntent().getStringExtra("user_id"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        initUI();
        bar.setTitle(R.string.home);
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selected = null;
                    switch (item.getItemId()) {
                        case R.id.menu_home:
                            selected = new HomeFragment();
                            break;
                        case R.id.menu_history:
                            selected = new HistoryFragment();
                            break;
                        case R.id.menu_profile:
                            selected = new ProfileFragment();
                            break;
                    }
                    bar.setTitle(item.getTitle());
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, selected).commit();
                    return true;
                }
            };

    public long getUserID() {
        return UserID;
    }

    public void setUserID(long userID) {
        UserID = userID;
    }
}