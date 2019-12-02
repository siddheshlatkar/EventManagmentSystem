package edu.northeastern.cs5200.com.myapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.cs5200.com.myapp.models.Admin;
import edu.northeastern.cs5200.com.myapp.repositories.AdminRepository;

@Service
public class AdminService {

  @Autowired
  AdminRepository adminRepository;


  public Admin registerNewAdmin(Admin admin) {
    return adminRepository.save(admin);
  }

  public Admin findByUserIDAndPassword(String userName, String password) {
    return adminRepository.findByUserNameAndPassword(userName, password);
  }

  public Admin findById(int id) {
    return adminRepository.findById(id).get();
  }
}
