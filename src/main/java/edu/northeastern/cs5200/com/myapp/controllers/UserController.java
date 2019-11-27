package edu.northeastern.cs5200.com.myapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpSession;

import edu.northeastern.cs5200.com.myapp.models.Admin;
import edu.northeastern.cs5200.com.myapp.models.Artist;
import edu.northeastern.cs5200.com.myapp.models.Contract;
import edu.northeastern.cs5200.com.myapp.models.Manager;
import edu.northeastern.cs5200.com.myapp.models.User;
import edu.northeastern.cs5200.com.myapp.services.AdminService;
import edu.northeastern.cs5200.com.myapp.services.ArtistService;
import edu.northeastern.cs5200.com.myapp.services.ContractService;
import edu.northeastern.cs5200.com.myapp.services.ManagerService;
import edu.northeastern.cs5200.com.myapp.services.UserService;

@RestController
public class UserController {
  @Autowired
  private UserService userService;

  @Autowired
  private ManagerService managerService;

  @Autowired
  private AdminService adminService;

  @Autowired
  private ArtistService artistService;

  @Autowired
  private ContractService contractService;

  @PostMapping(path = "/api/users", consumes = "application/json")
  public ResponseEntity<User> register(@RequestBody User user, HttpSession session) {

    if (user.getUserName() == null || user.getPassword() == null || user.getUserName().isEmpty()
            || user.getPassword().isEmpty() || user.getUserType() == null || user.getUserType().isEmpty()) {
      return new ResponseEntity("User name or password or user type can not be blank", HttpStatus.BAD_REQUEST);
    }

    User newUser = null;

    if (user.getUserType().equals("Manager")) {

      newUser = managerService.registerNewManager(new Manager(user.getUserName(), user.getPassword(),
              user.getFirstName(), user.getLastName(), user.getUserType(), user.getDob(), user.getAddress(), user.getEmail(), user.getPhone()));
      session.setAttribute("currentUserType", "MANAGER");

    } else if (user.getUserType().equals("Artist")) {

      newUser = artistService.registerNewArtist(new Artist(user.getUserName(), user.getPassword(),
              user.getFirstName(), user.getLastName(), user.getUserType(), user.getDob(), user.getAddress(), user.getEmail(), user.getPhone()));
      session.setAttribute("currentUserType", "ARTIST");

    } else if (user.getUserType().equals("Admin")) {

      newUser = adminService.registerNewAdmin(new Admin(user.getUserName(), user.getPassword(),
              user.getFirstName(), user.getLastName(), user.getUserType(), user.getDob(), user.getAddress(), user.getEmail(), user.getPhone()));
      session.setAttribute("currentUserType", "ADMIN");

    } else if (user.getUserType().equals("User")) {
      newUser = userService.registerNewUser(user);
      session.setAttribute("currentUserType", "USER");
    } else {
      return new ResponseEntity("Invalid user type passed.", HttpStatus.BAD_REQUEST);
    }

    if (newUser == null) {
      return new ResponseEntity("Username is already taken", HttpStatus.BAD_REQUEST);
    }
    session.setAttribute("currentUserId", newUser.getId());
    return new ResponseEntity(newUser, HttpStatus.OK);
  }

  @PostMapping(path = "api/user/login", consumes = "application/json")
  public ResponseEntity<Artist> login(@RequestBody User user, HttpSession session) {
    if (user.getUserType() == null) {
      return new ResponseEntity("User Type can not be null", HttpStatus.BAD_REQUEST);
    }
    User existingUser = null;
    if (user.getUserType().equals("Manager")) {
      existingUser = managerService.findByUserIDAndPassword(user.getUserName(), user.getPassword());
      session.setAttribute("currentUserType", "MANAGER");
    } else if (user.getUserType().equals("Artist")) {

      existingUser = artistService.findByUserIDAndPassword(user.getUserName(), user.getPassword());
      session.setAttribute("currentUserType", "ARTIST");

    } else if (user.getUserType().equals("Admin")) {

      existingUser = adminService.findByUserIDAndPassword(user.getUserName(), user.getPassword());
      session.setAttribute("currentUserType", "ADMIN");

    } else if (user.getUserType().equals("User")) {

      existingUser = userService.findByUserIDAndPassword(user.getUserName(), user.getPassword());
      session.setAttribute("currentUserType", "USER");

    } else {
      return new ResponseEntity("Invalid user type passed.", HttpStatus.BAD_REQUEST);
    }

    if (existingUser == null) {
      return new ResponseEntity("Username or password is wrong", HttpStatus.BAD_REQUEST);
    }
    session.setAttribute("currentUserId", existingUser.getId());
    return new ResponseEntity(existingUser, HttpStatus.OK);
  }

