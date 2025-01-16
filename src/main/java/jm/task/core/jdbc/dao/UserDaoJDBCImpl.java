package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private Connection connection= Util.getConnection();

    public UserDaoJDBCImpl() {
    }


    @Override
    public void createUsersTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50), " +
                "lastName VARCHAR(50), " +
                "age TINYINT)";

        if (connection == null) {
            System.out.println("Oshibka");
            return;
        }
        try (Statement statement3 = connection.createStatement()) {
            statement3.executeUpdate(createTable);
            System.out.println("Таблица создана");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void dropUsersTable() {
        String dropTable = "DROP TABLE IF EXISTS users;";

        try (Statement statement1 = connection.createStatement()) {
            statement1.executeUpdate(dropTable);
            System.out.println("Table drop");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        String userSaver = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)"; //здесь

        try (PreparedStatement stmt = connection.prepareStatement(userSaver, Statement.RETURN_GENERATED_KEYS)) {
            if(connection == null) {
                connection.setAutoCommit(false);
            }else throw new SQLException("Соединение не установлено");

            stmt.setString(1, name);
            stmt.setString(2, lastName);
            stmt.setByte(3, age);
            stmt.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException roll) {
                    roll.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if(connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void removeUserById(long id) {
        String deleteUser = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt1 = connection.prepareStatement(deleteUser)) {
            connection.setAutoCommit(false);
            stmt1.setLong(1, id);
            stmt1.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }
        } finally {
            try {
                connection.setAutoCommit(true);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String selectUser = "SELECT * FROM users";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(selectUser)
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    @Override
    public void cleanUsersTable() {
        String sql6 = "TRUNCATE TABLE users;";
        try (PreparedStatement stmt2 = connection.prepareStatement(sql6)) {
            stmt2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
