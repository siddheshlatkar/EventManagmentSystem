package edu.northeastern.cs5200.com.myapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import edu.northeastern.cs5200.com.myapp.models.EventHelper;
import edu.northeastern.cs5200.com.myapp.models.Ticket;
import edu.northeastern.cs5200.com.myapp.models.TicketHelper;
import edu.northeastern.cs5200.com.myapp.services.EventService;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import edu.northeastern.cs5200.com.myapp.models.Admin;
import edu.northeastern.cs5200.com.myapp.models.Artist;
import edu.northeastern.cs5200.com.myapp.models.Contract;
import edu.northeastern.cs5200.com.myapp.models.ContractHelper;
import edu.northeastern.cs5200.com.myapp.models.Event;
import edu.northeastern.cs5200.com.myapp.models.Manager;
import edu.northeastern.cs5200.com.myapp.models.User;
import edu.northeastern.cs5200.com.myapp.services.AdminService;
import edu.northeastern.cs5200.com.myapp.services.ArtistService;
import edu.northeastern.cs5200.com.myapp.services.ContractService;
import edu.northeastern.cs5200.com.myapp.services.ManagerService;
import edu.northeastern.cs5200.com.myapp.services.UserService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;


@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
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

  @Autowired
  private EventService eventService;

  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @PostMapping(path = "/api/users" )
  public ResponseEntity<User> register(@RequestBody User user) {

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

    return ResponseEntity.ok()
            .header("Authorization", newUser.getId().toString())
            .body(newUser);
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @PostMapping(path = "api/user/login" )
  public ResponseEntity login(@RequestBody User user) {

    User loggedUser = userService.findByUserIDAndPassword(user.getUserName(), user.getPassword());


    if (loggedUser == null) {
      return new ResponseEntity("Username or password is wrong", HttpStatus.BAD_REQUEST);
    }
    //System.out.println("*****ID " + loggedUser.getId() + "name  " + loggedUser.getUserName());


    //Cookie cookie = new Cookie("currentUser", loggedUser.getId().toString());
    //cookie.setPath("/");

    //response.addCookie(cookie);

    ResponseEntity responseEntity = ResponseEntity.ok()
            .header("Authorization", loggedUser.getId().toString())
            .body(loggedUser);

    //ResponseEntity responseEntity = new ResponseEntity(loggedUser, HttpStatus.OK);


    //session.setAttribute("currentUserId", loggedUser.getId());
    //return new ResponseEntity(loggedUser, HttpStatus.OK);
    return responseEntity;
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @GetMapping(path = "api/users/{id}/logout" )
  public ResponseEntity<String> logout(@PathVariable("id") int id, HttpServletRequest request) {
//    if (!validateId(id, session)) {
//      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
//    }
//    session.invalidate();


    ResponseEntity responseEntity = ResponseEntity.ok()
            .header("Authorization", "")
            .body("Successfully logged out.");
    //return new ResponseEntity("Successfully logged out.", HttpStatus.OK);
    return responseEntity;
  }


  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @GetMapping(path = "api/users/{id}/profile" )
  public ResponseEntity<User> profile(@PathVariable("id") Integer id, HttpServletRequest req) {
    //System.out.println("****Cookie Value" + req.getCookies()[0].getValue());
    //System.out.println("****Cookie name" + req.getCookies()[0].getName());

    String auth = req.getHeader("Authorization");

    System.out.println("****Auth " + auth);
    if (auth == null || (auth != null && !auth.equals(""+ id))) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

//    String cookieValue = Arrays.stream(req.getCookies())
//            .filter(c -> c.getName().equals("currentUser"))
//            .findFirst()
//            .map(Cookie::getValue)
//            .orElse(null);


//    if (cookieValue == null) {
//      return new ResponseEntity("Can't get cookie with namecurrentUser", HttpStatus.BAD_REQUEST);
//    }
//
//    if (!cookieValue.equals("" + id)) {
//      return new ResponseEntity("id and cookie value does not match", HttpStatus.BAD_REQUEST);
//    }

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

  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @PutMapping("/api/users/{id}/update")
  public ResponseEntity update(@PathVariable("id") int id, @RequestBody User user, HttpServletRequest req) {
    if (!validateId(id, req)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    User existingUser = userService.findUserByID(id);

    if (existingUser == null) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    if (user.getUserName() == null || user.getPassword() == null) {
      return new ResponseEntity("UserName or password can not be blank", HttpStatus.BAD_REQUEST);
    }
    if (existingUser.getUserType().equals("Manager")) {
      Manager manager = managerService.findManagerByID(id);

      manager.setUserName(user.getUserName());
      manager.setPassword(user.getPassword());
      manager.setFirstName(user.getFirstName());
      manager.setLastName(user.getLastName());
      manager.setDob(user.getDob());
      manager.setAddress(user.getAddress());
      manager.setPhone(user.getPhone());
      manager.setEmail(user.getEmail());

      Manager manager1 = managerService.save(manager);

      return new ResponseEntity(manager1, HttpStatus.OK);

    } else if (existingUser.getUserType().equals("Artist")) {

      Artist artist = artistService.findArtistByID(id);

      artist.setUserName(user.getUserName());
      artist.setPassword(user.getPassword());
      artist.setFirstName(user.getFirstName());
      artist.setLastName(user.getLastName());
      artist.setDob(user.getDob());
      artist.setAddress(user.getAddress());
      artist.setPhone(user.getPhone());
      artist.setEmail(user.getEmail());

      Artist artist1 = artistService.save(artist);

      return new ResponseEntity(artist1, HttpStatus.OK);

    } else if (existingUser.getUserType().equals("Admin")) {

      Admin admin = adminService.findById(id);

      admin.setUserName(user.getUserName());
      admin.setPassword(user.getPassword());
      admin.setFirstName(user.getFirstName());
      admin.setLastName(user.getLastName());
      admin.setDob(user.getDob());
      admin.setAddress(user.getAddress());
      admin.setPhone(user.getPhone());
      admin.setEmail(user.getEmail());

      Admin admin1 = adminService.save(admin);

      return new ResponseEntity(admin1, HttpStatus.OK);

    } else {

      existingUser.setUserName(user.getUserName());
      existingUser.setPassword(user.getPassword());
      existingUser.setFirstName(user.getFirstName());
      existingUser.setLastName(user.getLastName());
      existingUser.setDob(user.getDob());
      existingUser.setAddress(user.getAddress());
      existingUser.setPhone(user.getPhone());
      existingUser.setEmail(user.getEmail());
      User user1 = userService.save(existingUser);

      return new ResponseEntity(user1, HttpStatus.OK);
    }
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @PutMapping("/api/users/{id}/updateUser")
  public ResponseEntity updateUser(@PathVariable("id") int id, @RequestBody User user, HttpServletRequest req) {
    if (!validateId(id, req)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    User admin2 = userService.findUserByID(id);
    if (admin2 == null || (admin2 != null && !admin2.getUserType().equals("Admin"))) {
      return new ResponseEntity("Please login as Admin.", HttpStatus.BAD_REQUEST);
    }

    if (user.getUserName() == null || user.getPassword() == null || user.getId() == null) {
      return new ResponseEntity("Id, UserName or password can not be blank", HttpStatus.BAD_REQUEST);
    }

    User existingUser = userService.findUserByID(user.getId());

    if (existingUser == null) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    if (existingUser.getUserType().equals("Manager")) {
      Manager manager = managerService.findManagerByID(user.getId());

      manager.setUserName(user.getUserName());
      manager.setPassword(user.getPassword());
      manager.setFirstName(user.getFirstName());
      manager.setLastName(user.getLastName());
      manager.setDob(user.getDob());
      manager.setAddress(user.getAddress());
      manager.setPhone(user.getPhone());
      manager.setEmail(user.getEmail());

      Manager manager1 = managerService.save(manager);

      return new ResponseEntity(manager1, HttpStatus.OK);

    } else if (existingUser.getUserType().equals("Artist")) {

      Artist artist = artistService.findArtistByID(user.getId());

      artist.setUserName(user.getUserName());
      artist.setPassword(user.getPassword());
      artist.setFirstName(user.getFirstName());
      artist.setLastName(user.getLastName());
      artist.setDob(user.getDob());
      artist.setAddress(user.getAddress());
      artist.setPhone(user.getPhone());
      artist.setEmail(user.getEmail());

      Artist artist1 = artistService.save(artist);

      return new ResponseEntity(artist1, HttpStatus.OK);

    } else if (existingUser.getUserType().equals("Admin")) {

      Admin admin = adminService.findById(user.getId());

      admin.setUserName(user.getUserName());
      admin.setPassword(user.getPassword());
      admin.setFirstName(user.getFirstName());
      admin.setLastName(user.getLastName());
      admin.setDob(user.getDob());
      admin.setAddress(user.getAddress());
      admin.setPhone(user.getPhone());
      admin.setEmail(user.getEmail());

      Admin admin1 = adminService.save(admin);

      return new ResponseEntity(admin1, HttpStatus.OK);

    } else {

      existingUser.setUserName(user.getUserName());
      existingUser.setPassword(user.getPassword());
      existingUser.setFirstName(user.getFirstName());
      existingUser.setLastName(user.getLastName());
      existingUser.setDob(user.getDob());
      existingUser.setAddress(user.getAddress());
      existingUser.setPhone(user.getPhone());
      existingUser.setEmail(user.getEmail());
      User user1 = userService.save(existingUser);

      return new ResponseEntity(user1, HttpStatus.OK);
    }
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @DeleteMapping("/api/users/id/deleteUser/{userId}")
  public ResponseEntity deleteUser(@PathVariable("id") int id, @PathVariable("userId") int userId, HttpServletRequest req) {
    if (!validateId(id, req)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }
    User user = adminService.findById(id);
    if (!user.getUserType().equals("Admin")) {
      return new ResponseEntity("Please login as a admin.", HttpStatus.BAD_REQUEST);
    }

    if (userService.findUserByID(userId) != null) {
      userService.deleteUserById(userId);
      return new ResponseEntity("User Deleted", HttpStatus.OK);
    } else {
      return new ResponseEntity("User not exist", HttpStatus.OK);
    }

  }

  private boolean validateId(int id, HttpServletRequest req) {
    String auth = req.getHeader("Authorization");
    if (auth == null || (auth != null && !auth.equals(""+ id))) {
      return false;
    }
    return true;
  }

  private boolean validateId(int id, HttpSession session) {
////    if (session == null) {
////      return false;
////    }
////    try {
////      if (Integer.parseInt(session.getAttribute("currentUserId").toString()) != id) {
////        return false;
////      }
////    } catch (Exception e) {
////      return false;
////    }
    return true;
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @PostMapping(path = "api/users/{id}/creatUser" )
  public ResponseEntity<User> createUser(@PathVariable("id") int id, @RequestBody User user, HttpServletRequest request) {
    if (!validateId(id, request)) {
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

    return new ResponseEntity(newUser, HttpStatus.OK);
  }


  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @PostMapping(path = "api/users/{id}/deleteUser/{userToBeDeleted}" )
  public ResponseEntity<String> deleteUser(@PathVariable("id") int id, @PathVariable("userToBeDeleted") User user, HttpServletRequest request) {
    if (!validateId(id, request)) {
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

    if (existingUser != null) {
      userService.deleteUserById(user.getId());
      return new ResponseEntity("Successfully deleted user", HttpStatus.OK);
    } else {
      return new ResponseEntity("User does not exist", HttpStatus.BAD_REQUEST);
    }
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @GetMapping(path = "api/users/{id}/listManagers" )
  public ResponseEntity<String> listManagers(@PathVariable("id") int id, HttpServletRequest request) {
    if (!validateId(id, request)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    User admin = userService.findUserByID(id);
    if (!admin.getUserType().equals("Admin")) {
      return new ResponseEntity("Please login as a admin.", HttpStatus.BAD_REQUEST);
    }

    List<Manager> managers = managerService.findAllManagers();
    return new ResponseEntity(managers, HttpStatus.OK);
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @GetMapping(path = "api/users/{id}/listArtists" )
  public ResponseEntity<String> listArtists(@PathVariable("id") int id, HttpServletRequest req) {
    if (!validateId(id, req)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    List<Artist> artists = artistService.findAllArtists();
    return new ResponseEntity(artists, HttpStatus.OK);
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @GetMapping(path = "api/users/{id}/listUsers")
  public ResponseEntity<String> listUsers(@PathVariable("id") int id, HttpServletRequest request) {
    if (!validateId(id, request)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    User admin = userService.findUserByID(id);
    if (!admin.getUserType().equals("Admin")) {
      return new ResponseEntity("Please login as a admin.", HttpStatus.BAD_REQUEST);
    }


    List<User> users = userService.findAllUsers();

    return new ResponseEntity(users, HttpStatus.OK);
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @PostMapping(path = "api/users/{id}/request/artists/{artistId}" )
  public ResponseEntity<Contract> request(@PathVariable("id") int id, @PathVariable("artistId") int artistId, @RequestBody Contract contract, HttpServletRequest request) {
    if (!validateId(id, request)) {
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
    return new ResponseEntity(newContract, HttpStatus.OK);
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @GetMapping(path = "api/users/{id}/contracts" )
  public ResponseEntity<List<ContractHelper>> getContracts(@PathVariable("id") int id, HttpServletRequest request) {
    if (!validateId(id, request)) {
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

    List<ContractHelper> contractHelpers = new ArrayList();
    for (Contract contract : contracts) {
      ContractHelper contractHelper = new ContractHelper(contract.getId(), contract.getDate(), contract.getText(), contract.getStatus(), contract.getManager().getFirstName() + " " + contract.getManager().getLastName(),
              contract.getArtist().getFirstName() + " " + contract.getArtist().getLastName());
      contractHelpers.add(contractHelper);
    }

    return new ResponseEntity(contractHelpers, HttpStatus.OK);
    //return contractsJson;
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @PostMapping(value = "api/users/{id}/contracts/{contractId}/accept" )
  public ResponseEntity<Contract> acceptContract(@PathVariable("id") int id, @PathVariable("contractId") int contractId, HttpServletRequest request) {
    if (!validateId(id, request)) {
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

  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @PostMapping(value = "api/users/{id}/contracts/{contractId}/reject" )
  public ResponseEntity<Contract> rejectContract(@PathVariable("id") int id, @PathVariable("contractId") int contractId, HttpServletRequest request) {
    if (!validateId(id, request)) {
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

  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @PostMapping(value = "api/users/{id}/contracts/{contractId}/events" )
  public ResponseEntity<Event> createEvent(@PathVariable("id") int id, @PathVariable("contractId") int contractId, @RequestBody Event event,  HttpServletRequest request) {
    if (!validateId(id, request)) {
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

    return new ResponseEntity(newEvent, HttpStatus.OK);
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @GetMapping("/api/users/{id}/allUsers")
  public ResponseEntity<List<User>> getAllUsers(@PathVariable("id") int id, HttpServletRequest request) {
    if (!validateId(id, request)) {
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
    return new ResponseEntity(users, HttpStatus.OK);
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @GetMapping("api/users/search/")
  public ResponseEntity search(@RequestParam("name") String name, @RequestParam("location") String location, HttpServletRequest request) throws ParseException {
    String[] names = null;
    if (name != null && !name.isEmpty()) {
      names = name.split(" ");
    }
    Artist artist = null;
    if (names != null && names.length == 2) {
      artist = artistService.findArtistByName(names[0], names[1]);
    }

    List<EventHelper> eventHelpers = new ArrayList();

    if (artist != null) {
      List<Contract> contracts = artist.getContracts();
      List<Event> events = contracts.stream()
              .map(contract -> contract.getEvent())
              .filter(a -> a != null)
              .collect(Collectors.toList());

      for (Event event: events) {
        eventHelpers.add(new EventHelper(event.getId(), event.getName(), event.getDescription(),
                event.getCapacity(), event.getDate(), false, event.getContract().getId()));
      }
    }

    String externalAPIString = "";
    if (name != null && !name.equals("") && location != null && location.length() != 0) {
      externalAPIString = "https://api.songkick.com/api/3.0/events.json?apikey=Z1Ca95V8ZKsgJYNa" + "&artist_name=" + name + "&location=" + location;
    } else if (name != null && !name.isEmpty()) {
      externalAPIString = "https://api.songkick.com/api/3.0/events.json?apikey=Z1Ca95V8ZKsgJYNa" + "&artist_name=" + name;
    } else if (location != null && !location.isEmpty()) {
      externalAPIString = "https://api.songkick.com/api/3.0/events.json?apikey=Z1Ca95V8ZKsgJYNa" + "&location=" + location;
    } else {
      return new ResponseEntity(new ArrayList(), HttpStatus.OK);
    }

    HttpResponse response = HttpRequest
            .get(externalAPIString)
            .send();
    System.out.println(response.bodyText());
    JSONObject object = (JSONObject) new JSONParser().parse(response.bodyText());
    JSONObject resultsPage = (JSONObject) object.get("resultsPage");

    int totalEntries = Integer.parseInt(resultsPage.get("totalEntries").toString());

    System.out.println("**** Total Entries " + totalEntries );

    JSONObject results = (JSONObject) resultsPage.get("results");

    JSONArray eventsJson = (JSONArray) results.get("event");
    if (eventsJson == null) {
      return new ResponseEntity(eventHelpers, HttpStatus.OK);
    }
    JSONObject event = (JSONObject) eventsJson.get(0);
    System.out.println("Link" + event.get("uri").toString());

    for (int i = 0; i < totalEntries && i < 49; i++) {
      JSONObject tmpEvent = (JSONObject) eventsJson.get(i);

      eventHelpers.add(new EventHelper(-1, tmpEvent.get("displayName").toString(), tmpEvent.get("uri").toString(), 100, ((JSONObject)tmpEvent.get("start")).get("date").toString(), true, -1));
    }


    return new ResponseEntity(eventHelpers, HttpStatus.OK);
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @PostMapping("/api/users/{userId}/events/{eventId}/tickets")
  public ResponseEntity bookTickets(@PathVariable("userId") int userID, @PathVariable("eventId") int eventID, HttpServletRequest req) {
    if (!validateId(userID, req)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    User user = userService.findUserByID(userID);

    if (user == null) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    Event event = eventService.findByID(eventID);
    Ticket ticket = eventService.bookTicket(user, event);
    return new ResponseEntity(new TicketHelper(ticket.getId(), ticket.getSeat(), event.getId(), event.getName(), event.getDate()), HttpStatus.OK);
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @GetMapping("/api/users/{id}/tickets")
  public ResponseEntity tickets(@PathVariable("id") int id, HttpServletRequest req) {
    if (!validateId(id, req)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    User user = userService.findUserByID(id);

    if (user == null) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    List<TicketHelper> ticketHelpers = user.getTickets().stream()
                                          .map(ticket -> new TicketHelper(ticket.getId(), ticket.getSeat(), ticket.getEvent().getId(), ticket.getEvent().getName(), ticket.getEvent().getDate())).collect(Collectors.toList());

    return new ResponseEntity(ticketHelpers, HttpStatus.OK);
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
  @GetMapping("/api/users/{id}/events")
  public ResponseEntity getEventsForUser(@PathVariable("id") int id, HttpServletRequest req) {
    if (!validateId(id, req)) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    User user = userService.findUserByID(id);

    if (user == null) {
      return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }

    List<EventHelper> events = user.getTickets().stream().map(ticket -> new EventHelper(ticket.getEvent().getId(), ticket.getEvent().getName(),
            ticket.getEvent().getDescription(), ticket.getEvent().getCapacity(), ticket.getEvent().getDate(), false, ticket.getEvent().getContract().getId())).collect(Collectors.toList());
    return new ResponseEntity(events, HttpStatus.OK);
  }


}
