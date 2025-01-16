package jm.task.core.jdbc;

import com.google.protobuf.Service;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;


import java.security.Provider;
import java.sql.Connection;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        UserServiceImpl userService = new UserServiceImpl();
        System.out.println("Таблица создана");

        userService.createUsersTable();
         userService.saveUser("Иван", "Иванов", (byte) 25);
        userService.saveUser("Петр", "Петров", (byte) 30);
        userService.saveUser("Анна", "Аннова", (byte) 22);
        userService.saveUser("Елена", "Еленова", (byte) 28);
        userService.removeUserById(0);

        List<User> users = userService.getAllUsers();
        System.out.println("Список: ");
        for (User user : users) {
           System.out.println(user.toString());
        }


        userService.cleanUsersTable();
        System.out.println("Таблица очищена");
        userService.dropUsersTable();
        System.out.println("Таблица удалена");

        Util.closeConnection();

    }
}

