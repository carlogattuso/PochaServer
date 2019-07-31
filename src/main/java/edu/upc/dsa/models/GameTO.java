package edu.upc.dsa.models;

import java.util.LinkedList;
import java.util.List;

public class GameTO {

    private String gameType;
    private String date;
    private String winner;
    private List<Player> players = new LinkedList<>();

    public GameTO() {
    }

    public GameTO(String gameType, String date, String winner, List<Player> players) {
        this.gameType = gameType;
        this.date = date;
        this.winner = winner;
        this.players = players;
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
}
