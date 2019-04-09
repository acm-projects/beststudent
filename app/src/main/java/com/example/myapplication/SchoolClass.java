package com.example.myapplication;

public class SchoolClass {
    // instance variables
    private String className;
    private String room;
    private String classTime;
    private String professor;
    private String office;
    private String officeHours;

    // constructors
    public SchoolClass() {}
    public SchoolClass(String className, String room, String classTime, String professor, String office, String officeHours) {
        this.className = className;
        this.room = room;
        this.classTime = classTime;
        this.professor = professor;
        this.office = office;
        this.officeHours = officeHours;
    }

    // getters
    public String getClassName() {
        return className;
    }

    public String getRoom() {
        return room;
    }

    public String getClassTime() {
        return classTime;
    }

    public String getProfessor() {
        return professor;
    }

    public String getOffice() {
        return office;
    }

    public String getOfficeHours() {
        return officeHours;
    }

    // setters
    public void setClassName(String className) {
        this.className = className;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public void setOfficeHours(String officeHours) {
        this.officeHours = officeHours;
    }
}