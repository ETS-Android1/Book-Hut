/*
 * Author: Asad Jawaid
 * */
package com.example.bookapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BookMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_main);

        // Bottom nav
        BottomNavigationView btnNav = findViewById(R.id.bottomNavigation);
        btnNav.setOnNavigationItemSelectedListener(navListener);

        // Set home fragment as the main fragment:
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout, new HomeFragment()).commit();

    }

    // if the user is logged into the application and they press the back button this method will prevent them from going to the Login page activity and will
    // close the application. At the same time, the user is still logged into the app, so if they close the app they will be directed back to this activity.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // or finish();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch(item.getItemId()) {
                case R.id.home_page:
                    selectedFragment = new HomeFragment();
                    break;

                case R.id.search_page:
                    selectedFragment = new SearchFragment();
                    break;

                case R.id.logout_page:
                    selectedFragment = new LogoutFragment();
                    break;
            }

            // set the selected fragment as the view
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_layout, selectedFragment).commit();

            return true;
        }
    };
}