package main_laba3.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main_laba3.DAO.DbConnection;
import main_laba3.HelloApplication;

import java.io.IOException;
import java.sql.SQLException;



public class RegController {

    DbConnection db = null;
    @FXML
    private Button idBtn3;

    @FXML
    private TextField idFio;

    @FXML
    private TextField idLogin;

    @FXML
    private TextField idPassword;

    @FXML
    private Button backToLogin;

    @FXML
    private Label message;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        db = new DbConnection();
        message.setText("");

    }
    @FXML
    protected void onRegistrButtonClick() {
        String login = idLogin.getText();
        String pass = idPassword.getText();
        String fio = idFio.getText();

        if (login.isEmpty() || pass.isEmpty() || fio.isEmpty()) {
            message.setText("Есть пустые поля!");
            return;
        }


        try {
            db.addUser(idFio.getText(), idLogin.getText(), idPassword.getText());
            message.setText("Вы успешно зарегистрировались");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void openLogin() throws IOException {
        Stage stage = (Stage) idFio.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 320);
        stage.setTitle("Авторизация!");
        stage.setScene(scene);
        stage.show();
    }
}
