package edu.upc.dsa.models;

import java.util.LinkedList;
import java.util.List;

public class Game {
    private int id;
    private String gameType;
    private String date;
    private String winner;

    public Game() {
    }

    public Game(String gameType, String date, String winner) {
        this.gameType = gameType;
        this.date = date;
        this.winner = winner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "GameJava [id=" + id + ", gameType=" + gameType + ", date=" + date + ", winner=" + winner + "]";
    }
}
