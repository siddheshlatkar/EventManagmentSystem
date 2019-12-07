package edu.northeastern.cs5200.com.myapp.models;

import javax.persistence.Column;

public class TicketHelper {
  private int id;
  private int seat;
  private int eventId;
  private String eventName;
  private String eventDate;

  public TicketHelper(int id, int seat, int eventId, String eventName, String eventDate) {
    this.id = id;
    this.seat = seat;
    this.eventId = eventId;
    this.eventName = eventName;
    this.eventDate = eventDate;
  }

  public int getId() {
    return id;
  }

  public int getSeat() {
    return seat;
  }

  public int getEventId() {
    return eventId;
  }

  public String getEventName() {
    return eventName;
  }

  public String getEventDate() {
    return eventDate;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setSeat(int seat) {
    this.seat = seat;
  }

  public void setEventId(int eventId) {
    this.eventId = eventId;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public void setEventDate(String eventDate) {
    this.eventDate = eventDate;
  }
}
