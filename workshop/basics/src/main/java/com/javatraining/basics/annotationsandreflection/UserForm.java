package com.javatraining.basics.annotationsandreflection;

/** Example form class whose fields are validated by the reflection-based {@link Validator}. */
public class UserForm {

  @Validate(min = 2, max = 50, required = true)
  private String username;

  @Validate(min = 8, required = true)
  private String password;

  @Validate(required = false)
  private String nickname;

  public UserForm() {}

  public UserForm(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public UserForm(String username, String password, String nickname) {
    this.username = username;
    this.password = password;
    this.nickname = nickname;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }
}
