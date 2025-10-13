package main_laba3.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main_laba3.DAO.DbConnection;
import main_laba3.HelloApplication;

import java.io.IOException;
import java.sql.SQLException;

public class HelloController {

    @FXML
    private TextField idLogin;

    @FXML
    private Button idButtonLogin;

    @FXML
    private PasswordField idPassword;

    @FXML
    private Button idButtonRegistr;

    @FXML
    private Label welcomeText;
    DbConnection db = null;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        db = new DbConnection();
        welcomeText.setText("");

    }

    @FXML
    protected void onLoginButtonClick() throws  IOException {
        String login = idLogin.getText();
        String pass = idPassword.getText();

        if (login.isEmpty() || pass.isEmpty()) {
            welcomeText.setText("Есть пустые поля!");
            return;
        }

        try {
            int a=db.getUser(idLogin.getText(),idPassword.getText());
            if (a>0) {
              openOrderWindow();}
            else {welcomeText.setText("Такого пользователя нет");};
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onRegistrButtonClick() throws IOException {
        Stage stage = (Stage) idLogin.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("reg.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Регистрация!");
        stage.setScene(scene);
        stage.show();
    }

    public void openOrderWindow() throws SQLException, ClassNotFoundException, IOException {
      Stage stage = (Stage) idLogin.getScene().getWindow();
      FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("order.fxml"));
      Scene scene = new Scene(fxmlLoader.load(), 350, 280);
      stage.setTitle("Оформление заказа!");
      stage.setScene(scene);
      stage.show();
    }
}
