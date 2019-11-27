package edu.northeastern.cs5200.com.myapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import edu.northeastern.cs5200.com.myapp.models.Admin;
import edu.northeastern.cs5200.com.myapp.services.AdminService;

@RestController
public class AdminController {
  @Autowired
  private AdminService adminService;

  @PostMapping(path = "/api/admins", consumes = "application/json")
  public ResponseEntity<Admin> register(@RequestBody Admin admin, HttpSession session) {
    if (admin.getUserName() == null || admin.getPassword() == null || admin.getUserName().isEmpty() || admin.getPassword().isEmpty()) {
      return new ResponseEntity("User name of password can not be blank", HttpStatus.BAD_REQUEST);
    }
    Admin newAdmin = adminService.registerNewAdmin(admin);
    if (newAdmin == null) {
      return new ResponseEntity("Username is already taken", HttpStatus.BAD_REQUEST);
    }
    session.setAttribute("currentUserId", newAdmin.getId());
    session.setAttribute("currentUserType", "ADMIN");
    return new ResponseEntity(newAdmin, HttpStatus.OK);
  }

  @PostMapping(path = "api/admin/login", consumes = "application/json")
  public ResponseEntity<Admin> login(@RequestBody Admin artist, HttpSession session) {
    Admin existingAdmin = adminService.findByUserIDAndPassword(artist.getUserName(), artist.getPassword());
    if (existingAdmin == null) {
      return new ResponseEntity("Username or password is wrong", HttpStatus.BAD_REQUEST);
    }
    session.setAttribute("currentUserId", existingAdmin.getId());
    session.setAttribute("currentUserType", "ADMIN");
    return new ResponseEntity(existingAdmin, HttpStatus.OK);
  }

  @GetMapping(path = "api/admins/{id}/logout", consumes = "application/json")
  public ResponseEntity<String> logout(@PathVariable("id") int id, HttpSession session) {
    if (!validateId(id, session)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }
    session.invalidate();
    return new ResponseEntity("Successfully logged out.", HttpStatus.OK);
  }

  private boolean validateId(int id, HttpSession session) {
    if (session == null) {
      return false;
    }
    try {
      if (Integer.parseInt(session.getAttribute("currentUserId").toString()) != id ||
              !session.getAttribute("currentUserType").equals("ADMIN")) {
        return false;

      }
    } catch (Exception e) {
      return false;
    }
    return true;
  }
}
