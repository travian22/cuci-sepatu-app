package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12782324";
    private static final String USER = "sql12782324";
    private static final String PASSWORD = "KCKPWwMcyx";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
