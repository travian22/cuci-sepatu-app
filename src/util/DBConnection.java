package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://sthhr.h.filess.io:3307/FreeDB_earswingus";
    private static final String USER = "FreeDB_earswingus";
    private static final String PASSWORD = "b3c27129e5a6603b9a982397cca8577fa5e7cd90";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
