package edu.northeastern.cs5200.com.myapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "events")
public class Event {

  @Id
  private int id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "capacity")
  private int capacity;

  @Column(name = "date")
  private String date;

  @OneToOne
  @JsonIgnore
  private Contract contract;

  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Ticket> tickets;

  public Event(String name, String description, int capacity, String date, Contract contract) {
    this.name = name;
    this.description = description;
    this.capacity = capacity;
    this.date  = date;
    this.contract = contract;
  }

  public Event() {
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public int getCapacity() {
    return capacity;
  }

  public Contract getContract() {
    return contract;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public void setContract(Contract contract) {
    this.contract = contract;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public void addTicket(Ticket ticket) {
    this.tickets.add(ticket);
  }
}
