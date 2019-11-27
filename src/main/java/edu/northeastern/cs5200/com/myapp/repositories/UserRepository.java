package edu.northeastern.cs5200.com.myapp.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import edu.northeastern.cs5200.com.myapp.models.User;

public interface UserRepository extends CrudRepository<User, Integer> {
  @Query("SELECT s FROM User s WHERE s.userName=:username")
  User findByUserName(@Param("username") String userName);

  @Query("SELECT s FROM User s WHERE s.userName=:username and s.password=:password")
  User findByUserNameAndPassword(@Param("username") String userName, @Param("password") String password);
}
