package com.idv.campfire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

        txtSignLogInfo.setOnClickListener(new View.OnClickListener() {
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
}