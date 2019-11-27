package edu.northeastern.cs5200.com.myapp.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import edu.northeastern.cs5200.com.myapp.models.Admin;

public interface AdminRepository extends CrudRepository<Admin, Integer> {
  @Query("SELECT s FROM Admin s WHERE s.userName=:username and s.password=:password")
  Admin findByUserNameAndPassword(@Param("username") String userName, @Param("password") String password);
}
