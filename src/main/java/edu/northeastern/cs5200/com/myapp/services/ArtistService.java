package edu.northeastern.cs5200.com.myapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import edu.northeastern.cs5200.com.myapp.models.Artist;
import edu.northeastern.cs5200.com.myapp.models.Manager;
import edu.northeastern.cs5200.com.myapp.repositories.ArtistRepository;

@RestController
public class ArtistService {
  @Autowired
  private ArtistRepository artistRepository;

  @PostMapping(path = "/api/artists", consumes = "application/json")
  public ResponseEntity<Artist> register(@RequestBody Artist artist, HttpSession session) {
    session.setAttribute("currentUser", artist);
    if ((artist.getUserName() == null || artist.getPassword() == null) || artist.getUserName().isEmpty() || artist.getPassword().isEmpty()) {
      return new ResponseEntity("User name or Password can not be blank", HttpStatus.BAD_REQUEST);
    }
    Artist existingManager = (Artist) artistRepository.findByUserName(artist.getUserName());
    if (existingManager != null) {
      return new ResponseEntity("Username is already taken", HttpStatus.BAD_REQUEST);
    }
    Artist registeredArtist = artistRepository.save(((Artist)artist));
    return new ResponseEntity(registeredArtist, HttpStatus.OK);
  }

  @PostMapping(path = "api/artists/login")
  public ResponseEntity<Manager> login(@RequestBody Artist artist, HttpSession session) {
    if ((artist.getUserName() == null || artist.getPassword() == null) || artist.getUserName().isEmpty() || artist.getPassword().isEmpty()) {
      return new ResponseEntity("User name or Password can not be blank", HttpStatus.BAD_REQUEST);
    }
    Artist existingArtist = (Artist) artistRepository.findByUserNameAndPassword(artist.getUserName(), artist.getPassword());
    if (existingArtist != null) {
      session.setAttribute("currentUSer", existingArtist);
      return new ResponseEntity(existingArtist, HttpStatus.OK);
    }
    return new ResponseEntity("Username or password is wrong", HttpStatus.BAD_REQUEST);
  }
}
