package com.example.calendaretna;

public class Event {
    String id, eventName, time, date, month, year, mailEventCreator;

    public Event(String id, String eventName, String time, String date, String month, String year, String mailEventCreator){
        this.id = id;
        this.eventName = eventName;
        this.time = time;
        this.date = date;
        this.month = month;
        this.year = year;
        this.mailEventCreator = mailEventCreator;
    }

    public String getIDEvent(){
        return id;
    }

    public void setIdName(String id){
        this.id = id;
    }

    public String getEventName(){
        return eventName;
    }

    public void setEventName(String eventName){
        this.eventName = eventName;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getMonth(){
        return month;
    }

    public void setMonth(String month){
        this.month = month;
    }

    public String getYear(){
        return year;
    }

    public void setYear(String year){
        this.year = year;
    }

    public String getMailEventCreator(){
        return mailEventCreator;
    }

    public void setIDEventCreator(String mailEventCreator){
        this.mailEventCreator = mailEventCreator;
    }

}
