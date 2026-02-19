package com.idv.campfire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    ImageView userPfp, sendButton;
    TextView userChattingWith;
    EditText edt_chatBox;
    ProgressBar progressMessages;

    ArrayList<Message> messages;
    String usernameOfRoommate, emailOfRoommate, chatRoomId;
    RecyclerView recyclerMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        userPfp = findViewById(R.id.pfp_of_user);
        sendButton = findViewById(R.id.send_button);
        userChattingWith = findViewById(R.id.txt_display_name);
        edt_chatBox = findViewById(R.id.edt_chatBox);
        progressMessages = findViewById(R.id.progressMessages);
        recyclerMessage = findViewById(R.id.recyclerMessage);

        messages = new ArrayList<>();


        usernameOfRoommate = getIntent().getStringExtra("usernameOfRoommate");
        emailOfRoommate = getIntent().getStringExtra("emailOfRoommate");

        userChattingWith.setText(usernameOfRoommate);

        setUpChatRoom();
    }

    private void setUpChatRoom() {
        FirebaseDatabase.getInstance().getReference("user/"+ FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String myUsername = snapshot.getValue(User.class).getUsername();
                if (usernameOfRoommate.compareTo(myUsername) > 0) {
                    chatRoomId = myUsername + usernameOfRoommate;
                }
                else if (usernameOfRoommate.compareTo(myUsername) == 0) {
                    chatRoomId = myUsername + usernameOfRoommate;
                }
                else {
                    chatRoomId = usernameOfRoommate + myUsername;
                }
                attachMessageListener(chatRoomId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void attachMessageListener(String chatRoomId) {
        FirebaseDatabase.getInstance().getReference("messages/" + chatRoomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    messages.add(dataSnapshot.getValue(Message.class));
                }
                recyclerMessage.scrollToPosition(messages.size() - 1);
                recyclerMessage.setVisibility(View.VISIBLE);
                progressMessages.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}