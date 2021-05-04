package com.example.hw06;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    Forum forum;
    FirebaseFirestore db;
    ArrayList<Comment> comments;
    User user;
    ForumFragment forumFragment;
    String forumId;

    public CommentAdapter(Forum forum, User user, ArrayList<Comment> comments
            , ForumFragment forumFragment) {
        this.forum = forum;
        this.user = user;
        this.comments = comments;
        this.forumId = forum.getForumId();
        this.forumFragment = forumFragment;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment, parent, false);
        CommentViewHolder viewHolder = new CommentViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.position = position;
        Comment comment = comments.get(position);
        holder.name.setText(comment.createdBy.getName());
        holder.comment.setText(comment.text);
        Calendar cal = Calendar.getInstance();
        cal.setTime(comment.getCreatedAt());
        String am_pm = cal.get(Calendar.AM_PM) == 1 ? "PM" : "AM";
        holder.date.setText(cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.YEAR)+" "
                + cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+" "+am_pm);

        if(!this.user.equals(comment.createdBy)){
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return this.comments.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView comment;
        TextView date;
        ImageButton deleteButton;
        int position;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.commentCreator);
            comment = itemView.findViewById(R.id.comment);
            date = itemView.findViewById(R.id.commentDate);
            deleteButton = itemView.findViewById(R.id.deleteComment);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TAG", "onClick: "+position+" ,"+ comments.get(position));
                    db.collection("comments").document(comments.get(position).commentId).delete();
                    //.addOnSuccessListener(forumFragment.refreshComments());
                }
            });
        }

    }
}
