package main_laba3.Controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main_laba3.db.DbConnection;
import main_laba3.HelloApplication;
import main_laba3.Models.OrderType;
import main_laba3.Models.ProductType;

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

  @FXML
  private TableView<OrderType> orderTable;
  @FXML private TableColumn<OrderType, String> idFio;
  @FXML private TableColumn<OrderType, String> idProd;
  @FXML private TableColumn<OrderType, String> idDate;
  @FXML private TableColumn<OrderType, ImageView> idPhoto;

  private final DbConnection db = new DbConnection();

  private File selectedImageFile;

  @FXML
  void initialize() throws SQLException, ClassNotFoundException {
    LoadOrder();
    message.setText("");
  }

  @FXML
  void LoadOrder() throws SQLException, ClassNotFoundException {
    ObservableList<OrderType> order = db.getOrder();
    orderTable.getItems().clear();
    orderTable.getItems().addAll(order);

    idProd.setCellValueFactory(new PropertyValueFactory<OrderType, String>("name"));
    idPhoto.setCellValueFactory(p -> {
      String url = getClass().getResource("/main_laba3/image/") + p.getValue().getPhoto();
      Image image = new Image(url, 80, 80, false, true, true);
      return new ReadOnlyObjectWrapper<>(new ImageView(image));
    });

    ContextMenu rowMenu = new ContextMenu();
    MenuItem editItem = new MenuItem("Редактировать продукт");
    MenuItem delItem = new MenuItem("Удалит продукт");
    rowMenu.getItems().add(editItem);
    rowMenu.getItems().add(delItem);

    orderTable.setRowFactory(tv -> {
      TableRow<OrderType> row = new TableRow<>();
      row.contextMenuProperty().bind(
              Bindings.when(row.emptyProperty())
                      .then((ContextMenu) null)
                      .otherwise(rowMenu)
      );
      return row;
    });

    editItem.setOnAction(e -> {
      OrderType selectedOrder = orderTable.getSelectionModel().getSelectedItem();
      if (selectedOrder != null) {
        try {

          int productId = db.getProductIdByOrder(selectedOrder.getId());
          ProductType product = db.getProductById(productId);

          FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("edit.fxml"));
          Parent root = fxmlLoader.load();

          main_laba3.Controllers.EditController controller = fxmlLoader.getController();
          controller.initData(product);

          Stage stage = new Stage();
          stage.initModality(Modality.APPLICATION_MODAL);
          stage.setTitle("Редактирование продукта");
          stage.setScene(new Scene(root));
          stage.showAndWait();

          LoadOrder();
        } catch (IOException | SQLException | ClassNotFoundException ex) {
          ex.printStackTrace();
        }
      }
    });
    delItem.setOnAction(e -> {
      OrderType selectedOrder = orderTable.getSelectionModel().getSelectedItem();
      if (selectedOrder != null) {
        try {

          int productId = db.getProductIdByOrder(selectedOrder.getId());
          ProductType product = db.getProductById(productId);

          db.deleteItem(productId);



        } catch (SQLException | ClassNotFoundException ex) {
          ex.printStackTrace();
        }
      }
    });
  }

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

  public void onToHistory() throws SQLException, ClassNotFoundException, IOException {
    Stage stage = (Stage) orderTable.getScene().getWindow();
    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login_history.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 600, 600);
    stage.setTitle("История");
    stage.centerOnScreen();
    stage.setScene(scene);
    stage.show();
  }
}
