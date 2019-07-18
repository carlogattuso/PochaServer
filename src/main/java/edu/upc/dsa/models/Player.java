package edu.upc.dsa.models;

public class Player {

    private String username;
    private int points;

    public Player() {
    }

    public Player(String username, int points) {
        this.username = username;
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Player [username=" + username + ", points=" + points + "]";
    }
}
