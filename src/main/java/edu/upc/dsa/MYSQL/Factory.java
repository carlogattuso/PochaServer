package edu.upc.dsa.MYSQL;

public class Factory {
    public static Session getSession() {
        return new SessionImpl();
    }
}
