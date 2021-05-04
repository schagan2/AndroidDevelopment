package com.example.inclass09roomo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import java.util.List;
/*
 * Assignment: Inclass09
 * FileName: MainActivity.java [Inclass09Room0.app]
 * Names: Aakansha Chauhan, Sindhura Chaganti
 */
public class MainActivity extends AppCompatActivity implements CourseFragment.CourseListener, CreateCourseFragment.CreateCourseListener {

    AppDatabase db;
    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        db = Room.databaseBuilder(this, AppDatabase.class, "course.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        List<Course> list = db.courseDAO().getAll();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, CourseFragment.newInstance(list))
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_add){
            menuItem = item;
            menuItem.setVisible(false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rootView, new CreateCourseFragment())
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void deleteCourse(Course course) {
        db.courseDAO().delete(course);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, CourseFragment.newInstance(db.courseDAO().getAll()))
                .commit();
    }

    @Override
    public void addCourse(Course course) {
        try{
            menuItem.setVisible(true);
            db.courseDAO().insertAll(course);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rootView, CourseFragment.newInstance(db.courseDAO().getAll()))
                    .commit();
        }catch(SQLiteConstraintException ex){
            Toast.makeText(this, "Already course exists with same course number.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void goToGrades() {
        menuItem.setVisible(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, CourseFragment.newInstance(db.courseDAO().getAll()))
                .commit();
    }
}