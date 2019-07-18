package edu.upc.dsa;

import edu.upc.dsa.models.*;

import java.util.List;

public interface PochaManager {

    User newUser (String username, String password, String name, String surname, String mail, int age) throws Exception;
    UserJava getUser (String username, String password) throws Exception;
    GameJava newGame (String gameType, String date, String winner, List<Player> players) throws Exception;
    GameJava getLastGame (String username);
    List<GameJava> getGamesByUser (String username);
    List<GameJava> getGamesByUserAndGame (String username, String gameType);
    int userSize();

}
