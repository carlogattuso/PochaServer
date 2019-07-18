package edu.upc.dsa.models;

public class Relation {
    int idUser;
    int idGame;
    int points;

    public Relation() {
    }

    public Relation(int idUser, int idGame, int points) {
        this.idUser = idUser;
        this.idGame = idGame;
        this.points = points;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdGame() {
        return idGame;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
