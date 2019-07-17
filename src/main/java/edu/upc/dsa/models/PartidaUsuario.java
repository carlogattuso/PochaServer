package edu.upc.dsa.models;

public class PartidaUsuario {
    private int id;
    private int idPartida;
    private int idUsuario;
    private int puntuacion;

    public PartidaUsuario() {
    }

    public PartidaUsuario(int id, int idPartida, int idUsuario, int puntuacion) {
        this.id = id;
        this.idPartida = idPartida;
        this.idUsuario = idUsuario;
        this.puntuacion = puntuacion;
    }

    public PartidaUsuario(int idPartida, int idUsuario, int puntuacion) {
        this.idPartida = idPartida;
        this.idUsuario = idUsuario;
        this.puntuacion = puntuacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(int idPartida) {
        this.idPartida = idPartida;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }
}
