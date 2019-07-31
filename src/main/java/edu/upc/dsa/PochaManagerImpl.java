package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.MYSQL.Factory;
import edu.upc.dsa.MYSQL.Session;
import edu.upc.dsa.models.*;
import edu.upc.dsa.util.RandomUtils;
import org.apache.log4j.Logger;

import javax.jws.soap.SOAPBinding;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class PochaManagerImpl implements PochaManager {
    private static PochaManager instance;

    private HashMap<String,UserJava> users;
    private Boolean upToDate = false;

    final static Logger logger = Logger.getLogger(PochaManagerImpl.class);

    private Logger log = Logger.getLogger(PochaManagerImpl.class.getName());

    private PochaManagerImpl(){
        this.users = new HashMap<>();
    }

    public static PochaManager getInstance(){
        if (instance==null) instance = new PochaManagerImpl();
        return instance;
    }

    @Override
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
            u = new User(username, password, name, surname, mail, age);
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
    public List<String> getAllUsers() throws Exception {
        Session session = null;
        List<String> allUsers = new LinkedList<>();

        if(this.upToDate){
            Set<String> stringList = this.users.keySet();
            for(String username : stringList){
                UserJava user = this.getUser(username,"admin");
                allUsers.add(user.getUsername());
            }
        }
        else{
            session = Factory.getSession();
            List<Object> users;
            users =  session.findAll(User.class);
            for(Object o : users){
                User u = (User) o;
                allUsers.add(u.getUsername());
                this.upToDate = true;
            }
        }
        return allUsers;
    }

    @Override
    public List<UsernameTO> searchUser(String piece) throws Exception {
        Session session = null;
        List<String> names = new LinkedList<>();
        List<UsernameTO> usernames = new LinkedList<>();

        try {
            session = Factory.getSession();
            names = session.searchUser(piece);
            for(String s : names){
                usernames.add(new UsernameTO(s));
            }
        } catch (EmptyUserListException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (session != null) session.close();
        }
        return usernames;
    }

    @Override
    public Game newGame(String gameType, String date, String winner, List<Player> players) throws Exception {
        Session session = null;
        Game game = null;

        try {
            session = Factory.getSession();
            game = new Game(gameType,date,winner);
            session.save(game);
            int id = session.findLastId(Game.class);
            game = (Game) session.get(Game.class,id);
            logger.info("New Game: " + game.toString());
            for(Player p : players){
                UserJava userJava = this.getUser(p.getUsername(),"admin");
                userJava.setUpToDate(false);
                Relation relation = new Relation(userJava.getId(),id,p.getPoints());
                session.save(relation);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (session != null) session.close();
        }
        return game;
    }

    @Override
    public List<GameTO> getGamesByUser(String username) throws Exception {
        Session session = null;
        UserJava user = null;
        GameJava gameJava = null;
        List<Game> gamesNoPlayers = new LinkedList<>();
        List<Player> players = new LinkedList<>();

        user = this.getUser(username,"admin");
        logger.info("User status: "+user.getUpToDate());
        if(!user.getUpToDate()) {
            try {
                logger.info("getGames: Searching on DB...");
                session = Factory.getSession();
                user.getGames().clear();
                gamesNoPlayers = session.findGamesIdByUserId(user.getId());
                for (Game g : gamesNoPlayers) {
                    gameJava = new GameJava(g.getId(), g.getGameType(), g.getDate(), g.getWinner());
                    players = session.findPlayersByGameId(g.getId());
                    for (Player p : players) {
                        gameJava.addPlayer(p);
                    }
                    logger.info("Game prepared: " + gameJava.toString());
                    user.addGame(gameJava);
                }
                user.setUpToDate(true);
            } catch (EmptyGameListException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                if (session != null) session.close();
            }
        }
        return this.passGameJavaToGameTO(user.getGames());
    }

    @Override
    public List<GameTO> getGamesByUserAndGame(String username, String gameType) throws Exception{
        List<GameTO> games = this.getGamesByUser(username);
        List<GameTO> gamesOrderedByGameType = new LinkedList<>();
        for(GameTO g : games){
            if(g.getGameType().equals(gameType)){
                gamesOrderedByGameType.add(g);
            }
        }
        if(gamesOrderedByGameType.size()==0) throw new EmptyGameListException();
        return gamesOrderedByGameType;
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

    public List<GameTO> passGameJavaToGameTO (List<GameJava> javaGames) {
        GameTO gameTO = null;
        List<GameTO> TOgames = new LinkedList<>();

        for(GameJava g : javaGames){
            gameTO = new GameTO(g.getGameType(),g.getDate(),g.getWinner(),g.getPlayers());
            TOgames.add(gameTO);
        }
        return TOgames;
    }
}
