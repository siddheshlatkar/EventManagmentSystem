package edu.northeastern.cs5200.com.myapp.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class Artist extends User implements Serializable {
  @Column(name = "genre")
  private String genre;


  public Artist(String userName, String password, String firstName, String lastName, String genre) {
    super(userName, password, firstName, lastName);
    this.genre = genre;

  }

  public Artist() {

  }

}
