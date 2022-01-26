package com.javid;

import com.javid.domain.Privilege;
import com.javid.domain.User;
import com.javid.repository.PrivilegeRepository;
import com.javid.repository.UserRepository;
import com.javid.repository.jdbc.PrivilegeRepositoryImpl;
import com.javid.repository.jdbc.UserRepositoryImpl;
import com.javid.view.console.Console;

import java.sql.*;

/**
 * @author javid
 * Created on 1/1/2022
 */
public class Application {

    public static void main(String[] args) {
//        addPrivileges();
//        setAdminUser();
//        setAdminUserPrivileges();
        new Console().mainMenu();
    }

   private static void addPrivileges() {
        PrivilegeRepository repository = PrivilegeRepositoryImpl.getInstance();
       for (Privilege.Values values: Privilege.Values.values()) {
        Privilege privilege = new Privilege();
        privilege.setName(values.name());
        repository.save(privilege);
       }
    }


    private static void setAdminUser() {
        UserRepository userRepository = UserRepositoryImpl.getInstance();
        User admin = new User()
                .setUsername("admin")
                .setPassword("admin")
                .setEnabled(true);

        Privilege p1 = new Privilege();
        Privilege p2 = new Privilege();
        p1.setName(Privilege.Values.ADMIN_LOGIN);
        p2.setName(Privilege.Values.CONFIRM_CINEMA);
        admin.getPrivileges().add(p1);
        admin.getPrivileges().add(p2);

        User savedUser = userRepository.save(admin);

        if (savedUser.isNew()) {
            System.out.println("Username already exists!");
        } else {
            System.out.println(savedUser.getId());
        }
    }

    private static void setAdminUserPrivileges() {
        UserRepository userRepository = UserRepositoryImpl.getInstance();
        User admin = userRepository.find(new User()
                .setUsername("admin")
                .setPassword("admin"));

        Privilege p1 = new Privilege();
        Privilege p2 = new Privilege();
        p1.setName(Privilege.Values.ADMIN_LOGIN);
        p2.setName(Privilege.Values.CONFIRM_CINEMA);
        admin.getPrivileges().add(p1);
        admin.getPrivileges().add(p2);

        userRepository.grantPrivileges(admin);
    }
}
