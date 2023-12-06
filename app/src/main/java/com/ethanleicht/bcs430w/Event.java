package com.ethanleicht.bcs430w;

public class Event {
    private int id;
    private String movie;
    private String datetime;
    private int creator;

    public Event(int id, String movie, String datetime, int creator){
        this.id = id;
        this.movie = movie;
        this.datetime = datetime;
        this.creator = creator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }
}
