package edu.northeastern.cs5200.com.myapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.northeastern.cs5200.com.myapp.models.Admin;
import edu.northeastern.cs5200.com.myapp.models.Artist;
import edu.northeastern.cs5200.com.myapp.models.Contract;
import edu.northeastern.cs5200.com.myapp.models.Event;
import edu.northeastern.cs5200.com.myapp.models.Manager;
import edu.northeastern.cs5200.com.myapp.models.User;
import edu.northeastern.cs5200.com.myapp.services.AdminService;
import edu.northeastern.cs5200.com.myapp.services.ArtistService;
import edu.northeastern.cs5200.com.myapp.services.ContractService;
import edu.northeastern.cs5200.com.myapp.services.ManagerService;
import edu.northeastern.cs5200.com.myapp.services.UserService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
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

  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @PostMapping(path = "/api/users" )
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

  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @PostMapping(path = "api/user/login" )
  public ResponseEntity login(@RequestBody User user, HttpServletResponse response) {

    User loggedUser = userService.findByUserIDAndPassword(user.getUserName(), user.getPassword());


    if (loggedUser == null) {
      return new ResponseEntity("Username or password is wrong", HttpStatus.BAD_REQUEST);
    }
    //System.out.println("*****ID " + loggedUser.getId() + "name  " + loggedUser.getUserName());


    Cookie cookie = new Cookie("currentUser", loggedUser.getId().toString());
    cookie.setPath("/");

    response.addCookie(cookie);

//    ResponseEntity responseEntity = ResponseEntity.ok()
//            .header("currentUser", loggedUser.getId().toString())
//            .body(loggedUser);

    ResponseEntity responseEntity = new ResponseEntity(loggedUser, HttpStatus.OK);


    //session.setAttribute("currentUserId", loggedUser.getId());
    //return new ResponseEntity(loggedUser, HttpStatus.OK);
    return responseEntity;
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @GetMapping(path = "api/users/{id}/logout" )
  public ResponseEntity<String> logout(@PathVariable("id") int id, HttpSession session) {
    if (!validateId(id, session)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }
    session.invalidate();

    return new ResponseEntity("Successfully logged out.", HttpStatus.OK);
  }


  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @GetMapping(path = "api/users/{id}/profile" )
  public ResponseEntity<User> profile(@PathVariable("id") Integer id, HttpServletRequest req) {
    //System.out.println("****Cookie Value" + req.getCookies()[0].getValue());
    //System.out.println("****Cookie name" + req.getCookies()[0].getName());


    if (req.getCookies() == null) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    String cookieValue = Arrays.stream(req.getCookies())
            .filter(c -> c.getName().equals("currentUser"))
            .findFirst()
            .map(Cookie::getValue)
            .orElse(null);


    if (cookieValue == null) {
      return new ResponseEntity("Can't get cookie with namecurrentUser", HttpStatus.BAD_REQUEST);
    }

    if (!cookieValue.equals("" + id)) {
      return new ResponseEntity("id and cookie value does not match", HttpStatus.BAD_REQUEST);
    }

//    System.out.println("****Cookie " + Arrays.stream(req.getCookies())
//            .filter(c -> c.getName().equals("currentUser"))
//            .findFirst()
//            .map(Cookie::getValue)
//            .orElse(null));

//    Cookie[] cookies = req.getCookies();
//
//    if (cookies != null) {
//      System.out.println("***** cookies ******");
//      System.out.println(Arrays.stream(cookies)
//              .map(c -> c.getName() + "=" + c.getValue()).collect(Collectors.joining(", ")));
//    }


//    if (!validateId(id, session)) {
//      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
//    }
    User user = userService.findUserByID(id);
    //session.setAttribute("currentUserId", id);

    return new ResponseEntity(user, HttpStatus.OK);
  }

  private boolean validateId(int id, HttpSession session) {
//    if (session == null) {
//      return false;
//    }
//    try {
//      if (Integer.parseInt(session.getAttribute("currentUserId").toString()) != id) {
//        return false;
//      }
//    } catch (Exception e) {
//      return false;
//    }
    return true;
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @PostMapping(path = "api/users/{id}/creatUser" )
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


  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @PostMapping(path = "api/users/{id}/deleteUser/{userToBeDeleted}" )
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

  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @GetMapping(path = "api/users/{id}/listManagers" )
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

  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @GetMapping(path = "api/users/{id}/listArtists" )
  public ResponseEntity<String> listArtists(@PathVariable("id") int id, HttpSession session) {
    if (!validateId(id, session)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    User admin = userService.findUserByID(id);
    if (!admin.getUserType().equals("Admin")) {
      return new ResponseEntity("Please login as a admin.", HttpStatus.BAD_REQUEST);
    }



    List<Artist> artists = artistService.findAllArtists();
    session.setAttribute("currentUserId", id);
    return new ResponseEntity(artists, HttpStatus.OK);
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @GetMapping(path = "api/users/{id}/listUsers")
  public ResponseEntity<String> listUsers(@PathVariable("id") int id, HttpSession session) {
    if (!validateId(id, session)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    User admin = userService.findUserByID(id);
    if (!admin.getUserType().equals("Admin")) {
      return new ResponseEntity("Please login as a admin.", HttpStatus.BAD_REQUEST);
    }


    List<User> users = userService.findAllUsers();

    session.setAttribute("currentUserId", id);
    return new ResponseEntity(users, HttpStatus.OK);
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @PostMapping(path = "api/users/{id}/request/artists/{artistId}" )
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

  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @GetMapping(path = "api/users/{id}/contracts" )
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

  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @PostMapping(value = "api/users/{id}/contracts/{contractId}/accept" )
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
    session.setAttribute("currentUserId", id);
    return new ResponseEntity(contract, HttpStatus.OK);
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @PostMapping(value = "api/users/{id}/contracts/{contractId}/reject" )
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

    session.setAttribute("currentUserId", id);
    return new ResponseEntity(contract, HttpStatus.OK);
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @PostMapping(value = "api/users/{id}/contracts/{contractId}/events" )
  public ResponseEntity<Event> createEvent(@PathVariable("id") int id, @PathVariable("contractId") int contractId, @RequestBody Event event, HttpSession session) {
    if (!validateId(id, session)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    Manager manager = managerService.findManagerByID(id);

    if (manager == null) {
      return new ResponseEntity("Please login as an manager.", HttpStatus.BAD_REQUEST);
    }

    Contract contract = contractService.findContractById(contractId);
    if (contract == null) {
      return new ResponseEntity("Invalid contract id.", HttpStatus.BAD_REQUEST);
    }
    if (contract.getEvent() != null) {
      return new ResponseEntity("Already an event to this contract.", HttpStatus.BAD_REQUEST);
    }

    if (contract.getStatus().equals("Pending") || contract.getStatus().equals("Rejected")) {
      return new ResponseEntity("Event can not be creared before for Pending/Rejected contract", HttpStatus.BAD_REQUEST);
    }

    Event newEvent = contractService.createEvent(event.getName(), event.getDescription(),
            event.getCapacity(), event.getDate(), contract);

    session.setAttribute("currentUserId", id);
    return new ResponseEntity(newEvent, HttpStatus.OK);
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @GetMapping("/api/users/{id}/allUsers")
  public ResponseEntity<List<User>> getAllUsers(@PathVariable("id") int id, HttpSession session) {
    if (!validateId(id, session)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    Admin admin = adminService.findById(id);

    if (admin == null) {
      return new ResponseEntity("Login as a Admin", HttpStatus.BAD_REQUEST);
    }

    List<Object> users = new ArrayList<>();

    List<User> userTypes = userService.findAllUsers();
    List<Manager> managers = managerService.findAllManagers();
    List<Artist> artists = artistService.findAllArtists();

    users.add(managers);
    users.add(userTypes);
    users.add(artists);

    session.setAttribute("currentUserId", id);
    return new ResponseEntity(users, HttpStatus.OK);
  }


}
