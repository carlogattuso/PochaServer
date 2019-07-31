package edu.upc.dsa;

import edu.upc.dsa.models.*;

import java.util.List;

public interface PochaManager {

    User newUser (String username, String password, String name, String surname, String mail, int age) throws Exception;
    UserJava getUser (String username, String password) throws Exception;
    List<String> getAllUsers() throws Exception;
    List<UsernameTO> searchUser(String piece) throws Exception;
    Game newGame (String gameType, String date, String winner, List<Player> players) throws Exception;
    List<GameTO> getGamesByUser (String username) throws Exception;
    List<GameTO> getGamesByUserAndGame (String username, String gameType) throws Exception;
    int userSize();
}
