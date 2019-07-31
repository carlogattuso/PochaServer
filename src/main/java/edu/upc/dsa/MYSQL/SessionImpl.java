package edu.upc.dsa.MYSQL;

import edu.upc.dsa.exceptions.EmptyGameListException;
import edu.upc.dsa.exceptions.EmptyUserListException;
import edu.upc.dsa.exceptions.UserAlreadyExistsException;

import edu.upc.dsa.exceptions.UserNotFoundException;
import edu.upc.dsa.models.Game;
import edu.upc.dsa.models.Player;
import edu.upc.dsa.models.Relation;
import edu.upc.dsa.models.User;

import org.apache.log4j.Logger;

import javax.print.DocFlavor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.sql.Types.*;

public class SessionImpl implements Session {

    private Logger log = Logger.getLogger(SessionImpl.class.getName());
    private Connection connection;

    SessionImpl() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost/pochaDB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "carlosandro");
            log.info("Connected to db");
        } catch (Exception e) {
            log.error("Error exception");
            e.printStackTrace();
        }
    }

    public void save(Object entity) throws Exception {

        if (entity instanceof User)
            if (!find(User.class, -1, entity.getClass().getMethod("getUsername").invoke(entity).toString()).isEmpty()) throw new UserAlreadyExistsException();

        Field[] fields = entity.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();

        String query ="INSERT INTO " + entity.getClass().getSimpleName() +" (";
        for (Field f: fields) sb.append(f.getName()).append(",");
        query += sb.deleteCharAt(sb.length() - 1).toString();
        query += ") VALUES (";
        sb = new StringBuilder();
        for (Field f: fields) sb.append("?,");
        query += sb.deleteCharAt(sb.length() - 1).toString();
        query += ")";

        log.info("query: " + query);

        PreparedStatement prep = this.connection.prepareStatement(query);
        for (int i = 1; i < fields.length + 1; i++) {
            if (int.class.equals(fields[i - 1].getType())) {
                String labelName = fields[i - 1].getName().substring(0, 1).toUpperCase() + fields[i - 1].getName().substring(1);
                int intValue = (int) entity.getClass().getMethod("get" + labelName).invoke(entity);
                prep.setInt(i, intValue);
            } else if (String.class.equals(fields[i - 1].getType())) {
                prep.setString(i, new PropertyDescriptor(fields[i - 1].getName(), entity.getClass()).getReadMethod().invoke(entity).toString());
            } else {
                String labelName = fields[i - 1].getName().substring(0, 1).toUpperCase() + fields[i - 1].getName().substring(1);
                Timestamp timestampValue = (Timestamp) entity.getClass().getMethod("get" + labelName).invoke(entity);
                prep.setTimestamp(i, timestampValue);
            }
        }

        prep.execute();
        prep.close();
    }

    public User getByUsername(String username) throws Exception {
        User u = null;
        try {
            u = (User) find(User.class, -1, username).get(0);
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
        return u;
    }

    public Object get(Class theClass, int id) throws Exception {
        return find(theClass, id, null).get(0);
    }

    public List<Object> findAll(Class theClass) throws Exception {
        return find(theClass, 0, null);
    }

    public void update(Object entity, int id) throws Exception {
        Field[] fields = entity.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();

        String query ="UPDATE " + entity.getClass().getSimpleName() +" SET ";
        for (Field f: fields) sb.append(f.getName()).append("=?,");
        query += sb.deleteCharAt(sb.length() - 1).toString();
        query += " WHERE id = ?";

        PreparedStatement prep = this.connection.prepareStatement(query);
        for (int i = 1; i < fields.length + 1; i++) prep.setString(i, new PropertyDescriptor(fields[i - 1].getName(), entity.getClass()).getReadMethod().invoke(entity).toString());
        prep.setInt(fields.length + 1, id);
        prep.execute();
        prep.close();
        log.info("query: " + query);
    }

    public void delete(Class theClass, int id) throws Exception {
        Object object = find(theClass, id, "admin").get(0);
        String query = "DELETE FROM " + theClass.getSimpleName() + " WHERE id = ?";
        PreparedStatement prep = this.connection.prepareStatement(query);
        prep.setInt(1, id);
        prep.execute();
        log.info("que sale:" + prep);
        log.info("query: " + query);
    }

    public void close() throws Exception {
        this.connection.close();
        log.info("Connection closed");
    }

    public List<Object> find(Class theClass, int id, String username) throws Exception {

        List<Object> res = new ArrayList<>();
        ResultSet rs;
        Object object;
        String query;

        if (id > 0) {
            query = "SELECT * FROM " + theClass.getSimpleName() + " WHERE id = ?";
            PreparedStatement prep = this.connection.prepareStatement(query);
            prep.setInt(1, id);
            prep.execute();
            rs = prep.getResultSet();
        } else if (id == 0) {
            query = "SELECT * FROM " + theClass.getSimpleName();
            Statement statement = this.connection.createStatement();
            statement.execute(query);
            rs = statement.getResultSet();
        } else { //id == -1
            log.info("Username: " + username);
            query = "SELECT * FROM " + theClass.getSimpleName() + " WHERE username = ?";
            PreparedStatement prep = this.connection.prepareStatement(query);
            prep.setString(1, username);
            prep.execute();
            rs = prep.getResultSet();
        }
            log.info("query (find): " + query);
            while (rs.next()) {

                log.info("Creating object...");
                object = theClass.newInstance();
                log.info("Object created");

                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    String columnName = rs.getMetaData().getColumnName(i);
                    columnName = columnName.substring(0, 1).toUpperCase() + columnName.substring(1);

                    log.info("Column name: " + columnName);

                    switch (rs.getMetaData().getColumnType(i)) {
                        case INTEGER:
                            int intValue = rs.getInt(i);
                            theClass.getMethod("set" + columnName, int.class).invoke(object, intValue);
                            log.info("set" + columnName);
                            break;
                        case VARCHAR:
                            String stringValue = rs.getString(i);
                            theClass.getMethod("set" + columnName, String.class).invoke(object, stringValue);
                            log.info("set" + columnName);
                            break;
                        case TIMESTAMP:
                            Timestamp dateValue = rs.getTimestamp(i);
                            theClass.getMethod("set" + columnName, Timestamp.class).invoke(object, dateValue);
                            break;
                        case BOOLEAN:
                            Boolean booleanValue = rs.getBoolean(i);
                            theClass.getMethod("set" + columnName, Boolean.class).invoke(object, booleanValue);
                            break;
                        default:
                            log.info("Missing type: " + rs.getMetaData().getColumnType(i));
                            break;
                    }
                }
                log.info("Object founded: " + object);
                res.add(object);
            }
            return res;
    }

    @Override
    public int findLastId(Class theClass) throws Exception {
        int res;
        ResultSet rs;
        String query;

        query = "SELECT MAX(id) FROM pochaDB."+theClass.getSimpleName();
        PreparedStatement prep = this.connection.prepareStatement(query);
        prep.execute();
        rs = prep.getResultSet();

        log.info("query (find): " + query);

        rs.next();
        res = rs.getInt(1);

        return res;
    }

    @Override
    public List<Game> findGamesIdByUserId(int idUser) throws Exception {
        List<Game> list = new LinkedList<>();
        ResultSet rs;
        String query;
        Game game;

        query = "SELECT pochaDB.Game.*"
        + " FROM pochaDB.User,pochaDB.Game,pochaDB.Relation"
        + " WHERE pochaDB.User.id="
        + idUser
        + " AND pochaDB.User.id=pochaDB.Relation.idUser"
        + " AND pochaDB.Relation.idGame=pochaDB.Game.id"
        + " ORDER BY id ASC";
        PreparedStatement prep = this.connection.prepareStatement(query);
        prep.execute();
        rs = prep.getResultSet();

        log.info("query (find): " + query);

        while (rs.next()) {

            log.info("Creating game...");
            game = Game.class.newInstance();
            log.info("Object created");
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                String columnName = rs.getMetaData().getColumnName(i);
                columnName = columnName.substring(0, 1).toUpperCase() + columnName.substring(1);

                log.info("Column name: " + columnName);

                switch (rs.getMetaData().getColumnType(i)) {
                    case INTEGER:
                        int intValue = rs.getInt(i);
                        Game.class.getMethod("set" + columnName, int.class).invoke(game, intValue);
                        log.info("set" + columnName);
                        break;
                    case VARCHAR:
                        String stringValue = rs.getString(i);
                        Game.class.getMethod("set" + columnName, String.class).invoke(game, stringValue);
                        log.info("set" + columnName);
                        break;
                    case TIMESTAMP:
                        Timestamp dateValue = rs.getTimestamp(i);
                        Game.class.getMethod("set" + columnName, Timestamp.class).invoke(game, dateValue);
                        break;
                    case BOOLEAN:
                        Boolean booleanValue = rs.getBoolean(i);
                        Game.class.getMethod("set" + columnName, Boolean.class).invoke(game, booleanValue);
                        break;
                    default:
                        log.info("Missing type: " + rs.getMetaData().getColumnType(i));
                        break;
                }
            }
            log.info("Object founded: " + game);
            list.add(game);
        }
        if(list.size()==0) throw new EmptyGameListException();
        return list;
    }

    @Override
    public List<Player> findPlayersByGameId(int idGame) throws Exception {
        List<Player> list = new LinkedList<>();
        ResultSet rs;
        String query;
        Player player;

        query = "SELECT pochaDB.User.username,pochaDB.Relation.points"
        + " FROM pochaDB.User,pochaDB.Relation,pochaDB.Game"
        + " WHERE pochaDB.Game.id="
        + idGame
        + " AND pochaDB.User.id=pochaDB.Relation.idUser"
        + " AND pochaDB.Relation.idGame=pochaDB.Game.id"
        + " ORDER BY pochaDB.Relation.points DESC";
        PreparedStatement prep = this.connection.prepareStatement(query);
        prep.execute();
        rs = prep.getResultSet();

        log.info("query (find): " + query);

        while (rs.next()) {

            log.info("Creating player...");
            player = Player.class.newInstance();
            log.info("Player created");

            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                String columnName = rs.getMetaData().getColumnName(i);
                columnName = columnName.substring(0, 1).toUpperCase() + columnName.substring(1);

                log.info("Column name: " + columnName);

                switch (rs.getMetaData().getColumnType(i)) {
                    case INTEGER:
                        int intValue = rs.getInt(i);
                        Player.class.getMethod("set" + columnName, int.class).invoke(player, intValue);
                        log.info("set" + columnName);
                        break;
                    case VARCHAR:
                        String stringValue = rs.getString(i);
                        Player.class.getMethod("set" + columnName, String.class).invoke(player, stringValue);
                        log.info("set" + columnName);
                        break;
                    case TIMESTAMP:
                        Timestamp dateValue = rs.getTimestamp(i);
                        Player.class.getMethod("set" + columnName, Timestamp.class).invoke(player, dateValue);
                        break;
                    case BOOLEAN:
                        Boolean booleanValue = rs.getBoolean(i);
                        Player.class.getMethod("set" + columnName, Boolean.class).invoke(player, booleanValue);
                        break;
                    default:
                        log.info("Missing type: " + rs.getMetaData().getColumnType(i));
                        break;
                }
            }
            log.info("Object founded: " + player);
            list.add(player);
        }
        if(list.size()==0) throw new EmptyGameListException();
        return list;
    }

    @Override
    public List<String> searchUser(String piece) throws Exception {
        List<String> list = new LinkedList<>();
        ResultSet rs;
        String query;
        String username;

        query = "SELECT pochaDB.User.username FROM pochaDB.User WHERE pochaDB.User.username LIKE '" + piece + "%'";
        PreparedStatement prep = this.connection.prepareStatement(query);
        prep.execute();
        rs = prep.getResultSet();

        log.info("query (search user): " + query);

        while (rs.next()) {
            String stringValue = rs.getString(1);
            list.add(stringValue);
        }

        if(list.size()==0) throw new EmptyUserListException();
        return list;
    }
}
