package edu.northeastern.cs5200.com.myapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import edu.northeastern.cs5200.com.myapp.models.Ticket;
import edu.northeastern.cs5200.com.myapp.repositories.TicketRepository;

@Service
public class TicketService {
  @Autowired
  private TicketRepository ticketRepository;


  public Ticket findTicketById(int id) {
    Optional<Ticket> ticketOptional = ticketRepository.findById(id);

    if (ticketOptional.isPresent()) {
      return ticketOptional.get();
    }
    return null;
  }

  public Ticket save(Ticket ticket) {
    return ticketRepository.save(ticket);
  }

  public void delete(Ticket ticket) {
    ticketRepository.delete(ticket);
  }
}
