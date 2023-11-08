package com.ethanleicht.bcs430w;

public class User {
    private int id;
    private String username;
    private String bio;

    public User(String username, int id){
        this.username = username;
        this.id = id;
    }
    public User(int id, String username, String bio){
        this.username = username;
        this.id = id;
        this.bio = bio;
    }

    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getBio() { return bio; }
}
