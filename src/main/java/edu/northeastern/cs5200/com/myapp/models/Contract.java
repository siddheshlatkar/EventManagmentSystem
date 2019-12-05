package edu.northeastern.cs5200.com.myapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "contracts")
public class Contract {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "date")
  private String date;

  @Column(name = "text")
  private String text;

  @Column(name = "status")
  private String status;

  @ManyToOne
  @JsonIgnore
  @JoinColumn
  private Manager manager;

  @ManyToOne
  @JsonIgnore
  @JoinColumn
  private Artist artist;

  @OneToOne(mappedBy = "contract")
  private Event event;

  public Contract() {

  }

  public Contract(Manager manager, Artist artist, String date, String text, String status) {
    this.artist = artist;
    this.manager = manager;
    this.date = date;
    this.text = text;
    this.status = status;
  }

  public int getId() {
    return id;
  }

  public String getDate() {
    return date;
  }

  public String getText() {
    return text;
  }

  public Manager getManager() {
    return manager;
  }

  public Artist getArtist() {
    return artist;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setManager(Manager manager) {
    this.manager = manager;
  }

  public void setArtist(Artist artist) {
    this.artist = artist;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Event getEvent() {
    return event;
  }

  public void setEvent(Event event) {
    this.event = event;
  }
}
