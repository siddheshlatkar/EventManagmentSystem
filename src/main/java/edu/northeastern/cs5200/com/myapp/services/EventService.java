package edu.northeastern.cs5200.com.myapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.cs5200.com.myapp.models.Event;
import edu.northeastern.cs5200.com.myapp.models.Ticket;
import edu.northeastern.cs5200.com.myapp.models.User;
import edu.northeastern.cs5200.com.myapp.repositories.EventRepository;
import edu.northeastern.cs5200.com.myapp.repositories.TicketRepository;

@Service
public class EventService {
  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private TicketRepository ticketRepository;

  public Event findByID(int id) {
    return eventRepository.findById(id).get();
  }

  public Ticket bookTicket(User user, Event event) {
    Ticket ticket = new Ticket(event.getCapacity() - 1, event, user);
    event.setCapacity(event.getCapacity() - 1);
    event.addTicket(ticket);
    return ticketRepository.save(ticket);
  }
}
