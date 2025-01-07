package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.Main;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;
import org.hibernate.boot.model.relational.Database;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private Connection connection;

    public UserDaoJDBCImpl() {
        Util util = new Util();
        this.connection = util.getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createUsersTable() {
        String sql1 = "CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50), " +
                "lastName VARCHAR(50), " +
                "age TINYINT)";


        try (Statement statement3 = connection.createStatement()) {
            statement3.executeUpdate(sql1);
            System.out.println("Таблица создана");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void dropUsersTable() {
        String sql2 = "DROP TABLE IF EXISTS users;";

        try (Statement statement1 = connection.createStatement()) {
            statement1.executeUpdate(sql2);
            System.out.println("Table drop");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {

        String sql3 = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)"; //здесь

        try (PreparedStatement stmt = connection.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, lastName);
            stmt.setByte(3, age);
            stmt.executeUpdate();

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException roll) {
                    roll.printStackTrace();
                }
            }
            e.printStackTrace();
        }
    }


    public void removeUserById(long id) {
        String sql4 = "DELETE FROM users WHERE id = ?"; //здесь
        try (PreparedStatement stmt1 = connection.prepareStatement(sql4)) {
            stmt1.setLong(1, id);
            stmt1.executeUpdate();

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql5 = "SELECT * FROM users";


        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql5)
        ) {
            while (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String lastName = rs.getString("lastName");
                Byte age = rs.getByte("age");

                User user = new User(name, lastName, age);
                user.setId(id);
                users.add(user);
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    public void cleanUsersTable() {
        String sql6 = "TRUNCATE TABLE users;";
        try (PreparedStatement stmt2 = connection.prepareStatement(sql6)) {
            stmt2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
