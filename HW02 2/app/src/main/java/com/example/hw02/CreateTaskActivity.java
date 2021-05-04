package com.example.hw02;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateTaskActivity extends AppCompatActivity {

    public static final String TASK_OBJECT = "TASK_OBJECT";
    EditText nameEditId;
    TextView dateText;
    RadioGroup priorityLevel;
    DatePickerDialog picker;
    String taskName;
    String priority;
    Date date = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        setTitle("Create Task");

        nameEditId = findViewById(R.id.nameEditId);

        dateText = findViewById(R.id.dateDisplayCreateId);

        priorityLevel = findViewById(R.id.radioCreateId);

        findViewById(R.id.setdateID).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(CreateTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                TextView dateView = findViewById(R.id.dateDisplayCreateId);
                                String d = monthOfYear+1+"/"+dayOfMonth+"/"+year;
                                date = new Date(d);
                                dateView.setText(d);
                            }
                        }, year, month, day);
                picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                picker.show();
            }
        });

        findViewById(R.id.submitCreateId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                taskName = nameEditId.getText().toString();

                if(taskName.equals("")){
                    Toast.makeText(CreateTaskActivity.this, R.string.task_name_warning, Toast.LENGTH_SHORT).show();
                } else if (priorityLevel.getCheckedRadioButtonId() == -1){
                    Toast.makeText(CreateTaskActivity.this, R.string.task_priority_warning, Toast.LENGTH_SHORT).show();
                } else if(date == null){
                    Toast.makeText(CreateTaskActivity.this, R.string.task_date_warning, Toast.LENGTH_SHORT).show();
                } else { // all is ok ..
                    RadioButton button = ((RadioButton) findViewById(priorityLevel.getCheckedRadioButtonId()));
                    priority = button.getText().toString();

                    Task newTask = new Task(taskName, date, priority);
                    Intent intent = new Intent();
                    intent.putExtra(TASK_OBJECT, newTask);
                    setResult(RESULT_OK, intent);
                    finish();

                }

            }
        });

        findViewById(R.id.cancelCreateId).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }
}