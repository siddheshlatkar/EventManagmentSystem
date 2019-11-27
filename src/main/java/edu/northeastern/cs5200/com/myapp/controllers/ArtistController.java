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

import edu.northeastern.cs5200.com.myapp.models.Artist;
import edu.northeastern.cs5200.com.myapp.services.ArtistService;

@RestController
public class ArtistController {

  @Autowired
  private ArtistService artistService;

  @PostMapping(path = "/api/artists", consumes = "application/json")
  public ResponseEntity<Artist> register(@RequestBody Artist artist, HttpSession session) {
    if (artist.getUserName() == null || artist.getPassword() == null || artist.getUserName().isEmpty() || artist.getPassword().isEmpty()) {
      return new ResponseEntity("User name of password can not be blank", HttpStatus.BAD_REQUEST);
    }
    Artist newArtist = artistService.registerNewArtist(artist);
    if (newArtist == null) {
      return new ResponseEntity("Username is already taken", HttpStatus.BAD_REQUEST);
    }
    session.setAttribute("currentUserId", newArtist.getId());
    session.setAttribute("currentUserType", "ARTIST");
    return new ResponseEntity(newArtist, HttpStatus.OK);
  }

  @PostMapping(path = "api/artist/login", consumes = "application/json")
  public ResponseEntity<Artist> login(@RequestBody Artist artist, HttpSession session) {
    Artist existingArtist = artistService.findByUserIDAndPassword(artist.getUserName(), artist.getPassword());
    if (existingArtist == null) {
      return new ResponseEntity("Username or password is wrong", HttpStatus.BAD_REQUEST);
    }
    session.setAttribute("currentUserId", existingArtist.getId());
    session.setAttribute("currentUserType", "ARTIST");
    return new ResponseEntity(existingArtist, HttpStatus.OK);
  }

  @GetMapping(path = "api/artists/{id}/profile", consumes = "application/json")
  public ResponseEntity<Artist> profile(@PathVariable("id") int id, HttpSession session) {
    if (!validateId(id, session)) {
       return new ResponseEntity("Please login first", HttpStatus.BAD_REQUEST);
    }
    Artist artist = artistService.findArtistByID(id);
    return new ResponseEntity(artist, HttpStatus.OK);
  }

  @GetMapping(path = "api/artists/{id}/logout", consumes = "application/json")
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
              !session.getAttribute("currentUserType").equals("ARTIST")) {
        return false;

      }
    } catch (Exception e) {
      return false;
    }
    return true;
  }
}
