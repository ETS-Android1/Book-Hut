/*
 * Author: Asad Jawaid
 * */
package com.example.bookapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.w3c.dom.Text;

import java.util.HashMap;

public class RegisterPage extends AppCompatActivity {
    private TextInputLayout full_name, email, password;
    private Button registerButton;
    private TextView gotoLogin, go_to_mainActivity;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    //private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        full_name = (TextInputLayout) findViewById(R.id.FN_container);
        email = (TextInputLayout) findViewById(R.id.email_container);
        password = (TextInputLayout) findViewById(R.id.pass_container);
        registerButton = (Button) findViewById(R.id.sign_up_reg);
        gotoLogin = (TextView) findViewById(R.id.go_to_login);
        go_to_mainActivity = (TextView) findViewById(R.id.go_to_main);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(intent);
            }
        });

        go_to_mainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterPage.this, MainActivity.class));
            }
        });
    }

    private void registerUser() {
        // get all input
        String nameInput = full_name.getEditText().getText().toString().trim();
        String emailInput = email.getEditText().getText().toString().trim();
        String passwordInput = password.getEditText().getText().toString().trim();

        // verify inputs
        if(nameInput.isEmpty()) {
            full_name.setError("Please enter your full name!");
            full_name.requestFocus();
            return;
        }

        if(emailInput.isEmpty()) {
            email.setError("Please enter your email!");
            email.requestFocus();
            return;
        }

        // check if user has provided a valid email address (valid pattern: .com, ca, etc.)
        if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Invalid email! Please provide a valid email!");
            email.requestFocus();
            return;
        }

        if(passwordInput.isEmpty()) {
            password.setError("Please enter your password!");
            password.requestFocus();
            return;
        }
        // firebase requires password to be greater than or equal to 6 characters
        if(passwordInput.length() < 6) {
            password.setError("Password is too short! Try again.");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    HashMap<String, String> map = new HashMap<>(); // hash map
                    map.put("name", nameInput);
                    map.put("email", emailInput);

                    FirebaseDatabase rootNode = FirebaseDatabase.getInstance(); // root node
                    DatabaseReference reference = rootNode.getReference("Users"); // Users
                    String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    reference.child(uId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                // redirect to Login Page
                                startActivity(new Intent(RegisterPage.this, LoginPage.class));
                            }
                            else {
                                Toast.makeText(RegisterPage.this, "Error registering. Try again!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterPage.this, "Error registering. Try again!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}