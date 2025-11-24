package main_laba3.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main_laba3.db.DbConnection;
import main_laba3.Models.ProductType;

import java.io.File;
import java.sql.SQLException;

public class EditController {

  @FXML private TextField productNameField;
  @FXML private TextField productPhotoField;
  @FXML private ImageView productImage;

  private ProductType currentProduct;
  private String selectedImagePath;

  // Получаем данные продукта
  public void initData(ProductType product) {
    currentProduct = product;
    productNameField.setText(product.getTitle());
    productPhotoField.setText(product.getPhoto());

    try {
      String url = getClass().getResource("/main_laba3/image/" + product.getPhoto()).toExternalForm();
      productImage.setImage(new Image(url));
    } catch (Exception e) {
      productImage.setImage(null);
    }
  }

  // Выбор изображения
  @FXML
  private void chooseImage() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Выберите изображение");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.jpeg"));
    File file = fileChooser.showOpenDialog(productImage.getScene().getWindow());
    if (file != null) {
      selectedImagePath = file.getAbsolutePath();
      productPhotoField.setText(file.getName());
      productImage.setImage(new Image(file.toURI().toString()));
    }
  }

  // Сохранение изменений
  @FXML
  private void saveChanges() throws SQLException, ClassNotFoundException {
    if (currentProduct != null) {
      currentProduct.setTitle(productNameField.getText());
      if (selectedImagePath != null) currentProduct.setPhoto(new File(selectedImagePath).getName());

      DbConnection db = new DbConnection();
      db.updateProduct(currentProduct.getIdproducts(), currentProduct.getTitle(), currentProduct.getPhoto());

      closeWindow();
    }
  }

  @FXML
  private void closeWindow() {
    Stage stage = (Stage) productImage.getScene().getWindow();
    stage.close();
  }
}
