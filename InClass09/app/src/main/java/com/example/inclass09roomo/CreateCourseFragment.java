package com.example.inclass09roomo;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CreateCourseFragment extends Fragment {

    EditText courseNumber;
    EditText courseName;
    EditText creditHours;
    RadioGroup courseGroup;
    RadioButton grade;
    CreateCourseListener createListener;

    public CreateCourseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_course, container, false);
        getActivity().setTitle(R.string.addCourse);

        courseNumber = view.findViewById(R.id.courseNumberCreate);
        courseName = view.findViewById(R.id.courseNameCreate);
        creditHours = view.findViewById(R.id.creditHoursCreate);
        courseGroup = view.findViewById(R.id.gradeGroup);

        courseGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                grade = view.findViewById(checkedId);
            }
        });

        view.findViewById(R.id.submit).setOnClickListener((v) -> {
            String courseNum = courseNumber.getText().toString();
            String courseNameString = courseName.getText().toString();
            String credit = creditHours.getText().toString();
            String gradeText = grade != null ? (String) grade.getText(): null;

            if(courseNum == null || courseNum.isEmpty()){
                displayToastMsg(getResources().getString(R.string.courseNum_error));
            }else if(courseNameString == null || courseNameString.isEmpty()){
                displayToastMsg(getResources().getString(R.string.courseName_error));
            }else if(credit == null || credit.isEmpty()){
                displayToastMsg(getResources().getString(R.string.credit_hours_error));
            }else if(gradeText == null){
                 displayToastMsg(getResources().getString(R.string.grade_error));
            }else{
                double creditHours = Double.parseDouble(credit);
                if(creditHours < 0){
                    displayToastMsg(getResources().getString(R.string.credit_hours_neg_error));
                }
                Course course = new Course(courseNum,
                        courseNameString,
                        creditHours,
                        gradeText.charAt(0));

                createListener.addCourse(course);
            }
        });

        view.findViewById(R.id.cancel).setOnClickListener((v) -> {
            createListener.goToGrades();
        });
        return view;
    }

    private void displayToastMsg(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof CreateCourseListener){
            createListener = (CreateCourseListener) context;
        }else{
            throw new RuntimeException(getContext()+" must implement CreateCourseListener.");
        }
    }

    interface CreateCourseListener{
        void addCourse(Course course);
        void goToGrades();
    }
}