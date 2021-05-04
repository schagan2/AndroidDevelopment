package com.example.midterm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ForumViewHolder> {

    ArrayList<DataServices.Forum> forums;
    DataServices.AuthResponse authResponse;
    DataServices.RequestException exception;
    ForumsFragment.ForumListener forumListener;
    Context context;

    public ForumAdapter(ArrayList<DataServices.Forum> forum, DataServices.AuthResponse authResponse,
                        ForumsFragment.ForumListener forumListener){
        this.forums = forum;
        this.authResponse = authResponse;
        this.forumListener = forumListener;
    }

    @NonNull
    @Override
    public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_forum_item, parent, false);
        ForumViewHolder viewHolder = new ForumViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ForumViewHolder holder, int position) {
        DataServices.Forum forum = forums.get(position);
        holder.title.setText(forum.getTitle());
        holder.createdBy.setText(forum.getCreatedBy().getName());
        holder.description.setText(forum.getDescription().substring(0, Math.min(forum.getDescription().length(), 200)));
        holder.noOfLikes.setText(forum.getLikedBy().size()+" Likes | ");
        Calendar cal = Calendar.getInstance();
        cal.setTime(forum.getCreatedAt());
        String am_pm = cal.get(Calendar.AM_PM) == 1 ? "PM" : "AM";
        holder.date.setText(cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.YEAR)+" "
                + cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+" "+am_pm);

        if(forum.getLikedBy().contains(authResponse.account)){
            holder.like.setImageResource(R.drawable.like_favorite);
        }else{
            holder.like.setImageResource(R.drawable.like_not_favorite);
        }

        if(!forum.getCreatedBy().equals(this.authResponse.account)){
            holder.delete.setVisibility(View.GONE);
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
                Log.d("TAG", "ForumViewHolder: in delete listener");
                new ForumAdapterAsyncTask(authResponse.token, forums.get(position).getForumId()).execute();
            });

            like.setOnClickListener((v) -> {
                if(like.getId() == R.drawable.like_favorite){
                    like.setImageResource(R.drawable.like_not_favorite);
                    new LikeAsyncTask(authResponse.token, forums.get(position).getForumId(), false).execute();
                }else{
                    like.setImageResource(R.drawable.like_favorite);
                    new LikeAsyncTask(authResponse.token, forums.get(position).getForumId(), true).execute();
                }
            });
            itemView.setOnClickListener((v) -> {
                    forumListener.setForumFragment(forums.get(position));
                });
        }
    }
    
    class ForumAdapterAsyncTask extends AsyncTask<String, String, Object>{
        String token;
        long forumId;

        ForumAdapterAsyncTask(String token, long forumId){
            this.token = token;
            this.forumId = forumId;
        }

        @Override
        protected Object doInBackground(String[] objects) {
            try {
                DataServices.deleteForum(token, forumId);
            } catch (DataServices.RequestException e) {
                exception = e;
            }
            return exception;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(o != null && o instanceof DataServices.RequestException){
                Toast.makeText(getContext(), ((DataServices.RequestException) o).getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                Log.d("TAG", "onPostExecute: in postExecute");
                forumListener.setForumsFragmentAfterDelete();
            }
        }
    }

    class LikeAsyncTask extends AsyncTask<String, String, Object>{
        String token;
        long forumId;
        boolean isLike;

        public LikeAsyncTask(String token, long forumId, boolean isTrue){
            this.forumId = forumId;
            this.token = token;
            this.isLike = isTrue;
        }

        @Override
        protected Object doInBackground(String[] objects) {
            try {
                if (isLike) {
                    DataServices.likeForum(token, forumId);
                } else {
                    DataServices.unLikeForum(token, forumId);
                }
            }catch (DataServices.RequestException e){
                exception = e;
            }
            return exception;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(o!=null && o instanceof DataServices.RequestException){
                Toast.makeText(getContext(), ((DataServices.RequestException) o).getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                forumListener.setForumsFragmentAfterDelete();
            }
        }
    }

    private Context getContext() {
        return getContext();
    }
}
