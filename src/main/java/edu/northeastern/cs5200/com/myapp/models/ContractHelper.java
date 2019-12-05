package edu.northeastern.cs5200.com.myapp.models;

import java.io.Serializable;

public class ContractHelper implements Serializable {
  private int id;
  private String date;
  private String text;
  private String status;
  private String manager;
  private String artist;
  //private Event event;

  public ContractHelper(int id, String date, String text, String status, String manager, String artist) {
    this.id = id;
    this.date = date;
    this.text = text;
    this.status = status;
    this.manager = manager;
    this.artist = artist;
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

  public String getStatus() {
    return status;
  }

  public String getManager() {
    return manager;
  }

  public String getArtist() {
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

  public void setStatus(String status) {
    this.status = status;
  }

  public void setManager(String manager) {
    this.manager = manager;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

}
