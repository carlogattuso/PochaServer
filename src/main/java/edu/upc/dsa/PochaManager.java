package edu.upc.dsa;

import edu.upc.dsa.Exceptions.UserNotFoundException;
import edu.upc.dsa.models.PartidaPocha;
import edu.upc.dsa.models.PartidaTO;
import edu.upc.dsa.models.PartidaUsuario;
import edu.upc.dsa.models.Usuario;

import java.util.List;

public interface PochaManager {

    Usuario añadirUsuarios (String nombre) throws Exception;
    Usuario getUsuario (String nombre) throws UserNotFoundException;
    PartidaPocha añadirPartida (String nombre, int numJugadores) throws Exception;
    PartidaUsuario añadirPartidaUsuario (String nombrePartida, int idUsuario, int puntuacion) throws Exception;
    List<Usuario> getUsuarios() throws Exception;
    PartidaTO getPartida(String nombre) throws Exception;
    List<PartidaPocha> getPartidas() throws Exception;



    int size();
}