  @GetMapping(path = "api/users/{id}/logout", consumes = "application/json")
  public ResponseEntity<String> logout(@PathVariable("id") int id, HttpSession session) {
    if (!validateId(id, session)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }
    session.invalidate();
    return new ResponseEntity("Successfully logged out.", HttpStatus.OK);
  }


  @GetMapping(path = "api/users/{id}/profile", consumes = "application/json")
  public ResponseEntity<User> profile(@PathVariable("id") int id, HttpSession session) {
    if (!validateId(id, session)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }
    User user = userService.findUserByID(id);
    return new ResponseEntity(user, HttpStatus.OK);
  }

  private boolean validateId(int id, HttpSession session) {
    if (session == null) {
      return false;
    }
    try {
      if (Integer.parseInt(session.getAttribute("currentUserId").toString()) != id) {
        return false;
      }
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  @PostMapping(path = "api/users/{id}/creatUser", consumes = "application/json")
  public ResponseEntity<User> createUser(@PathVariable("id") int id, @RequestBody User user, HttpSession session) {
    if (!validateId(id, session)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }
    User admin = userService.findUserByID(id);
    if (!admin.getUserType().equals("Admin")) {
      return new ResponseEntity("Please login as a admin.", HttpStatus.BAD_REQUEST);
    }

    if (user.getUserName() == null || user.getPassword() == null || user.getUserName().isEmpty()
            || user.getPassword().isEmpty() || user.getUserType() == null || user.getUserType().isEmpty()) {
      return new ResponseEntity("User name or password or user type can not be blank", HttpStatus.BAD_REQUEST);
    }

    User newUser = null;

    if (user.getUserType().equals("Manager")) {

      newUser = managerService.registerNewManager(new Manager(user.getUserName(), user.getPassword(),
              user.getFirstName(), user.getLastName(), user.getUserType(), user.getDob(), user.getAddress(), user.getEmail(), user.getPhone()));


    } else if (user.getUserType().equals("Artist")) {

      newUser = artistService.registerNewArtist(new Artist(user.getUserName(), user.getPassword(),
              user.getFirstName(), user.getLastName(), user.getUserType(), user.getDob(), user.getAddress(), user.getEmail(), user.getPhone()));


    } else if (user.getUserType().equals("Admin")) {

      newUser = adminService.registerNewAdmin(new Admin(user.getUserName(), user.getPassword(),
              user.getFirstName(), user.getLastName(), user.getUserType(), user.getDob(), user.getAddress(), user.getEmail(), user.getPhone()));


    } else if (user.getUserType().equals("User")) {
      newUser = userService.registerNewUser(user);

    } else {
      return new ResponseEntity("Invalid user type passed.", HttpStatus.BAD_REQUEST);
    }

    if (newUser == null) {
      return new ResponseEntity("Username is already taken", HttpStatus.BAD_REQUEST);
    }

    session.setAttribute("currentUserId", id);
    return new ResponseEntity(newUser, HttpStatus.OK);
  }



  @PostMapping(path = "api/users/{id}/deleteUser/{userToBeDeleted}", consumes = "application/json")
  public ResponseEntity<String> deleteUser(@PathVariable("id") int id, @PathVariable("userToBeDeleted") User user, HttpSession session) {
    if (!validateId(id, session)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    User admin = userService.findUserByID(id);
    if (!admin.getUserType().equals("Admin")) {
      return new ResponseEntity("Please login as a admin.", HttpStatus.BAD_REQUEST);
    }

    if (user.getId() == null) {
      return new ResponseEntity("Please pass user id to be deleted", HttpStatus.BAD_REQUEST);
    }

    User existingUser = userService.findUserByID(user.getId());

    session.setAttribute("currentUserId", id);

    if (existingUser != null) {
      userService.deleteUserById(user.getId());
      return new ResponseEntity("Successfully deleted user", HttpStatus.OK);
    } else {
      return new ResponseEntity("User does not exist", HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(path = "api/users/{id}/listManagers", consumes = "application/json")
  public ResponseEntity<String> listManagers(@PathVariable("id") int id, HttpSession session) {
    if (!validateId(id, session)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    User admin = userService.findUserByID(id);
    if (!admin.getUserType().equals("Admin")) {
      return new ResponseEntity("Please login as a admin.", HttpStatus.BAD_REQUEST);
    }

    session.setAttribute("currentUserId", id);

    List<Manager> managers = managerService.findAllManagers();
    return new ResponseEntity(managers, HttpStatus.OK);
  }

  @GetMapping(path = "api/users/{id}/listArtists", consumes = "application/json")
  public ResponseEntity<String> listArtists(@PathVariable("id") int id, HttpSession session) {
    if (!validateId(id, session)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    User admin = userService.findUserByID(id);
    if (!admin.getUserType().equals("Admin")) {
      return new ResponseEntity("Please login as a admin.", HttpStatus.BAD_REQUEST);
    }

    session.setAttribute("currentUserId", id);

    List<Artist> artists = artistService.findAllArtists();
    return new ResponseEntity(artists, HttpStatus.OK);
  }

  @GetMapping(path = "api/users/{id}/listUsers", consumes = "application/json")
  public ResponseEntity<String> listUsers(@PathVariable("id") int id, HttpSession session) {
    if (!validateId(id, session)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    User admin = userService.findUserByID(id);
    if (!admin.getUserType().equals("Admin")) {
      return new ResponseEntity("Please login as a admin.", HttpStatus.BAD_REQUEST);
    }

    session.setAttribute("currentUserId", id);

    List<User> users = userService.findAllUsers();

    return new ResponseEntity(users, HttpStatus.OK);
  }

  @PostMapping(path = "api/users/{id}/request/artists/{artistId}", consumes = "application/json")
  public ResponseEntity<Contract> request(@PathVariable("id") int id, @PathVariable("artistId") int artistId, @RequestBody Contract contract, HttpSession session) {
    if (!validateId(id, session)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    Manager manager = managerService.findManagerByID(id);
    if (manager == null) {
      return new ResponseEntity("Please login as a manager.", HttpStatus.BAD_REQUEST);
    }

    Artist artist = artistService.findArtistByID(artistId);

    if (artist == null) {
      return new ResponseEntity("Artist does not exist", HttpStatus.BAD_REQUEST);
    }

    Contract newContract = managerService.createContract(manager, artist, contract.getText());

    session.setAttribute("currentUserId", id);

    return new ResponseEntity(newContract, HttpStatus.OK);
  }

  @GetMapping(path = "api/users/{id}/contracts", consumes = "application/json")
  public ResponseEntity<List<Contract>> getContracts(@PathVariable("id") int id, HttpSession session) {
    if (!validateId(id, session)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    User user = userService.findUserByID(id);
    if (user == null) {
      return new ResponseEntity("Please login as a manager.", HttpStatus.BAD_REQUEST);
    }
    List<Contract> contracts = null;
    if (user.getUserType().equals("Manager")) {
      contracts = managerService.getContracts(user.getId());
    } else {
      contracts = artistService.getContracts(user.getId());
    }

    session.setAttribute("currentUserId", id);

    return new ResponseEntity(contracts, HttpStatus.OK);
  }

  @PostMapping("api/users/{id}/contracts/{contractId}/accept")
  public ResponseEntity<Contract> acceptContract(@PathVariable("id") int id, @PathVariable("contractId") int contractId, HttpSession session) {
    if (!validateId(id, session)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    Artist artist = artistService.findArtistByID(id);

    if (artist == null) {
      return new ResponseEntity("Please login as an artist.", HttpStatus.BAD_REQUEST);
    }

    Contract contract = contractService.findContractById(contractId);

    if (contract == null) {
      return new ResponseEntity("Invalid contract id", HttpStatus.BAD_REQUEST);
    }

    contract = contractService.acceptContract(contractId);

    return new ResponseEntity(contract, HttpStatus.OK);
  }

  @PostMapping("api/users/{id}/contracts/{contractId}/reject")
  public ResponseEntity<Contract> rejectContract(@PathVariable("id") int id, @PathVariable("contractId") int contractId, HttpSession session) {
    if (!validateId(id, session)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    Artist artist = artistService.findArtistByID(id);

    if (artist == null) {
      return new ResponseEntity("Please login as an artist.", HttpStatus.BAD_REQUEST);
    }

    Contract contract = contractService.findContractById(contractId);

    if (contract == null) {
      return new ResponseEntity("Invalid contract id", HttpStatus.BAD_REQUEST);
    }

    contract = contractService.rejectContract(contractId);

    return new ResponseEntity(contract, HttpStatus.OK);
  }
}
