package com.idv.campfire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    MessageAdapter messageAdapter;

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

        // on click listener for send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("messages/"+chatRoomId).push()
                        .setValue(new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                emailOfRoommate, edt_chatBox.getText().toString()));
                edt_chatBox.setText("");
            }
        });

        messageAdapter = new MessageAdapter(messages, getIntent().getStringExtra("myImg"),
                getIntent().getStringExtra("imgOfRoommate"), MessageActivity.this);
        recyclerMessage.setLayoutManager(new LinearLayoutManager(this));
        recyclerMessage.setAdapter(messageAdapter);

        Glide.with(MessageActivity.this)
                .load(getIntent().getStringExtra("imgOfRoommate"))
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(userPfp);

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
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    messages.add(dataSnapshot.getValue(Message.class));
                }

                messageAdapter.notifyDataSetChanged();

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