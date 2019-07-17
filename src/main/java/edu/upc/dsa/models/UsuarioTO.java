package edu.upc.dsa.models;

public class UsuarioTO {

    private String nombre;
    private int Puntuacion;

    public UsuarioTO(String nombre, int puntuacion) {
        this.nombre = nombre;
        Puntuacion = puntuacion;
    }

    public UsuarioTO() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntuacion() {
        return Puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        Puntuacion = puntuacion;
    }
}
