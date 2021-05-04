package com.example.hw02;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Task implements Serializable, Comparable<Task>{
    String taskName;
    Date date;
    String priority;

    public Task() {
    }

    public Task(String taskName, Date date, String priority) {
        this.taskName = taskName;
        this.date = date;
        this.priority = priority;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStringDate(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH)+1;
        int year = cal.get(Calendar.YEAR);
        String slash = "/";
        return String.valueOf(month).concat(slash).concat(String.valueOf(day)).concat(slash).concat(String.valueOf(year));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskName.equals(task.taskName) &&
                date.equals(task.date) &&
                priority.equals(task.priority);
    }

    @SuppressLint("NewApi")
    @Override
    public int hashCode() {
        return Objects.hash(taskName, date, priority);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", date=" + date +
                ", priority='" + priority + '\'' +
                '}';
    }

    @Override
    public int compareTo(Task task) {
        return this.date.compareTo(task.date);
    }
}
