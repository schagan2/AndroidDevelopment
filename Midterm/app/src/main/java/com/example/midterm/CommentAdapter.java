package com.example.midterm;

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

import java.util.ArrayList;
import java.util.Calendar;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    DataServices.Forum forum;
    DataServices.RequestException exception;
    ArrayList<DataServices.Comment> comments;
    DataServices.AuthResponse auth;
    ForumFragment.ForumFragmentListener forumFragment;
    long forumId;
    Context context;

    public CommentAdapter(DataServices.Forum forum, DataServices.AuthResponse auth, ArrayList<DataServices.Comment> comments
            , ForumFragment.ForumFragmentListener forumFragment, Context context) {
        this.forum = forum;
        this.auth = auth;
        this.comments = comments;
        this.forumId = forum.getForumId();
        this.forumFragment = forumFragment;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment, parent, false);
        CommentViewHolder viewHolder = new CommentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.position = position;
        DataServices.Comment comment = comments.get(position);
        holder.name.setText(comment.createdBy.getName());
        holder.comment.setText(comment.text);
        Calendar cal = Calendar.getInstance();
        cal.setTime(comment.getCreatedAt());
        String am_pm = cal.get(Calendar.AM_PM) == 1 ? "PM" : "AM";
        holder.date.setText(cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.YEAR)+" "
        + cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+" "+am_pm);

        if(!this.auth.account.equals(comment.createdBy)){
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
                    new DeleteAsyncTask(auth.token, forumId, comments.get(position).commentId).execute();
                }
            });
        }

    }

    class DeleteAsyncTask extends AsyncTask<String, String, Object>{
        String token;
        long forumId;
        long commentId;

        public DeleteAsyncTask(String token, long forumId, long commentId){
            this.token = token;
            this.forumId = forumId;
            this.commentId = commentId;
        }

        @Override
        protected Object doInBackground(String[] objects) {
            try {
                DataServices.deleteComment(token, forumId, commentId);
            } catch (DataServices.RequestException e) {
                exception = e;
            }
            return exception;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(o!=null && o instanceof DataServices.RequestException){
                Toast.makeText(context, ((DataServices.RequestException) o).getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                forumFragment.setForumFragmentAfterDelete(forum);
            }
        }
    }
}
