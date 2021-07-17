package com.example.calendaretna;

public class Event {
    String eventName, time, date, month, year, IDEventCreator;

    public Event(String eventName, String time, String date, String month, String year, String IDEventCreator){
        this.eventName = eventName;
        this.time = time;
        this.date = date;
        this.month = month;
        this.year = year;
        this.IDEventCreator = IDEventCreator;
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

    public String getIDEventCreator(){
        return IDEventCreator;
    }

    public void setIDEventCreator(String IDEventCreator){
        this.IDEventCreator = IDEventCreator;
    }

}
