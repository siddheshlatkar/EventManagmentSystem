package edu.northeastern.cs5200.com.myapp.repositories;

import org.springframework.data.repository.CrudRepository;

import edu.northeastern.cs5200.com.myapp.models.Ticket;

public interface TicketRepository extends CrudRepository<Ticket, Integer> {
}
