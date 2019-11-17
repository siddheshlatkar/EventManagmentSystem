package edu.northeastern.cs5200.com.myapp.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class Manager extends User implements Serializable {

  @Column(name = "experience")
  private Integer experience;

  public Manager(String userName, String password, String firstName, String lastName, Integer gradYear, long scholarship) {
    super(userName, password, firstName, lastName);
  }

  public Manager() {

  }
}
