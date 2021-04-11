/*
 * Author: Asad Jawaid
 * */
package com.example.bookapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }

    public void startLoginPage(View view) {
        Intent intent = new Intent(MainActivity.this, LoginPage.class);
        startActivity(intent);
    }

    public void startRegisterPage(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterPage.class);
        startActivity(intent);
    }

    // Here I check if the user is already logged into the application. If they are then they directed to the BookMain activity. Otherwise if they are not
    // logged into the application then this activity (MainActivity) is displayed!
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser mUser = mAuth.getCurrentUser();

        // if the user has logged in before then continue to BookMain otherwise stay on this page
        if(mUser != null) {
            // user is logged in
            startActivity(new Intent(MainActivity.this, BookMain.class));
           // finish();
        }
    }
}