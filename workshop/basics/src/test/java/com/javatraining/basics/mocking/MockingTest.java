package com.javatraining.basics.mocking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Demonstrates Mockito features: stubbing, verification, argument capture, and spy patterns. */
@ExtendWith(MockitoExtension.class)
@DisplayName("Mocking with Mockito")
class MockingTest {

  @Mock UserRepository repository;

  @InjectMocks UserService userService;

  @Test
  @DisplayName("getUser returns user when found")
  void getUserReturnsUserWhenFound() {
    User alice = new User("1", "Alice", "a@a.com");
    when(repository.findById("1")).thenReturn(Optional.of(alice));

    User result = userService.getUser("1");

    assertThat(result.name()).isEqualTo("Alice");
    assertThat(result.email()).isEqualTo("a@a.com");
  }

  @Test
  @DisplayName("getUser throws RuntimeException when user not found")
  void getUserThrowsWhenNotFound() {
    when(repository.findById("99")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.getUser("99"))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("User not found");
  }

  @Test
  @DisplayName("deleteUser delegates to repository.delete")
  void deleteUserDelegatesToRepository() {
    userService.deleteUser("1");

    verify(repository).delete("1");
  }

  @Test
  @DisplayName("createUser passes correct name to repository.save")
  void createUserPassesCorrectNameToSave() {
    User saved = new User("generated-id", "Bob", "b@b.com");
    when(repository.save(any(User.class))).thenReturn(saved);

    userService.createUser("Bob", "b@b.com");

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    verify(repository).save(captor.capture());

    User captured = captor.getValue();
    assertThat(captured.name()).isEqualTo("Bob");
    assertThat(captured.email()).isEqualTo("b@b.com");
    assertThat(captured.id()).isNotBlank(); // UUID was generated
  }

  @Test
  @DisplayName("getAllUsers returns the list provided by repository")
  void getAllUsersReturnsRepositoryList() {
    List<User> users = List.of(new User("1", "Alice", "a@a.com"), new User("2", "Bob", "b@b.com"));
    when(repository.findAll()).thenReturn(users);

    List<User> result = userService.getAllUsers();

    assertThat(result).hasSize(2).extracting(User::name).containsExactly("Alice", "Bob");
    verify(repository, times(1)).findAll();
  }
}
