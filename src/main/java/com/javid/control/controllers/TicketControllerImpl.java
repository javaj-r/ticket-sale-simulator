package com.javid.control.controllers;

import com.javid.config.Configurable;
import com.javid.control.TicketController;
import com.javid.domain.Cinema;
import com.javid.domain.Customer;
import com.javid.domain.Ticket;
import com.javid.repository.TicketRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javid
 * Created on 1/5/2022
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TicketControllerImpl implements TicketController, Configurable {

    TicketRepository ticketRepository = CONFIG.getTicketRepository();

    private static class Singleton {
        private static final TicketController INSTANCE = new TicketControllerImpl();
    }

    public static TicketController getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public Ticket saveNewTicket(Ticket ticket) {
        Cinema cinema = ticket.getCinema();
        if (cinema == null || cinema.isNew())
            return ticket;

        Ticket savedTicket = ticketRepository.save(ticket);
        if (savedTicket.isNew())
            return savedTicket;

        savedTicket.getCinema().getTickets().add(savedTicket);

        return savedTicket;
    }

    @Override
    public List<Ticket> getUnexpiredTickets(Cinema cinema) {
        if (cinema == null || cinema.isNew())
            return new ArrayList<>();

        return ticketRepository.findUnexpired(cinema);
    }

    @Override
    public void deleteTicket(Ticket ticket) {
        if (ticket==null || ticket.isNew())
            return;
         ticketRepository.deleteById(ticket.getId());
    }

    @Override
    public List<Ticket> getAllUnexpired() {
        return ticketRepository.findAllUnexpired().stream().toList();
    }

    @Override
    public boolean reserveTicket(Customer customer, Ticket ticket, int number) {
        boolean updated =ticketRepository.updateTicket(customer, ticket,  number);
        if (updated)
            customer.getTickets().add(ticket);
        return updated;
    }

    @Override
    public Ticket findTicketById(Ticket ticket) {
        if (ticket.isNew())
            return ticket;
        return ticketRepository.findById(ticket.getId());
    }
}
