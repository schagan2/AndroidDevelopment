package com.example.inclass2b;

/*Assignment: Inclass 02
* File Name: MainActivity[Inclass_2b.app]
* Full name of the student: Aakansha Chauhan, Sindhura Chaganti
*/

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button convert;
    RadioGroup conversionGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView result = findViewById(R.id.resultOutput);
        EditText inches = findViewById(R.id.inputInches);

        findViewById(R.id.convert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conversionGroup = findViewById(R.id.conversionGroup);
                int checkedId = conversionGroup.getCheckedRadioButtonId();
                if(checkedId == R.id.to_meters){
                    double inputInch = Double.parseDouble(inches.getText().toString());
                    result.setText(String.valueOf(inputInch*0.0254));
                }else if(checkedId == R.id.to_feet){
                    double inputInch = Double.parseDouble(inches.getText().toString());
                    result.setText(String.valueOf(inputInch*1/12));
                }else if(checkedId == R.id.to_miles){
                    double inputInch = Double.parseDouble(inches.getText().toString());
                    result.setText(String.valueOf(inputInch*1/63360));
                }else if(checkedId == R.id.clearAll){
                    inches.setText("");
                    result.setText("");
                }
            }
        });

    }
}