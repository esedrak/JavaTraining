package com.javatraining.basics.mocking;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

/** Service layer for user management, demonstrating constructor injection for easy mocking. */
@Service
public class UserService {

  private final UserRepository repository;

  public UserService(UserRepository repository) {
    this.repository = repository;
  }

  /**
   * Retrieves a user by id.
   *
   * @throws RuntimeException if no user with the given id exists
   */
  public User getUser(String id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("User not found: " + id));
  }

  /**
   * Creates a new user with a generated UUID and persists it.
   *
   * @param name  display name
   * @param email email address
   * @return the saved user (as returned by the repository)
   */
  public User createUser(String name, String email) {
    String id = UUID.randomUUID().toString();
    User user = new User(id, name, email);
    return repository.save(user);
  }

  /** Deletes the user with the given id. */
  public void deleteUser(String id) {
    repository.delete(id);
  }

  /** Returns all users in the repository. */
  public List<User> getAllUsers() {
    return repository.findAll();
  }
}
