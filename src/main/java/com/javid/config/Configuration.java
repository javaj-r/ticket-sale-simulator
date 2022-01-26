package com.javid.config;

import com.javid.control.CinemaController;
import com.javid.control.CustomerController;
import com.javid.control.TicketController;
import com.javid.control.UserController;
import com.javid.repository.*;
import com.javid.router.Router;

import java.sql.Connection;

/**
 * @author javid
 * Created on 1/2/2022
 */
public interface Configuration {

    UserController getUserController();

    Router getRouter();

    CinemaController getCinemaController();

    TicketController getTicketController();

    CustomerController getCustomerController();

    String getProperty(String key);

    Connection getConnection();

    UserRepository getUserRepository();

    PrivilegeRepository getPrivilegeRepository();

    CinemaRepository getCinemaRepository();

    TicketRepository getTicketRepository();

    CustomerRepository getCustomerRepository();
}
