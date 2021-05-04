/*
a. Assignment #: HW01
b. File Name: MainActivity.java [HW01.app]
c. Full name of the student: Aakanksha Chauhan, Sindhura Chaganti
 */

package com.example.hw01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Seek bar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText billValue = findViewById(R.id.billValue);
        SeekBar seekBarInput = findViewById(R.id.seekBar);
        TextView seekOutput = findViewById(R.id.seekOutput);
        TextView tipOutput = findViewById(R.id.tipValue);
        TextView total = findViewById(R.id.totalDisplay);
        RadioGroup tips = findViewById(R.id.percentGroup);

        seekOutput.setText(String.valueOf(seekBarInput.getProgress() + "%"));
        //Listener to edit text
        billValue.setOnClickListener(new View.OnClickListener() {

            double bill;
            double tipValue;

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + billValue);
                if (billValue.getText() == null || billValue.getText().toString().isEmpty()) {
                    Toast.makeText(v.getContext(), "Enter the bill value", Toast.LENGTH_SHORT).show();
                } else {

                    bill = Double.parseDouble(billValue.getText().toString());
                      //tip and total bill amount calculation based on percent selected by the user
                    if (bill > 0) {
                        int tip = tips.getCheckedRadioButtonId();
                        if (tip == R.id.tenPercent) {
                            tipValue = bill * 10 / 100;
                        } else if (tip == R.id.fifteenPercent) {
                            tipValue = bill * 15 / 100;
                        } else if (tip == R.id.eighteenPercent) {
                            tipValue = bill * 18 / 100;
                        } else if (tip == R.id.custom) {
                            //Captures the values on Seekbar and calculates the values accordingly
                            seekBarInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                    seekOutput.setText(String.valueOf(progress) + "%");
                                    tipValue = bill * progress / 100;
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {
                                    Log.d(TAG, "onStartTrackingTouch: ");
                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {
                                    Log.d(TAG, "onStopTrackingTouch: ");
                                }

                            });
                        }


                        tipOutput.setText(String.valueOf(tipValue));
                        total.setText(String.valueOf(bill + tipValue));
                    }
                }
            }
        });
        //When clicked on Exit button, it exits the application
        findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
}
