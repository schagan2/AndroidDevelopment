package com.example.inclass09roomo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CourseDAO {

    @Query("SELECT * FROM course")
    List<Course> getAll();

    @Query("SELECT * FROM course WHERE course_number = :courseNo LIMIT 1")
    Course getByCourseNumber(String courseNo);

    @Update
    void update(Course course);

    @Insert
    void insertAll(Course... courses);

    @Delete
    void delete(Course course);
}
