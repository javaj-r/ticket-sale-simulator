package com.javid.router;

import com.javid.domain.Cinema;
import com.javid.domain.Customer;
import com.javid.domain.Ticket;
import com.javid.domain.User;

import java.util.List;

/**
 * @author javid
 * Created on 1/4/2022
 */
public interface Router {

    User signUp(User user);

    User loginAsAdmin(User user);

    Cinema loginAsCinema(Cinema cinema);

    Customer loginAsCustomer(Customer customer);

    List<Cinema> getUnconfirmedCinemas();

    boolean isUsernameAvailable(String username);

    Cinema saveCinema(Cinema cinema);

    int confirmCinema(Cinema cinema);

    Ticket addNewTicket(Ticket ticket);

    List<Ticket> getUnexpiredTickets(Cinema cinema);

    void deleteTicket(Ticket ticket);

    Customer saveCustomer(Customer customer);

    List<Ticket> getAllTickets();

    boolean reserveTicket(Customer customer, Ticket ticket, int number);

    Ticket findTicket(Ticket ticket);
}
