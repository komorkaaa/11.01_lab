package main_laba3.Controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import main_laba3.HelloApplication;
import main_laba3.Models.LoginHistoryType;
import main_laba3.db.DbConnection;

import java.io.IOException;
import java.sql.Timestamp;

public class HistoryController {

  @FXML
  private TableView<LoginHistoryType> historyTable;
  @FXML
  private TableColumn<LoginHistoryType, Number> userId;
  @FXML
  private TableColumn<LoginHistoryType, String> userName;
  @FXML
  private TableColumn<LoginHistoryType, Timestamp> loginDate;
  @FXML
  private TableColumn<LoginHistoryType, String> status;

  @FXML
  public void initialize() {

    userId.setCellValueFactory(data ->
            new javafx.beans.property.SimpleIntegerProperty(data.getValue().getUserId())
    );

    userName.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getUserName())
    );

    loginDate.setCellValueFactory(data ->
            new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getLoginTime())
    );

    status.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus())
    );

    loadHistory();
  }

  private void loadHistory() {
    DbConnection db = new DbConnection();
    ObservableList<LoginHistoryType> history = db.getLoginHistory();
    historyTable.setItems(history);
  }

  @FXML
  public void onBackToAdmin() throws IOException {
    Stage stage = (Stage) historyTable.getScene().getWindow();
    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("admin.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 600, 600);
    stage.setTitle("Админ панель");
    stage.setScene(scene);
    stage.show();
  }
}
