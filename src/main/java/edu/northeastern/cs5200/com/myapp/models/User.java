package edu.northeastern.cs5200.com.myapp.models;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "user_name")
  private String userName;



  @Column(name = "password")
  private String password;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "dob")
  private String dob;

  @Column(name = "address")
  private String address;

  @Column(name = "phone")
  private String phone;

  @Column(name = "email")
  private String email;

  @Column(name = "user_type")
  private String userType;


  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Ticket> tickets;

  public User(String userName, String password, String firstName, String lastName, String userType) {
    this.userName = userName;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.userType = userType;
  }

  public User(String userName, String password, String firstName, String lastName, String userType, String dob, String address, String email, String phone) {
    this.userName = userName;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.userType = userType;
    this.dob = dob;
    this.address = address;
    this.phone = phone;
    this.email = email;
  }

  public User() {

  }

  public String getUserName() {
    return userName;
  }

  public String getPassword() {
    return password;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }

  public void setDob(String dob) {
    this.dob = dob;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getDob() {
    return dob;
  }

  public String getAddress() {
    return address;
  }

  public String getPhone() {
    return phone;
  }

  public String getEmail() {
    return email;
  }

  public List<Ticket> getTickets() {
    return tickets;
  }

  public void setTickets(List<Ticket> tickets) {
    this.tickets = tickets;
  }
}

