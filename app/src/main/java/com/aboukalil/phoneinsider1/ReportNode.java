package com.aboukalil.phoneinsider1;

public class ReportNode
{
    private  int id;
    private  String date;
    private String task;
    private String contact;

    public ReportNode(){}

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTask() {
        return task;
    }

    public String getContact() {
        return contact;
    }



    public void setDate(String date) {
        this.date = date;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

}
