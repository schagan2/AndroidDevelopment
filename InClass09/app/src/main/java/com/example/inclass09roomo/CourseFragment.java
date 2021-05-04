package com.example.inclass09roomo;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private List<Course> coursesList;
    TextView gpaText;
    TextView hoursText;
    RecyclerView recyclerView;
    CourseAdapter courseAdapter;
    LinearLayoutManager layoutManager;
    CourseListener courseListener;
    double creditHours = 0.0;
    double hours = 0.0;

    public CourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param coursesList Parameter 1.
     * @return A new instance of fragment CourseFragment.
     */
    public static CourseFragment newInstance(List<Course> coursesList) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, (Serializable) coursesList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            coursesList = (List<Course>) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        getActivity().setTitle(R.string.grades);

        gpaText = view.findViewById(R.id.gpa);
        hoursText = view.findViewById(R.id.hours);
        recyclerView = view.findViewById(R.id.courseList);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        courseAdapter = new CourseAdapter(coursesList, courseListener);
        recyclerView.setAdapter(courseAdapter);

        for(Course course : coursesList){
            hours += course.creditHours;

            if(course.grade == 'A'){
                creditHours += (course.creditHours * 4.0);
            }else if(course.grade == 'B'){
                creditHours += (course.creditHours * 3.0);
            }else if(course.grade == 'C'){
                creditHours += (course.creditHours * 2.0);
            }else if(course.grade == 'D'){
                creditHours += (course.creditHours * 1.0);
            }else if(course.grade == 'F'){
                creditHours += (course.creditHours * 0.0);
            }
        }

        String gpa = (String) gpaText.getText();
        if(hours == 0.0){
            gpa = gpa.concat(" 4.0");
        }else{
            gpa = gpa.concat(" "+ (creditHours/hours));
        }
        gpaText.setText(gpa);

        String hoursString = (String) hoursText.getText();
        hoursString = hoursString.concat(" "+ hours);

        hoursText.setText(hoursString);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof CourseListener){
            courseListener = (CourseListener) context;
        }else{
            throw new RuntimeException(getContext()+" must implement CourseListener.");
        }
    }

    interface CourseListener{
        void deleteCourse(Course course);
    }
}