package com.example.hw06;

import java.util.Date;

public class Comment {
    String text;
    User createdBy;
    Date createdAt;
    String commentId;

    public Comment(String text, User createdBy, Date createdAt, String commentId) {
        this.text = text;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.commentId = commentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "text='" + text + '\'' +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                ", commentId='" + commentId + '\'' +
                '}';
    }
}
