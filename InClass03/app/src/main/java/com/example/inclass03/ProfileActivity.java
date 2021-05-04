package com.example.inclass03;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    TextView name;
    TextView email;
    TextView id;
    TextView department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");

        name = findViewById(R.id.profileNameDisplay);
        email = findViewById(R.id.profileEmailDisplay);
        id = findViewById(R.id.profileIDDisplay);
        department = findViewById(R.id.profileDeptDisplay);

        if(getIntent() != null && getIntent().getExtras() != null && getIntent().hasExtra(MainActivity.PROFILE_KEY)){
            Profile profile = (Profile) getIntent().getSerializableExtra(MainActivity.PROFILE_KEY);
            name.setText(profile.name);
            email.setText(profile.email);
            id.setText(String.valueOf(profile.id));
            department.setText(profile.department);
        }
    }
}