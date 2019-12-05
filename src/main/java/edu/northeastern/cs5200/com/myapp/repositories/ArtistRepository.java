package edu.northeastern.cs5200.com.myapp.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.northeastern.cs5200.com.myapp.models.Artist;
import edu.northeastern.cs5200.com.myapp.models.Manager;

@Repository
public interface ArtistRepository extends CrudRepository<Artist, Integer> {
  @Query("SELECT s FROM Artist s WHERE s.userName=:username")
  Artist findByUserName(@Param("username") String userName);

  @Query("SELECT s FROM Artist s WHERE s.userName=:username and s.password=:password")
  Artist findByUserNameAndPassword(@Param("username") String userName, @Param("password") String password);

  @Query("SELECT s FROM Artist s WHERE s.firstName=:firstName and s.lastName=:lastName")
  Artist findByFirstNameAndLastName(@Param("firstName")String firstName, @Param("lastName") String lastName);
}
