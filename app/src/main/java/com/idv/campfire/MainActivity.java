package com.idv.campfire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText edtUsername, edtEmail, edtPassword;

    AppCompatButton btnSubmit;
    TextView txtSignLogInfo;  // text that displays below the button
    private boolean isSignUp = true; // variable to keep track of which page we are in
                                     // default page is sign in page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtSignLogInfo = findViewById(R.id.txtSignUp);

        // if user is already logged in, go directly to friends activity
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, FriendsActivity.class));
            finish(); // clears all activities before friends, so pressing back button would exit the app
                      // instead of going back to sign in page even tho we're already signed in
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSignUp) {  // if the page is a sign up page, what happens if we click submit ?
                    if (edtUsername.getText().toString().isEmpty() || edtEmail.getText().toString().isEmpty() ||
                            edtPassword.getText().toString().isEmpty()) {   //validating and checking credentials
                        Toast.makeText(MainActivity.this, "Invalid input.", Toast.LENGTH_SHORT).show();
                    } else {
                        handleSignUp();
                    }
                }
                else { // if the page is a login page, what happens if we click submit?
                    if (edtEmail.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Invalid input.", Toast.LENGTH_SHORT).show();
                    } else {
                        handleLogIn();
                    }
                }
            }
        });

        txtSignLogInfo.setOnClickListener(new View.OnClickListener() { // writing code for the dynamic submit button
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (isSignUp) {    // if the page is a sign up page,turn it into a log in page
                    isSignUp = false;  // now its a login page
                    btnSubmit.setText("LOG IN");
                    txtSignLogInfo.setText("Don't have an account? Sign up");
                    edtUsername.setVisibility(View.GONE);
                }
                else {            // if the page is a login page, turn it into a sign up page
                    isSignUp = true;        // now its a sign up page
                    btnSubmit.setText("SIGN UP");
                    txtSignLogInfo.setText("Already have an account? Log in");
                    edtUsername.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void handleSignUp(){

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // adding user object to database
                            // "user/" + is used to create new user instead of rewriting over the same object
                            FirebaseDatabase.getInstance().getReference("user/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(new User(edtUsername.getText().toString(), edtEmail.getText().toString(), ""));

                            Toast.makeText(MainActivity.this, "Signed up successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, FriendsActivity.class));
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void handleLogIn() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, FriendsActivity.class));
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}