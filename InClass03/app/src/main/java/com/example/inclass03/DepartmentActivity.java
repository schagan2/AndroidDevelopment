package com.example.inclass03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class DepartmentActivity extends AppCompatActivity {

    public static final String DEPT_KEY = "deptName";
    private static final String TAG = "DepartmentActivity";

    RadioGroup deptGroup;
    String selectedGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        setTitle("Department");
        deptGroup = findViewById(R.id.deptGroup);

        findViewById(R.id.selectDept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton) findViewById(deptGroup.getCheckedRadioButtonId());
                if(radioButton != null){
                    selectedGroup = radioButton.getText().toString();
                    Intent intent = new Intent();
                    intent.putExtra(DEPT_KEY, selectedGroup);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else{
                    Toast.makeText(DepartmentActivity.this, R.string.toast_dept, Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Cancelled selection");
                Toast.makeText(DepartmentActivity.this, R.string.toast_cancel, Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}