package com.javid.repository;

import com.javid.domain.Cinema;
import com.javid.domain.Customer;
import com.javid.domain.Ticket;

import java.util.List;

/**
 * @author javid
 * Created on 1/1/2022
 */
public interface TicketRepository extends CrudRepository<Ticket, Integer> {

    List<Ticket> findUnexpired(Cinema cinema);

    List<Ticket> findAllUnexpired();

    boolean updateTicket(Customer customer, Ticket ticket, int number);
}
