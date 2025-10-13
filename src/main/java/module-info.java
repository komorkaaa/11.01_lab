module main.laba3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.graphics;
//  requires main.laba3;
  requires javafx.base;


  opens main_laba3 to javafx.fxml;
    exports main_laba3;
  exports main_laba3.Controllers;
  opens main_laba3.Controllers to javafx.fxml;
  exports main_laba3.DAO;
  opens main_laba3.DAO to javafx.fxml;
  exports main_laba3.Models;
  opens main_laba3.Models to javafx.fxml;
}