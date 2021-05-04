package com.example.hwork07;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListViewHolder> {

    FirebaseFirestore db;
    List<Photo> photos;
    Profile profile;
    Profile currentProfile;
    ProfileFragment fragment;
    Context context;
    PhotoAdapterListerner listener;

    public PhotoListAdapter(List<Photo> photoRefs, Profile profile, Profile currentProfile, Context context, ProfileFragment profileFragment) {
        this.profile = profile;
        this.photos = photoRefs;
        this.currentProfile = currentProfile;
        this.context = context;
        this.fragment = profileFragment;
        this.listener = (PhotoAdapterListerner) context;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public PhotoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_profile_post, parent, false);
        PhotoListViewHolder viewHolder = new PhotoListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoListViewHolder holder, int position) {
        Photo photo = photos.get(position);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child(profile.getProfileId()).child(photo.getPhotoRef().toString());

        Glide.with(fragment)
                .load(storageReference)
                .into(holder.photo);

        if(!profile.getEmail().equals(currentProfile.getEmail())){
            holder.deletePost.setEnabled(false);
            holder.deletePost.setVisibility(View.GONE);
        }

        if(photo.getLikedBy()!=null && photo.getLikedBy().contains(currentProfile.getProfileId())){
            holder.likeDislikePost.setImageResource(R.drawable.like);
        }
        else{
            holder.likeDislikePost.setImageResource(R.drawable.dislike);
        }

        holder.comments.setOnClickListener((view) -> {
            listener.goToCommentsFragment(profile, photo);
        });

        holder.deletePost.setOnClickListener((view) -> {
            photos.remove(photo);

            db.collection("profiles")
                    .document(profile.getProfileId())
                    .collection("photos")
                    .document(photo.getPhotoId())
                    .delete()
                    .addOnSuccessListener((aVoid) -> {
                            Toast.makeText(context, R.string.delete_success, Toast.LENGTH_SHORT).show();
                        });
        });

        holder.likeDislikePost.setOnClickListener((view) -> {
            List<String> likedList = photo.getLikedBy();

            if(likedList == null){
                likedList = new ArrayList<>();
            }
            if(likedList.contains(currentProfile.getProfileId())){
                likedList.remove(currentProfile.getProfileId());
            }else{
                likedList.add(currentProfile.getProfileId());
            }
            db.collection("profiles")
                    .document(profile.getProfileId())
                    .collection("photos")
                    .document(photo.getPhotoId())
                    .update("likedBy", likedList)
                    .addOnSuccessListener((aVoid) -> {
                            Toast.makeText(context, R.string.update_success, Toast.LENGTH_SHORT).show();
                        });
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
}

class PhotoListViewHolder extends RecyclerView.ViewHolder {
    ImageButton photo;
    ImageButton deletePost;
    ImageButton likeDislikePost;
    Button comments;

    public PhotoListViewHolder(@NonNull View itemView) {
        super(itemView);
        photo = itemView.findViewById(R.id.photoPost);
        deletePost = itemView.findViewById(R.id.deleteButton);
        likeDislikePost = itemView.findViewById(R.id.likeDislikeButton);
        comments = itemView.findViewById(R.id.comments);
    }
}

interface PhotoAdapterListerner{
    void goToCommentsFragment(Profile profile, Photo photo);
}