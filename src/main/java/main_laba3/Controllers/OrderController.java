package main_laba3.Controllers;

import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.stage.Modality;
import javafx.stage.Stage;
import main_laba3.DAO.DbConnection;
import main_laba3.HelloApplication;
import main_laba3.Models.BuyersType;
import main_laba3.Models.OrderType;
import main_laba3.Models.ProductType;
import main_laba3.Models.UsersType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderController {

  @FXML
  private ComboBox<String> buyerName;

  @FXML
  private ComboBox<String> productTitle;

  @FXML
  private Button orderButton;

  @FXML
  private Label message;

  @FXML
  private Button adminButton;

  @FXML
  private TableView<OrderType> orderTable;
  @FXML private TableColumn<OrderType, String> idFio;
  @FXML private TableColumn<OrderType, String> idProd;
  @FXML private TableColumn<OrderType, String> idDate;
  @FXML private TableColumn<OrderType, ImageView> idPhoto;

  public ArrayList<Integer> prod;

  public ArrayList <Integer> buyer;

  String name;
  String photo;
  int id;

  DbConnection db = null;

  @FXML
  void initialize() throws SQLException, ClassNotFoundException {
    db = new DbConnection();
    LoadProduct();
    LoadBuyer();
    LoadOrder();

    adminButton.setVisible(UsersType.isAdmin());


    message.setText("");

  }

  @FXML
  public void LoadProduct() throws SQLException , ClassNotFoundException {
    ObservableList<ProductType> tabs = db.getProduct();
    prod = new ArrayList();
    tabs.forEach(tab -> {
      productTitle.getItems().add(tab.getTitle());
      prod.add(tab.getIdproducts());
    });
  }

  @FXML
  public void LoadBuyer() throws SQLException, ClassNotFoundException {
    ObservableList<BuyersType> tabs = db.getBuyer();
    buyer = new ArrayList();
    tabs.forEach(tab -> {
      buyerName.getItems().add(tab.getFio());
      buyer.add(tab.getIdbyuer());
    });
  }

  @FXML
  void Zakaz() throws SQLException, ClassNotFoundException {

    SingleSelectionModel<String> model_buyer = buyerName.getSelectionModel();
    int end_buyer = buyer.get(model_buyer.getSelectedIndex());

    SingleSelectionModel<String> model_product = productTitle.getSelectionModel();
    int end_product = prod.get(model_product.getSelectedIndex());

    db.addOrder(end_product, end_buyer);
    LoadOrder();

    message.setText("Вы успешно оформили заказ!");
  }

  @FXML
  void LoadOrder() throws SQLException, ClassNotFoundException {
    ObservableList<OrderType> order = db.getOrder();
    orderTable.getItems().clear();
    orderTable.getItems().addAll(order);

    idFio.setCellValueFactory(new PropertyValueFactory<OrderType, String>("fio"));
    idProd.setCellValueFactory(new PropertyValueFactory<OrderType, String>("name"));
    idDate.setCellValueFactory(new PropertyValueFactory<OrderType, String>("dateOrder"));
    idPhoto.setCellValueFactory(p -> {
      String url = getClass().getResource("/main_laba3/image/") + p.getValue().getPhoto();
      Image image = new Image(url, 80, 80, false, true, true);
      return new ReadOnlyObjectWrapper<>(new ImageView(image));
    });
  }

  public void openAdminPanel() throws IOException {
    Stage stage = (Stage) buyerName.getScene().getWindow();
    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("admin.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 600, 600);
    stage.setTitle("Оформление заказа!");
    stage.centerOnScreen();
    stage.setScene(scene);
    stage.show();
  }

}
