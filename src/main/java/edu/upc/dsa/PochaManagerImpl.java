package edu.upc.dsa;

import edu.upc.dsa.Exceptions.UserAlreadyExistsException;
import edu.upc.dsa.Exceptions.UserNotFoundException;
import edu.upc.dsa.MYSQL.Factory;
import edu.upc.dsa.MYSQL.Session;
import edu.upc.dsa.models.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PochaManagerImpl implements PochaManager {
    private static PochaManager instance;

    private HashMap<String,Usuario> usuarios;
    private HashMap<String,PartidaPocha> partidas;

    final static Logger logger = Logger.getLogger(PochaManagerImpl.class);

    private Logger log = Logger.getLogger(PochaManagerImpl.class.getName());

    private PochaManagerImpl(){
        this.usuarios = new HashMap<>();
        this.partidas = new HashMap<>();
    }

    public static PochaManager getInstance(){
        if (instance==null) instance = new PochaManagerImpl();
        return instance;
    }

    public int size() {
        int ret = this.usuarios.size();
        logger.info("size " + ret);
        return ret;
    }

    @Override
    public Usuario añadirUsuarios(String nombre) throws Exception {
        Session session = null;
        Usuario u = usuarios.get(nombre);

        if (u == null) {
            try {
                u=new Usuario(nombre);
                session = Factory.getSession();
                session.save(u);

                log.info("User insert: " + u);
                this.usuarios.put(nombre, u);
            } catch (UserAlreadyExistsException e) {
                log.info("User already exists");
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                if (session != null) session.close();
            }
            logger.info("New user: " + u.toString());
            return u;
        } else throw new UserAlreadyExistsException();
    }

    @Override
    public Usuario getUsuario(String nombre) throws UserNotFoundException{
        Usuario u = usuarios.get(nombre);
        if(u==null) throw new UserNotFoundException();
        return u;
    }

    @Override
    public PartidaPocha añadirPartida(String nombre, int numJugadores) throws Exception {
        Session session = null;
        PartidaPocha partidaPocha = partidas.get(nombre);
        if (partidaPocha == null) {
            partidaPocha = new PartidaPocha(nombre, numJugadores);
            try {
                session = Factory.getSession();
                session.save(partidaPocha);
                partidas.put(partidaPocha.getNombre(),partidaPocha);
                log.info("Partida insert: " + partidaPocha);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                if (session != null) session.close();
            }
            logger.info("New user: " + partidaPocha.toString());
            return partidaPocha;
        } else return null;
    }

    @Override
    public PartidaUsuario añadirPartidaUsuario(String nombre, int idUsuario, int puntuacion) throws Exception {
        Session session = null;
        PartidaPocha partidaPocha = partidas.get(nombre);
        PartidaUsuario partidaUsuario = new PartidaUsuario(partidaPocha.getId(),idUsuario,puntuacion);
        try {
            session = Factory.getSession();
            session.save(partidaUsuario);

            log.info("Partida insert: " + partidaUsuario);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (session != null) session.close();
        }
        logger.info("New user: " + partidaUsuario.toString());
        return partidaUsuario;
    }

    @Override
    public List<Usuario> getUsuarios() throws Exception {
        Session session = null;
        try {
            List<Usuario> listUsers = new ArrayList<>();
            session = Factory.getSession();
            List<Object> lista = session.findAll(Usuario.class);
            for (Object object : lista) {
                if (object instanceof Usuario) {
                    Usuario CP = (Usuario) object;
                    usuarios.put(CP.getNombre(),CP);
                    listUsers.add(CP);
                }
            }
            return listUsers;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public PartidaTO getPartida(String nombre) throws Exception {
        Session session = null;
        PartidaPocha partidaPocha = partidas.get(nombre);
        if (partidaPocha != null) {
            try {
                List<UsuarioTO> listUsers = new ArrayList<>();
                session = Factory.getSession();
                List<Object> lista = session.find(PartidaUsuario.class,partidaPocha.getId(),partidaPocha.getNombre());
                for (Object object : lista) {
                    if (object instanceof PartidaUsuario) {
                        Usuario usuario = (Usuario) session.get(Usuario.class,((PartidaUsuario) object).getId());
                        UsuarioTO usuarioTO = new UsuarioTO(usuario.getNombre(),((PartidaUsuario) object).getPuntuacion());
                        listUsers.add(usuarioTO);
                    }
                }
                PartidaTO partidaTO = new PartidaTO(nombre,listUsers.size(),listUsers);
                return partidaTO;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                if (session != null) session.close();
            }
        }
        return null;
    }

    @Override
    public List<PartidaPocha> getPartidas() throws Exception {
        Session session = null;
        try {
            List<PartidaPocha> listPartidas = new ArrayList<>();
            session = Factory.getSession();
            List<Object> lista = session.findAll(PartidaPocha.class);
            for (Object object : lista) {
                if (object instanceof PartidaPocha) {
                    PartidaPocha CP = (PartidaPocha) object;
                    partidas.put(CP.getNombre(),CP);
                    listPartidas.add(CP);
                }
            }
            return listPartidas;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (session != null) session.close();
        }    }
}
