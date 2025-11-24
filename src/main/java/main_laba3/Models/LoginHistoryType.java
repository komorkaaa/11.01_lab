package main_laba3.Models;

import java.sql.Timestamp;
import java.util.Date;

public class LoginHistoryType {

  private int userId;
  private String userName;
  private Timestamp loginTime;
  private String status;

  public LoginHistoryType(int userId, String userName, Timestamp loginTime, String status) {
    this.userId = userId;
    this.userName = userName;
    this.loginTime = loginTime;
    this.status = status;
  }

  public int getUserId() { return userId; }
  public String getUserName() { return userName; }
  public Timestamp getLoginTime() { return loginTime; }
  public String getStatus() { return status; }
}