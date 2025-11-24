package main_laba3.Models;

public class UsersType {
  private int idusers;
  private String fio;
  private String login;
  private String password;
  private String role;
  private boolean isBlocked;
  private int failedAttempts;

  private static UsersType currentUser;

  public static void setCurrentUser(UsersType user) {
    currentUser = user;
  }

  public static boolean isAdmin() {
    return currentUser != null && "admin".equalsIgnoreCase(currentUser.getRole());
  }

  public UsersType(int idusers, String fio, String login, String password, String role, boolean isBlocked, int failedAttempts) {
    this.idusers = idusers;
    this.fio = fio;
    this.login = login;
    this.password = password;
    this.role = role;
    this.isBlocked = isBlocked;
    this.failedAttempts = failedAttempts;
  }


  public boolean isBlocked() { return isBlocked; }
  public int getFailedAttempts() { return failedAttempts; }
  public int getIdusers() {
    return idusers;
  }
  public String getFio() {
    return fio;
  }
  public String getLogin() {
    return login;
  }
  public String getPassword() {
    return password;
  }
  public String getRole() {
    return role;
  }

  public void setIdusers(int idusers) {
    this.idusers = idusers;
  }
}
