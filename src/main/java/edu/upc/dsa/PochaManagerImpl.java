package edu.upc.dsa;

import edu.upc.dsa.exceptions.PasswordNotMatchException;
import edu.upc.dsa.exceptions.UserAlreadyExistsException;
import edu.upc.dsa.MYSQL.Factory;
import edu.upc.dsa.MYSQL.Session;
import edu.upc.dsa.exceptions.UserNotFoundException;
import edu.upc.dsa.models.*;
import edu.upc.dsa.util.RandomUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;

public class PochaManagerImpl implements PochaManager {
    private static PochaManager instance;

    private HashMap<String,UserJava> users;

    final static Logger logger = Logger.getLogger(PochaManagerImpl.class);

    private Logger log = Logger.getLogger(PochaManagerImpl.class.getName());

    private PochaManagerImpl(){
        this.users = new HashMap<>();
    }

    public static PochaManager getInstance(){
        if (instance==null) instance = new PochaManagerImpl();
        return instance;
    }

    public int userSize() {
        int ret = this.users.size();
        logger.info("Users size " + ret);
        return ret;
    }

    @Override
    public User newUser(String username, String password, String name, String surname, String mail, int age) throws Exception {
        Session session = null;
        User u = null;

        try {
            this.checkUser(username);
            session = Factory.getSession();
            u = new User(0,username, password, name, surname, mail, age);
            session.save(u);
            log.info("Registered: " + u);
            this.getUser(u.getUsername(),u.getPassword());
        } catch (UserAlreadyExistsException e) {
            log.info("User already exists");
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (session != null) session.close();
        }
        return u;
    }

    @Override
    public UserJava getUser(String username, String password) throws Exception{
        Session session = null;
        UserJava u = this.users.get(username);

        if(u==null){
            try {
                session = Factory.getSession();
                User user = session.getByUsername(username);
                u = new UserJava(user.getId(),user.getUsername(), user.getPassword(), user.getName(), user.getSurname(), user.getMail(), user.getAge());
                this.users.put(username,u);
                this.checkPassword(username,password);
                logger.info("Logged in: " + u.toString());
            } catch (PasswordNotMatchException e) {
                throw e;
            } catch (UserNotFoundException e2){
                throw e2;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                if (session != null) session.close();
            }
        }
        else{
            try {
                this.checkPassword(username,password);
                logger.info("Logged in: " + u.toString());
            } catch (PasswordNotMatchException e){
                throw e;
            }
        }
        return u;
    }

    @Override
    public GameJava newGame(String gameType, String date, String winner, List<Player> players) throws Exception {
        Session session = null;
        GameJava gameJava;
        try {
            session = Factory.getSession();
            Game game = new Game(gameType,date,winner);
            session.save(game);
            gameJava = new GameJava(0,gameType,date,winner);
            logger.info("New Game: " + game.toString());
            for(Player p : players){
                gameJava.addPlayer(p);
                UserJava userJava = this.getUser(p.getUsername(),"admin");
                Relation relation = new Relation(userJava.getId(),gameJava.getId(),p.getPoints());
                session.save(relation);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (session != null) session.close();
        }
        return gameJava;
    }

    @Override
    public GameJava getLastGame(String username) {
        return null;
    }

    @Override
    public List<GameJava> getGamesByUser(String username) {
        return null;
    }

    @Override
    public List<GameJava> getGamesByUserAndGame(String username, String gameType) {
        return null;
    }

    //Internal methods

    public void checkUser (String username) throws Exception{
        UserJava u = this.users.get(username);
        if(u!=null) throw new UserAlreadyExistsException();
    }

    public void checkPassword (String username, String password) throws Exception{
        UserJava u = this.users.get(username);
        if(password.equals("admin")) return;
        if(!u.getPassword().equals(password)) throw new PasswordNotMatchException();
    }

    /*@Override
    public User newUser(String nombre) throws Exception {
        Session session = null;
        User u = usuarios.get(nombre);

        if (u == null) {
            try {
                u=new User(nombre);
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
    */
    /*@Override
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
        */
}
