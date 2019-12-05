package edu.northeastern.cs5200.com.myapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.cs5200.com.myapp.models.Artist;
import edu.northeastern.cs5200.com.myapp.models.Contract;
import edu.northeastern.cs5200.com.myapp.repositories.ArtistRepository;

@Service
public class ArtistService {
  @Autowired
  private ArtistRepository artistRepository;

  public Artist registerNewArtist(Artist artist) {
    Artist alreadyRegisteredArtist = artistRepository.findByUserName(artist.getUserName());
    if (alreadyRegisteredArtist != null) {
      return null;
    }
    return artistRepository.save(artist);
  }

  public Artist findByUserIDAndPassword(String userName, String password) {
    return artistRepository.findByUserNameAndPassword(userName, password);
  }

  public Artist findArtistByID(int id) {
    return artistRepository.findById(id).get();
  }

  public List<Artist> findAllArtists() {
    Iterable<Artist> it =  artistRepository.findAll();
    List<Artist> artists = new ArrayList();

    for (Artist artist : it) {
      artists.add(artist);
    }
    return artists;
  }

  public List<Contract> getContracts(Integer id) {
    Artist artist = artistRepository.findById(id).get();
    return artist.getContracts();
  }

  public Artist save(Artist artist) {
    if (artist == null) {
      return null;
    }
    return artistRepository.save(artist);
  }

  public Artist findArtistByName(String artistFisrtName, String lastName) {
    return artistRepository.findByFirstNameAndLastName(artistFisrtName, lastName);
  }
}
