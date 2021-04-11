/*
 * Author: Asad Jawaid
 * */
package com.example.bookapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {
    private TextInputLayout email, password;
    private Button loginBtn;
    private TextView forgotPass, signUp, go_to_mainActivity;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        email = (TextInputLayout) findViewById(R.id.email_container);
        password = (TextInputLayout) findViewById(R.id.pass_container);
        loginBtn = (Button) findViewById(R.id.login);
        forgotPass = (TextView) findViewById(R.id.reset_password);
        signUp = (TextView) findViewById(R.id.go_to_registar);
        go_to_mainActivity = (TextView) findViewById(R.id.go_to_main);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startApp();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this, ResetPassword.class));
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intent);
            }
        });

        go_to_mainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this, MainActivity.class));
            }
        });
    }

    private void startApp() {
        String emailInput = email.getEditText().getText().toString();
        String passwordInput = password.getEditText().getText().toString();

        // verify inputs
        if(emailInput.isEmpty()) {
            email.setError("Email error!");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Invalid email! Please provide a valid email!");
            email.requestFocus();
            return;
        }

        if(passwordInput.isEmpty()) {
            password.setError("Password error!");
            password.requestFocus();
            return;
        }

        if(passwordInput.length() < 6) {
            password.setError("Password is too short! Try again.");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    // start Book Main
                    startActivity(new Intent(LoginPage.this, BookMain.class));
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(LoginPage.this, "Failed to login. Try again!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

}