package com.example.hwork07;

import java.io.Serializable;
import java.util.List;

public class Profile implements Serializable {
    String profileId;
    String name;
    String email;
    String password;
    List<Photo> photoRefs;

    public Profile(String profileId, String name, String email, String password, List<Photo> photoRefs) {
        this.profileId = profileId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.photoRefs = photoRefs;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Photo> getPhotoRefs() {
        return photoRefs;
    }

    public void setPhotoRefs(List<Photo> photoRefs) {
        this.photoRefs = photoRefs;
    }

    @Override
    public String toString() {
        return name;
    }
}
