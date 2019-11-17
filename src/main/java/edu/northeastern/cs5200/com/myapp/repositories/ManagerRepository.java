package edu.northeastern.cs5200.com.myapp.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.northeastern.cs5200.com.myapp.models.Manager;

@Repository
public interface ManagerRepository extends CrudRepository<Manager, Integer> {

  @Query("SELECT s FROM Manager s WHERE s.userName=:username")
  Manager findByUserName(@Param("username") String userName);

  @Query("SELECT s FROM Manager s WHERE s.userName=:username and s.password=:password")
  Manager findByUserNameAndPassword(@Param("username") String userName, @Param("password") String password);
}