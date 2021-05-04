package com.example.hwork07;

import android.net.Uri;

import java.io.Serializable;
import java.util.List;

public class Photo implements Serializable {
    String photoId;
    List<Comment> commentList;
    List<String> likedBy;
    Uri photoRef;
    String title;

    public Photo(String photoId, List<Comment> commentList, List<String> likedBy, Uri photoRef, String title) {
        this.photoId = photoId;
        this.commentList = commentList;
        this.likedBy = likedBy;
        this.photoRef = photoRef;
        this.title = title;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public List<String> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(List<String> likedBy) {
        this.likedBy = likedBy;
    }

    public Uri getPhotoRef() {
        return photoRef;
    }

    public void setPhotoRef(Uri photoRef) {
        this.photoRef = photoRef;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "photoId='" + photoId + '\'' +
                ", commentList=" + commentList +
                ", likedBy=" + likedBy +
                ", photoRef='" + photoRef + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
