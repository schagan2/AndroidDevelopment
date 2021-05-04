package com.example.hwork07;

public class Comment {
    String commentId;
    String uid;
    String commentUser;
    String text;

    public Comment(String commentId, String uid, String name, String text) {
        this.commentId = commentId;
        this.uid = uid;
        this.commentUser = name;
        this.text = text;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(String commentUser) {
        this.commentUser = commentUser;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "uid='" + uid + '\'' +
                ", commentUser=" + commentUser +
                ", text='" + text + '\'' +
                '}';
    }
}
