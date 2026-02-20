package com.idv.campfire;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {
    private final ArrayList<Message> messages;
    private final String senderImg;
    private final String receiverImg;
    private final Context context;

    public MessageAdapter(ArrayList<Message> messages, String senderImg, String receiverImg, Context context) {
        this.messages = messages;
        this.senderImg = senderImg;
        this.receiverImg = receiverImg;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_background, parent, false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageHolder holder, int position) {
        holder.message_content.setText(messages.get(position).getContent());
        ConstraintLayout cl = holder.constraintLayout;
        if (messages.get(position).getSender().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail())) {
            ConstraintSet cs = new ConstraintSet();
            cs.clone(cl);
            cs.clear(R.id.card_img, ConstraintSet.LEFT);
            cs.clear(R.id.card_message, ConstraintSet.LEFT);
            cs.connect(R.id.card_img, ConstraintSet.RIGHT, R.id.message_cl, ConstraintSet.RIGHT);
            cs.connect(R.id.card_message, ConstraintSet.RIGHT, R.id.card_img, ConstraintSet.LEFT);
            cs.applyTo(cl);

            holder.message_content.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

            Glide.with(context).load(senderImg)
                    .error(R.drawable.ic_profile)
                    .placeholder(R.drawable.ic_profile)
                    .into(holder.pfp_img);
        }

        else {
            ConstraintSet cs = new ConstraintSet();
            cs.clone(cl);
            cs.clear(R.id.card_img, ConstraintSet.LEFT);
            cs.clear(R.id.card_message, ConstraintSet.LEFT);
            cs.connect(R.id.card_img, ConstraintSet.LEFT, R.id.message_cl, ConstraintSet.LEFT);
            cs.connect(R.id.card_message, ConstraintSet.LEFT, R.id.card_img, ConstraintSet.RIGHT);
            cs.applyTo(cl);

            holder.message_content.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

            Glide.with(context).load(receiverImg)
                    .error(R.drawable.ic_profile)
                    .placeholder(R.drawable.ic_profile)
                    .into(holder.pfp_img);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageHolder extends RecyclerView.ViewHolder {
        CardView pfp_card, message_card;
        ImageView pfp_img;
        TextView message_content;
        ConstraintLayout constraintLayout;


        public MessageHolder(@NonNull View itemView) {
            super(itemView);

            pfp_card = itemView.findViewById(R.id.card_img);
            message_card = itemView.findViewById(R.id.card_message);
            pfp_img = itemView.findViewById(R.id.user_img);
            message_content = itemView.findViewById(R.id.txt_message);
            constraintLayout = itemView.findViewById(R.id.message_cl);

        }
    }
}
