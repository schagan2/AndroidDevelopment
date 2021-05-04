/*
a. Assignment #. In Class 03
b. File Name: MainActivity.java
c. Full name of the student. Name: Aakanksha Chauhan, Sindhura Chaganti
 */
package com.example.inclass03;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE = 100;
    static final String PROFILE_KEY = "Profile";
    private static final String TAG = "MainActivity";
    Button selectDept;
    TextView deptName;
    EditText enteredName;
    EditText email;
    EditText id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deptName = findViewById(R.id.textDeptName);
        enteredName = findViewById(R.id.inputName);
        email = findViewById(R.id.email);
        id = findViewById(R.id.inputId);

        findViewById(R.id.select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectIntent = new Intent(MainActivity.this, DepartmentActivity.class);
                startActivityForResult(selectIntent, REQ_CODE);
            }
        });

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String profileName = enteredName.getText().toString();
                String profileEmail = email.getText().toString();
                String profileDept = deptName.getText().toString();
                String profileId = id.getText().toString();

                if (profileName.isEmpty()) {
                    Toast.makeText(MainActivity.this,R.string.toast_name, Toast.LENGTH_SHORT).show();
                } else if (profileEmail.isEmpty()) {
                    Toast.makeText(MainActivity.this,R.string.toast_email, Toast.LENGTH_SHORT).show();
                } else if (profileDept.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.toast_dept, Toast.LENGTH_SHORT).show();
                } else if (profileId.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.toast_id, Toast.LENGTH_SHORT).show();
                } else {
                    Profile profile = new Profile(profileName, profileEmail, profileId, profileDept);

                    Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                    profileIntent.putExtra(PROFILE_KEY, profile);
                    startActivity(profileIntent);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Log.d(TAG, "onActivityResult: Department selected");
            if(data != null && data.hasExtra(DepartmentActivity.DEPT_KEY)) {
                deptName.setText(data.getStringExtra(DepartmentActivity.DEPT_KEY));
            }
        }else if(resultCode == RESULT_CANCELED){
            Log.d(TAG, "onActivityResult: Cancelled operation");
            deptName.setText("");
        }
    }
}