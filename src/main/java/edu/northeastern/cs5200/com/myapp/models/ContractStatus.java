package edu.northeastern.cs5200.com.myapp.models;

public enum ContractStatus {
  REQUESTED("Requested"), ACCEPTED("Accepted"), REJECTED("Rejected");

  private String status;

   ContractStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
     return this.status;
  }
}
