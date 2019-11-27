package edu.northeastern.cs5200.com.myapp.models;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class Manager extends User implements Serializable {

  @Column(name = "experience")
  private Integer experience;

  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Contract> contracts;

  public Manager(String userName, String password, String firstName, String lastName, String userType) {
    super(userName, password, firstName, lastName, userType);
  }


  public Manager() {

  }

  public Manager(String userName, String password, String firstName, String lastName, String userType, String dob, String address, String email, String phone) {
    super(userName, password, firstName, lastName, userType, dob, address, email, phone);
  }

  public void addContract(Contract contract) {
    this.contracts.add(contract);
  }


  public Integer getExperience() {
    return experience;
  }

  public List<Contract> getContracts() {
    return contracts;
  }


  public void setContracts(List<Contract> contracts) {
    this.contracts = contracts;
  }
}
