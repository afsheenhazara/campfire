package com.idv.campfire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText edtUsername, edtEmail, edtPassword;

    AppCompatButton btnSubmit;
    TextView txtSignLogInfo;  // text that displays below the button
    private boolean isSignUp = true; // variable to keep track of which page we are in

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtSignLogInfo = findViewById(R.id.txtSignUp);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSignUp) {
                    if (edtUsername.getText().toString().isEmpty() || edtEmail.getText().toString().isEmpty() ||
                            edtPassword.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Invalid input.", Toast.LENGTH_SHORT).show();
                    } else {
                        handleSignUp();
                    }
                }
                else {
                    if (edtEmail.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Invalid input.", Toast.LENGTH_SHORT).show();
                    } else {
                        handleLogIn();
                    }
                }
            }
        });

        txtSignLogInfo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (isSignUp) {
                    isSignUp = false;
                    btnSubmit.setText("LOG IN");
                    txtSignLogInfo.setText("Don't have an account? Sign up");
                    edtUsername.setVisibility(View.GONE);
                }
                else {
                    isSignUp = true;
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
                            Toast.makeText(MainActivity.this, "Signed up successfully.", Toast.LENGTH_SHORT).show();
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
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}