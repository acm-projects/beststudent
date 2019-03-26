package com.example.myapplication;

import java.util.Calendar;

public class Task {
    private String taskName;
    private Calendar dueDate;
    private String notes;

    public Task(String name, Calendar date){
        taskName = name;
        dueDate = date;
        notes = "";
    }

    public Task(String name, Calendar date, String note){
        taskName = name;
        dueDate = date;
        notes = note;
    }

    public void setTaskName(String name){
        taskName = name;
    }

    public void setDueDate(Calendar date){
        dueDate = date;
    }

    public void setNotes(String note){
        notes = note;
    }

    public String getTaskName(){
        return taskName;
    }

    public Calendar getDueDate(){
        return dueDate;
    }

    public String getNotes(){
        return notes;
    }
}
