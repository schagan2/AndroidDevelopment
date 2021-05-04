/*
a. Assignment #: In Class 02
b. File Name: InClass2a
c. Full name of the students: Aakanksha Chauhan, Sindhura Chaganti
d. Group: A 12
*/

package com.example.inclass2a;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView result = findViewById(R.id.res);
        EditText inches = findViewById(R.id.inputInch);
        Button clearAll = (Button)findViewById(R.id.clearAll);


        findViewById(R.id.toFeet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double inputInch = Double.parseDouble(inches.getText().toString());
                result.setText(String.valueOf(inputInch*0.0833));

            }
        });
        findViewById(R.id.toMeters).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double inputInch = Double.parseDouble(inches.getText().toString());
                result.setText(String.valueOf(inputInch*0.0254));

            }
        });
        findViewById(R.id.toMiles).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double inputInch = Double.parseDouble(inches.getText().toString());
                result.setText(String.valueOf(inputInch*1/63360));

            }
        });
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inches.setText("");
                result.setText("");

            }
        });
    }

}
