package edu.upc.dsa.models;

import java.util.List;

public class PartidaTO {

    private String nombre;
    private int numJugadores;
    private List<UsuarioTO> jugadores;

    public PartidaTO() {
    }

    public PartidaTO(String nombre, int numJugadores, List<UsuarioTO> jugadores) {
        this.nombre = nombre;
        this.numJugadores = numJugadores;
        this.jugadores = jugadores;
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

    public List<UsuarioTO> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<UsuarioTO> jugadores) {
        this.jugadores = jugadores;
    }
}
