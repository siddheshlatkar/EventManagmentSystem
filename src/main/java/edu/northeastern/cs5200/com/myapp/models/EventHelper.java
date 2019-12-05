package edu.northeastern.cs5200.com.myapp.models;

public class EventHelper {
  private int id;
  private String name;
  private String description;
  private int capacity;
  private String date;
  private boolean externalEvent;
  private int contractId;

  public EventHelper(int id, String name, String description, int capacity, String date, boolean externalEvent, int contractId) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.capacity = capacity;
    this.date = date;
    this.contractId = contractId;
    this.externalEvent = externalEvent;
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

  public String getDate() {
    return date;
  }

  public int getContractId() {
    return contractId;
  }

  public boolean isExternalEvent() {
    return externalEvent;
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

  public void setDate(String date) {
    this.date = date;
  }

  public void setExternalEvent(boolean externalEvent) {
    this.externalEvent = externalEvent;
  }

  public void setContractId(int contractId) {
    this.contractId = contractId;
  }
}
