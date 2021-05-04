package com.example.hw02;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DisplayTaskActivity extends AppCompatActivity {

    static final String DEL_TASK = "DEL_TASK";
    TextView taskName;
    TextView taskDate;
    TextView taskPriority;
    Task task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_task);

        setTitle("Display Task");
        taskName = findViewById(R.id.nameDisplayId);
        taskDate = findViewById(R.id.dateDisplayId);
        taskPriority = findViewById(R.id.priorityDisplayId);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(MainActivity.TASK)){
            task = (Task) intent.getSerializableExtra(MainActivity.TASK);

            taskName.setText(task.getTaskName());
            taskDate.setText(task.getStringDate());
            taskPriority.setText(task.getPriority());
        }

        findViewById(R.id.deleteId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder deleteConfirm = new AlertDialog.Builder(DisplayTaskActivity.this);
                deleteConfirm.setTitle(R.string.confirm)
                        .setMessage(R.string.message_display)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                 Intent intent = new Intent();
                                 intent.putExtra(DEL_TASK, task);
                                 setResult(RESULT_OK, intent);
                                 finish();
                                }

                        });
                deleteConfirm.create();
                deleteConfirm.show();
            }
        });

        findViewById(R.id.cancelId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
}