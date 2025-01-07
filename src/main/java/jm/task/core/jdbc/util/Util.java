package jm.task.core.jdbc.util;

import jm.task.core.jdbc.Main;
import org.hibernate.boot.model.relational.Database;

import java.net.URI;
import java.security.Provider;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/kataac";
    private static final String USER = "root";
    private static final String PASSWORD = "kataac";

    private Connection connection;

    public Util() {
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Соединение закрыто");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


// реализуйте настройку соеденения с БД
