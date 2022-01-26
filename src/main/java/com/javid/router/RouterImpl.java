package com.javid.router;

import com.javid.config.Configurable;
import com.javid.control.TicketController;
import com.javid.control.CinemaController;
import com.javid.control.CustomerController;
import com.javid.control.UserController;
import com.javid.domain.Cinema;
import com.javid.domain.Customer;
import com.javid.domain.Ticket;
import com.javid.domain.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author javid
 * Created on 1/4/2022
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RouterImpl implements Router, Configurable {

    UserController userController = CONFIG.getUserController();
    CinemaController cinemaController = CONFIG.getCinemaController();
    TicketController tickerController = CONFIG.getTicketController();
    CustomerController customerController = CONFIG.getCustomerController();

    private static class Singleton {
        private static final Router INSTANCE = new RouterImpl();
    }

    public static Router getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public User signUp(User user) {
        return null;
    }

    @Override
    public User loginAsAdmin(User user) {
        return userController.getAdminUser(user);
    }

    @Override
    public Cinema loginAsCinema(Cinema cinema) {
        return cinemaController.getCinema(cinema);
    }

    @Override
    public Customer loginAsCustomer(Customer customer) {
        return customerController.getCustomer(customer);
    }

    @Override
    public List<Cinema> getUnconfirmedCinemas() {
        return cinemaController.getUnconfirmedCinemas();
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return userController.isUsernameAvailable(username);
    }

    @Override
    public Cinema saveCinema(Cinema cinema) {
        return cinemaController.register(cinema);
    }

    @Override
    public int confirmCinema(Cinema cinema) {
        return userController.enableUser(cinema);
    }

    @Override
    public Ticket addNewTicket(Ticket ticket) {
        return tickerController.saveNewTicket(ticket);
    }

    @Override
    public List<Ticket> getUnexpiredTickets(Cinema cinema) {
        return tickerController.getUnexpiredTickets(cinema);
    }

    @Override
    public void deleteTicket(Ticket ticket) {
        tickerController.deleteTicket(ticket);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerController.register(customer);
    }

    @Override
    public List<Ticket> getAllTickets() {
        return tickerController.getAllUnexpired();
    }

    @Override
    public boolean reserveTicket(Customer customer, Ticket ticket, int number) {
        return tickerController.reserveTicket(customer, ticket, number);
    }

    @Override
    public Ticket findTicket(Ticket ticket) {
        return tickerController.findTicketById(ticket);
    }


}
