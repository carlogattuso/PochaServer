package edu.upc.dsa.models;

import java.util.LinkedList;
import java.util.List;

public class GameJava {
    private int id;
    private String gameType;
    private String date;
    private String winner;
    private List<Player> players = new LinkedList<>();

    public GameJava() {
    }

    public GameJava(int id, String gameType, String date, String winner) {
        this.id = id;
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

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player){
        this.players.add(player);
    }

    @Override
    public String toString() {
        return "GameJava [gameType=" + gameType + ", date=" + date + ", winner=" + winner + ", players=" + players.size() + "]";
    }
}
