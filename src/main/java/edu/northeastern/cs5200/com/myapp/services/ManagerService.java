package edu.northeastern.cs5200.com.myapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import edu.northeastern.cs5200.com.myapp.models.Manager;
import edu.northeastern.cs5200.com.myapp.repositories.ManagerRepository;

@RestController
public class ManagerService {

  @Autowired
  private ManagerRepository managerRepository;


  @PostMapping(path = "/api/managers", consumes = "application/json")
  public ResponseEntity<Manager> register(@RequestBody Manager manager, HttpSession session) {
    session.setAttribute("currentUser", manager);
    if ((manager.getUserName() == null || manager.getPassword() == null) || manager.getUserName().isEmpty() || manager.getPassword().isEmpty()) {
      return new ResponseEntity("User name or Password can not be blank", HttpStatus.BAD_REQUEST);
    }
    Manager existingManager = (Manager) managerRepository.findByUserName(manager.getUserName());
    if (existingManager != null) {
      return new ResponseEntity("Username is already taken", HttpStatus.BAD_REQUEST);
    }
    Manager registeredManager = managerRepository.save(((Manager)manager));
    return new ResponseEntity(registeredManager, HttpStatus.OK);
  }

  @PostMapping(path = "api/managers/login")
  public ResponseEntity<Manager> login(@RequestBody Manager manager, HttpSession session) {
    if ((manager.getUserName() == null || manager.getPassword() == null) || manager.getUserName().isEmpty() || manager.getPassword().isEmpty()) {
      return new ResponseEntity("User name or Password can not be blank", HttpStatus.BAD_REQUEST);
    }
    Manager existingManager = (Manager) managerRepository.findByUserNameAndPassword(manager.getUserName(), manager.getPassword());
    if (existingManager != null) {
      session.setAttribute("currentUSer", existingManager);
      return new ResponseEntity(existingManager, HttpStatus.OK);
    }
    return new ResponseEntity("Username or password is wrong", HttpStatus.BAD_REQUEST);
  }
}
