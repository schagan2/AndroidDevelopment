package com.example.inclass09roomo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "course")
public class Course {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "course_number")
    public String courseNumber;

    @ColumnInfo(name = "course_name")
    public String courseName;

    @ColumnInfo(name = "credit_hours")
    public double creditHours;

    @ColumnInfo
    public char grade;

    public Course(String courseNumber, String courseName, double creditHours, char grade) {
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseNumber='" + courseNumber + '\'' +
                ", courseName='" + courseName + '\'' +
                ", creditHours=" + creditHours +
                ", grade=" + grade +
                '}';
    }
}