package edu.northeastern.cs5200.com.myapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.northeastern.cs5200.com.myapp.models.User;
import edu.northeastern.cs5200.com.myapp.repositories.UserRepository;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public User registerNewUser(User user) {
    User alreadyRegisteredUser = userRepository.findByUserName(user.getUserName());
    if (alreadyRegisteredUser != null) {
      return null;
    }
    return userRepository.save(user);
  }

  public User findByUserIDAndPassword(String userName, String password) {
    return userRepository.findByUserNameAndPassword(userName, password);
  }

  public User findUserByID(int id) {
    return userRepository.findById(id).get();
  }

  public void deleteUserById(Integer id) {
    userRepository.deleteById(id);
  }

  public List<User> findAllUsers() {
    Iterable<User> it = userRepository.findAll();
    List<User> users = new ArrayList();

    for (User user : it) {
      users.add(user);
    }
    return users.stream()
            .filter(user -> user.getUserType().equals("User"))
            .collect(Collectors.toList());
  }

  public User save(User existingUser) {
    if (existingUser == null) {
      return null;
    }
    return userRepository.save(existingUser);
  }
}
