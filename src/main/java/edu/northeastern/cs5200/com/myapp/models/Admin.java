package edu.northeastern.cs5200.com.myapp.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class Admin extends User implements Serializable {


  public Admin(String userName, String password, String firstName, String lastName, String userType) {
    super(userName, password, firstName, lastName, userType);
  }

  public Admin(String userName, String password, String firstName, String lastName, String userType, String dob, String address, String email, String phone) {
    super(userName, password, firstName, lastName, userType, dob, address, email, phone);
  }

  public Admin() {

  }
}
