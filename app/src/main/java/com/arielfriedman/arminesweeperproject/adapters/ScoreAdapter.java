package com.arielfriedman.arminesweeperproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arielfriedman.arminesweeperproject.R;
import com.arielfriedman.arminesweeperproject.model.User;

import java.util.ArrayList;
import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {

    private final List<User> userScoreList;

    public ScoreAdapter() {
        userScoreList = new ArrayList<>();
    }

    public ScoreAdapter(ArrayList users) {
        userScoreList = users;
    }

    @NonNull
    @Override
    public ScoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_score, parent, false);
        return new ScoreAdapter.ViewHolder(view);
    }

   @Override
    public void onBindViewHolder(@NonNull ScoreAdapter.ViewHolder holder, int position) {
        User user = userScoreList.get(position);
        if (user == null) return;

        holder.tvName.setText(user.getFullName());
        holder.tvScore.setText("סיבוב שיא: " + user.getScore());

        // Set initials
        String initials = "";
        if (user.getFname() != null && !user.getFname().isEmpty()) {
            initials += user.getFname().charAt(0);
        }
        if (user.getLname() != null && !user.getLname().isEmpty()) {
            initials += user.getLname().charAt(0);
        }
        holder.tvInitials.setText(initials.toUpperCase());
    }

    @Override
    public int getItemCount() {
        return userScoreList.size();
    }

    public void setScoreList(List<User> users) {
        userScoreList.clear();
        userScoreList.addAll(users);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvInitials, tvScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_item_user_name);
            tvInitials = itemView.findViewById(R.id.tv_user_initials);
            tvScore = itemView.findViewById(R.id.tv_item_user_score);
        }
    }

}
