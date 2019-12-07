package edu.northeastern.cs5200.com.myapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tickets")
public class Ticket {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "seat")
  private int seat;

  @ManyToOne
  @JsonIgnore
  @JoinColumn
  private Event event;

  @ManyToOne
  @JsonIgnore
  @JoinColumn
  private User user;

  public Ticket(int seat, Event event, User user) {
    this.seat = seat;
    this.event = event;
    this.user = user;
  }

  public Ticket() {}

  public int getId() {
    return id;
  }

  public int getSeat() {
    return seat;
  }

  public Event getEvent() {
    return event;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setSeat(int seat) {
    this.seat = seat;
  }

  public void setEvent(Event event) {
    this.event = event;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
