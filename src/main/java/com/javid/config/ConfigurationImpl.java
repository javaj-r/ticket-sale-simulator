package com.javid.config;

import com.javid.control.CinemaController;
import com.javid.control.CustomerController;
import com.javid.control.TicketController;
import com.javid.control.UserController;
import com.javid.control.controllers.CinemaControllerImpl;
import com.javid.control.controllers.CustomerControllerImpl;
import com.javid.control.controllers.TicketControllerImpl;
import com.javid.control.controllers.UserControllerImpl;
import com.javid.exception.PropertiesException;
import com.javid.repository.*;
import com.javid.repository.jdbc.*;
import com.javid.router.Router;
import com.javid.router.RouterImpl;
import com.javid.util.Property;
import com.javid.util.Screen;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author javid
 * Created on 1/2/2022
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigurationImpl implements Configuration {

    private final Properties properties = Property.getProperties();
    private Connection connection;

    private static class Singleton {
        private static final ConfigurationImpl INSTANCE = new ConfigurationImpl();
    }

    public static ConfigurationImpl getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String getProperty(String key) {
        if (this.properties == null) {
            throw new PropertiesException("Property file not found!");
        } else if (!this.properties.containsKey(key)) {
            throw new PropertiesException("Property key [" + key +"] not found!");
        } else {
            return properties.get(key).toString();
        }
    }

    @Override
    public Connection getConnection() {
        if (this.connection == null) {
            setConnection();
        }
        return connection;
    }

    @Override
    public UserRepository getUserRepository() {
        return UserRepositoryImpl.getInstance();
    }

    @Override
    public PrivilegeRepository getPrivilegeRepository() {
        return PrivilegeRepositoryImpl.getInstance();
    }

    @Override
    public CinemaRepository getCinemaRepository() {
        return CinemaRepositoryImpl.getInstance();
    }

    @Override
    public TicketRepository getTicketRepository() {
        return TicketRepositoryImpl.getInstance();
    }

    @Override
    public CustomerRepository getCustomerRepository() {
        return CustomerRepositoryImpl.getInstance();
    }

    private void setConnection() {
        try {
            String database = getProperty("db.name");
            String url = getProperty("db.url") + database;
            String username = getProperty("db.username");
            String password = getProperty("db.password");
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (PropertiesException e) {
            Screen.printError(e.getMessage());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserController getUserController() {
        return UserControllerImpl.getInstance();
    }

    @Override
    public Router getRouter() {
        return RouterImpl.getInstance();
    }

    @Override
    public CinemaController getCinemaController() {
        return CinemaControllerImpl.getInstance();
    }

    @Override
    public TicketController getTicketController() {
        return TicketControllerImpl.getInstance();
    }

    @Override
    public CustomerController getCustomerController() {
        return CustomerControllerImpl.getInstance();
    }
}
