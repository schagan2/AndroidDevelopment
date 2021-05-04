package com.example.inclass03;

import java.io.Serializable;

public class Profile implements Serializable {
    String name;
    String email;
    String id;
    String department;

    public Profile() {
    }

    public Profile(String name, String email, String id, String department) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.department = department;
    }
}
