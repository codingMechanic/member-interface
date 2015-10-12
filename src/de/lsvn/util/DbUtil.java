package de.lsvn.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class DbUtil {


    public static Connection getConnection() {
        Connection connection = null;
        try {
                Properties prop = new Properties();
                InputStream inputStream = DbUtil.class.getClassLoader().getResourceAsStream("/db.properties");
                prop.load(inputStream);
                String driver = prop.getProperty("driver");
                String url = prop.getProperty("url");
                String user = prop.getProperty("user");
                String password = prop.getProperty("password");
                Class.forName(driver);
                connection = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return connection;
    }
    public static void closeResultSet(ResultSet rs) {
        if (rs != null)
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void closeStatement(Statement stmt) {
        if (stmt != null)
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void closePreparedStatement(PreparedStatement stmt) {
        if (stmt != null)
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void closeConnection(Connection conn) {
        if (conn != null)
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}