package edu.upc.dsa.models;

import java.util.LinkedList;
import java.util.List;

public class UserJava {
    private int id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String mail;
    private int age;
    private Boolean upToDate;
    private List<GameJava> games = new LinkedList<>();

    public UserJava() {
    }

    public UserJava(int id, String username, String password, String name, String surname, String mail, int age) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.age = age;
        this.upToDate = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Boolean getUpToDate() {
        return upToDate;
    }

    public void setUpToDate(Boolean upToDate) {
        this.upToDate = upToDate;
    }

    public List<GameJava> getGames() {
        return games;
    }

    public void setGames(List<GameJava> games) {
        this.games = games;
    }

    public void addGame(GameJava game){
        this.games.add(game);
    }

    @Override
    public String toString() {
        return "UserJava [username=" + username + ", password=" + password + ", games=" + games.size() + "]";
    }
}
