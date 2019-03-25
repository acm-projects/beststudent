package com.example.myapplication;

import java.util.Calendar;

public class Task {
    private String taskName;
    private Calendar dueDate;

    public Task(String name, Calendar date){
        taskName = name;
        dueDate = date;
    }

    public void setTaskName(String name){
        taskName = name;
    }

    public void setDueDate(Calendar date){
        dueDate = date;
    }

    public String getTaskName(){
        return taskName;
    }

    public Calendar getDueDate(){
        return dueDate;
    }
}
