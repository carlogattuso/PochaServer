package edu.upc.dsa.MYSQL;


import edu.upc.dsa.models.Game;
import edu.upc.dsa.models.Player;
import edu.upc.dsa.models.User;

import java.util.List;

public interface Session {

    //DAO

    void save(Object entity) throws Exception;
    Object get(Class theClass, int id) throws Exception;
    List<Object> findAll(Class theClass) throws Exception;
    List<Object> find(Class theClass, int id, String username) throws Exception;
    int findLastId(Class theClass) throws Exception;
    void update(Object object, int id) throws Exception;
    void delete(Class theClass, int id) throws Exception;
    void close() throws Exception;

    //Internal queries
    User getByUsername(String username) throws Exception;
    List<Game> findGamesIdByUserId (int idUser) throws Exception;
    List<Player> findPlayersByGameId (int idGame) throws Exception;
    List<String> searchUser (String piece) throws Exception;
}
