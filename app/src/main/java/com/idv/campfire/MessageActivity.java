package com.idv.campfire;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    ImageView userPfp, sendButton;
    TextView userChattingWith;
    EditText edt_chatBox;
    ProgressBar progressMessages;

    ArrayList<Message> messages;
    String usernameOfRoommate, emailOfRoommate, chatRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        userPfp = findViewById(R.id.pfp_of_user);
        sendButton = findViewById(R.id.send_button);
        userChattingWith = findViewById(R.id.txt_display_name);
        edt_chatBox = findViewById(R.id.edt_chatBox);
        progressMessages = findViewById(R.id.progressMessages);

        messages = new ArrayList<>();


        usernameOfRoommate = getIntent().getStringExtra("usernameOfRoommate");
        emailOfRoommate = getIntent().getStringExtra("emailOfRoommate");
    }
}