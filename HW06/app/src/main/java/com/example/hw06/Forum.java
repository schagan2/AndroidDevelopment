package com.example.hw06;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class Forum implements Serializable {
    private String forumId;
    private String title;
    private User createdBy;
    private Date createdAt;
    private String description;
    private List<User> likedBy;

    public Forum(String id, String title, User createdBy, Date createdAt, String description, List<User> users) {
        this.forumId = id;
        this.title = title;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.description = description;
        this.likedBy = users;
        this.forumId = forumId;
    }

    public String getForumId() {
        return forumId;
    }

    public String getTitle() {
        return title;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
        return description;
    }

    public List<User> getLikedBy() {
        return likedBy;
    }

    @Override
    public String toString() {
        return "Forum{" +
                "title='" + title + '\'' +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                ", description='" + description + '\'' +
                ", likedBy=" + likedBy +
                ", forumId=" + forumId +
                '}';
    }
}