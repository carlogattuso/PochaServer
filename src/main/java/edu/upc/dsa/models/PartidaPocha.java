package edu.upc.dsa.models;

public class PartidaPocha {

    private int id;
    private String nombre;
    private int numJugadores;

    public PartidaPocha() {
    }

    public PartidaPocha(int id, String nombre, int numJugadores) {
        this.id = id;
        this.nombre = nombre;
        this.numJugadores = numJugadores;
    }

    public PartidaPocha(String nombre, int numJugadores) {
        this.nombre = nombre;
        this.numJugadores = numJugadores;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumJugadores() {
        return numJugadores;
    }

    public void setNumJugadores(int numJugadores) {
        this.numJugadores = numJugadores;
    }
}
