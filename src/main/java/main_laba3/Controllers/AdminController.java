package main_laba3.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main_laba3.DAO.DbConnection;
import main_laba3.HelloApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

public class AdminController {

  @FXML
  private Button newValue;

  @FXML
  private Button backToOrder;

  @FXML
  private TextField newBuyers;

  @FXML
  private TextField newProduct;

  @FXML
  private Label message;

  @FXML
  private ImageView productPreview;

  @FXML
  private Label selectedImageName;

  private final DbConnection db = new DbConnection();

  private File selectedImageFile;

  @FXML
  private void addNewValue() throws SQLException, ClassNotFoundException, IOException {
    String productName = newProduct.getText().trim();

    if (productName.isEmpty()) {
      message.setText("Заполните поле товара");
      return;
    }

    if (selectedImageFile == null) {
      message.setText("Выберите изображение для товара");
      return;
    }

    String imageName = selectedImageFile.getName();

    File destDir = new File("src/main/resources/main_laba3/image");
    if (!destDir.exists()) destDir.mkdirs();

    File destFile = new File(destDir, imageName);
    Files.copy(selectedImageFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

    db.addNewProduct(productName, imageName);

    message.setText("Вы успешно добавили товар: " + productName);

    newProduct.clear();
    productPreview.setImage(null);
    selectedImageName.setText("");
    selectedImageFile = null;
  }

  @FXML
  private void onChooseImage() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Выберите изображение товара");
    fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Изображения", "*.jpg", "*.jpeg", "*.png")
    );

    File file = fileChooser.showOpenDialog(null);
    if (file != null) {
      selectedImageFile = file;
      selectedImageName.setText(file.getName());
      productPreview.setImage(new Image(file.toURI().toString()));
    }
  }

  @FXML
  public void onBackToOrder() throws SQLException, ClassNotFoundException, IOException {
    Stage stage = (Stage) newProduct.getScene().getWindow();
    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("order.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 600, 420);
    stage.setTitle("Оформление заказа!");
    stage.centerOnScreen();
    stage.setScene(scene);
    stage.show();
  }
}
