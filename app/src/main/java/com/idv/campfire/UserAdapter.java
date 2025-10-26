package com.idv.campfire;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    ArrayList<User> users;
    Context context;
    OnUserClickedListener onUserClickedListener;

    interface OnUserClickedListener {
        void onUserClicked(int position);
    }

    public UserAdapter(ArrayList<User> users, Context context, OnUserClickedListener onUserClickedListener) {
        this.users = users;
        this.context = context;
        this.onUserClickedListener = onUserClickedListener;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_holder, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.txtUsername.setText(users.get(position).getUsername());
        // set image by using Glide
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserHolder extends RecyclerView.ViewHolder {
        TextView txtUsername;
        ImageView img_pfp;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txt_display_name);
            img_pfp = itemView.findViewById(R.id.img_pfp);
        }
    }
}
