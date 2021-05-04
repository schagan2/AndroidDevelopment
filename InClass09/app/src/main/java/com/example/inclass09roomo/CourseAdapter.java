package com.example.inclass09roomo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseViewHolder> {
    List<Course> courses;
    CourseFragment.CourseListener listener;

    public CourseAdapter(List<Course> courses, CourseFragment.CourseListener courseListener) {
        this.courses = courses;
        this.listener = courseListener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_course, parent, false);
        CourseViewHolder viewHolder = new CourseViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courses.get(position);

        holder.courseGrade.setText(String.valueOf(course.grade));
        holder.courseNumber.setText(course.courseNumber);
        holder.courseName.setText(course.courseName);
        holder.creditHours.setText(String.valueOf(course.creditHours)+ " Credit Hours");

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteCourse(course);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }
}

class CourseViewHolder extends RecyclerView.ViewHolder{

    TextView courseGrade;
    TextView courseNumber;
    TextView courseName;
    TextView creditHours;
    ImageButton deleteButton;

    public CourseViewHolder(@NonNull View itemView) {
        super(itemView);
        courseGrade = itemView.findViewById(R.id.courseGrade);
        courseNumber = itemView.findViewById(R.id.courseNumber);
        courseName = itemView.findViewById(R.id.courseName);
        creditHours = itemView.findViewById(R.id.creditHours);
        deleteButton = itemView.findViewById(R.id.deleteButton);
    }
}