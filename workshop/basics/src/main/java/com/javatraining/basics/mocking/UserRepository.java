package com.javatraining.basics.mocking;

import java.util.List;
import java.util.Optional;

/** Repository interface for {@link User} persistence operations. */
public interface UserRepository {

  Optional<User> findById(String id);

  User save(User user);

  void delete(String id);

  List<User> findAll();
}
