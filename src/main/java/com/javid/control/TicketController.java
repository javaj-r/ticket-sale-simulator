package com.javid.control;

import com.javid.domain.Cinema;
import com.javid.domain.Customer;
import com.javid.domain.Ticket;

import java.util.List;

/**
 * @author javid
 * Created on 1/1/2022
 */
public interface TicketController {

    Ticket saveNewTicket(Ticket ticket);

    List<Ticket> getUnexpiredTickets(Cinema cinema);

    void deleteTicket(Ticket ticket);

    List<Ticket> getAllUnexpired();

    boolean reserveTicket(Customer customer, Ticket ticket, int number);

    Ticket findTicketById(Ticket ticket);
}
