package com.example.hw06;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ForumsAdapter extends RecyclerView.Adapter<ForumsAdapter.ForumViewHolder> {

    AdapterListener context;
    ArrayList<Forum> forums;
    User user;
    FirebaseFirestore db;
    FirebaseAuth mauth;

    public ForumsAdapter(ArrayList<Forum> forum, User user, Context context){
        this.forums = forum;
        this.user = user;
        this.context = (AdapterListener) context;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_forum_item, parent, false);
        ForumViewHolder viewHolder = new ForumViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull ForumViewHolder holder, int position) {
        Forum forum = forums.get(position);

        holder.title.setText(forum.getTitle());
        holder.createdBy.setText(forum.getCreatedBy().getName());
        holder.description.setText(forum.getDescription().substring(0, Math.min(forum.getDescription().length(), 200)));
        Calendar cal = Calendar.getInstance();
        cal.setTime(forum.getCreatedAt());
        String am_pm = cal.get(Calendar.AM_PM) == 1 ? "PM" : "AM";
        holder.date.setText(cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.YEAR)+" "
                + cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+" "+am_pm);

        if(!mauth.getCurrentUser().getEmail().equals(forum.getCreatedBy().getEmail())){
            holder.delete.setEnabled(false);
            holder.delete.setVisibility(View.GONE);
        }

        if(forum.getLikedBy() != null){
            List<User> userList = forum.getLikedBy();
            holder.noOfLikes.setText(userList.size()+" Likes | ");
            if(userList.contains(user)){
                holder.like.setImageResource(R.drawable.like_favorite);
            }
        }

        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return this.forums.size();
    }

    class ForumViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView createdBy;
        TextView description;
        TextView noOfLikes;
        TextView date;
        ImageButton delete;
        ImageButton like;
        View rootView;
        int position;

        @SuppressLint("ResourceType")
        public ForumViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            title = itemView.findViewById(R.id.titleId);
            createdBy = itemView.findViewById(R.id.creatorId);
            description = itemView.findViewById(R.id.descriptionId);
            noOfLikes = itemView.findViewById(R.id.likesId);
            date = itemView.findViewById(R.id.dateId);
            delete = itemView.findViewById(R.id.deleteIconId);
            like = itemView.findViewById(R.id.likeIconId);

            delete.setOnClickListener((v) -> {
                db.collection("forums").document(forums.get(position).getForumId()).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    context.refreshForumsAdapter();
                                }else{
                                    createAlertMessage(task.getException().getMessage());
                                }
                            }
                        });
            });

            like.setOnClickListener((v) -> {
                List<User> userList = forums.get(position).getLikedBy();

                if(userList.contains(user)){
                    userList.remove(user);
                }else{
                    userList.add(user);
                }

                db.collection("forums").document(forums.get(position).getForumId())
                        .update("likedBy", userList)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                context.refreshForumsAdapter();
                            }
                        });
            });
            itemView.setOnClickListener((v) -> {
                context.goToForumDetail(forums.get(position));
            });
        }
    }

    public void createAlertMessage(String msg){
        AlertDialog.Builder alert = new AlertDialog.Builder((Context) context);
        alert.setTitle("Error").setMessage(msg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    interface AdapterListener{
        void refreshForumsAdapter();
        void goToForumDetail(Forum forum);
    }
}
