package com.example.hwork07;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapterViewHolder> {

    FirebaseAuth mauth;
    FirebaseFirestore db;
    List<Comment> commentList;
    Profile profile;
    Photo photo;
    Context context;

    public CommentAdapter(ArrayList<Comment> commentsList, Profile profile, Photo photo, Context context) {
        this.commentList = commentsList;
        this.profile = profile;
        this.photo = photo;
        this.context = context;
        this.mauth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public CommentAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment, parent, false);
        CommentAdapterViewHolder viewHolder = new CommentAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapterViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        holder.userName.setText(comment.getCommentUser());

        Log.d("TAG", "onBindViewHolder: "+ comment.getUid()+", "+mauth.getCurrentUser().getUid());
        if(!comment.getUid().trim().equals(mauth.getCurrentUser().getUid())){
            holder.deleteComment.setEnabled(false);
            holder.deleteComment.setVisibility(View.GONE);
        }

        holder.comment.setText(comment.getText());

        holder.deleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentList.remove(comment);

                db.collection("profiles")
                        .document(profile.getProfileId())
                        .collection("photos")
                        .document(photo.getPhotoId())
                        .collection("comments")
                        .document(comment.getCommentId())
                        .delete()
                        .addOnSuccessListener((avoid) -> {
                            Toast.makeText(context, R.string.delete_success, Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}

class CommentAdapterViewHolder extends RecyclerView.ViewHolder {

    TextView userName;
    TextView comment;
    ImageButton deleteComment;

    public CommentAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        userName = itemView.findViewById(R.id.commentUserName);
        comment = itemView.findViewById(R.id.commentText);
        deleteComment = itemView.findViewById(R.id.deleteComment);
    }
}
