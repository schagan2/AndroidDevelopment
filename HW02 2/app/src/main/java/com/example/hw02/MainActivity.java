package com.example.hw02;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
/*
 * Assignment: Homework-2
 * File Name: MainActivity.java[HW02.app]
 * Group: C12
 * Names: Aakanksha Chauhan, Sindhura Chaganti
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    static final String TASK = "Task_Object";
    static final int REQ_CODE = 100;
    static final int REQ_DEL_CODE = 111;
    private static final String TASK_LIST = "Task_list";
    ArrayList<Task> toDoList = new ArrayList();
    TextView taskView;
    TextView upcomingTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("To Do List");

        taskView = findViewById(R.id.tasks);
        displayTaskMessage(taskView);

        upcomingTasks = findViewById(R.id.upcomingTasks);
        displayUpcomingTasks(upcomingTasks);

        findViewById(R.id.viewTaskButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                if (toDoList.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.empty_list, Toast.LENGTH_SHORT).show();
                } else {
                    String[] taskNames = new String[toDoList.size()];
                    for (int i = 0; i < toDoList.size(); i++) {
                        taskNames[i] = toDoList.get(i).getTaskName();
                    }

                    alert.setItems(taskNames, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, DisplayTaskActivity.class);
                            intent.putExtra(TASK, (Task) toDoList.get(which));
                            startActivityForResult(intent, REQ_DEL_CODE);
                        }
                    });
                    alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    alert.setTitle("Select Task");
                    alert.create();
                    alert.show();
                }
            }
        });

        findViewById(R.id.createTaskButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCreate = new Intent(MainActivity.this, CreateTaskActivity.class);
                startActivityForResult(intentCreate, REQ_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQ_CODE){
                if(data != null && data.hasExtra(CreateTaskActivity.TASK_OBJECT)){
                    Log.d(TAG, "onActivityResult: "+ (Task) data.getSerializableExtra(CreateTaskActivity.TASK_OBJECT) );
                    toDoList.add((Task) data.getSerializableExtra(CreateTaskActivity.TASK_OBJECT));
                    displayTaskMessage(taskView);
                    displayUpcomingTasks(upcomingTasks);
                }
            }else if(requestCode  == REQ_DEL_CODE){
                if(data != null && data.hasExtra(DisplayTaskActivity.DEL_TASK)){
                    Task taskObj = (Task) data.getSerializableExtra(DisplayTaskActivity.DEL_TASK);
                    if(toDoList.contains(taskObj)){
                        toDoList.remove(taskObj);
                    }
                    displayTaskMessage(taskView);
                    displayUpcomingTasks(upcomingTasks);
                }
            }

        }
    }

    /* This method is to count the number of tasks and display in the
     * TextView of the app.
     */
    private void displayTaskMessage(TextView taskView) {
        Resources res = getResources();
        String viewMsg = String.format(res.getString(R.string.tasks_view), toDoList.size());
        taskView.setText(viewMsg);
    }

    /* This method is to display the upcoming tasks and display in the
     * TextView of the app.
     */
    private void displayUpcomingTasks(TextView upcomingTasks) {
        final long now = System.currentTimeMillis();
        if(toDoList.size() != 0){
            Collections.sort(toDoList);

            Task taskTop = toDoList.get(0);

            upcomingTasks.setText("Upcoming Tasks"+"\n"+taskTop.getTaskName()+"\n"+ taskTop.getStringDate() +"      "+ taskTop.getPriority());
        }else{
            upcomingTasks.setText(R.string.none);
        }
    }

}