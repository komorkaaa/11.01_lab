package main_laba3.Controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main_laba3.DAO.DbConnection;
import main_laba3.Models.BuyersType;
import main_laba3.Models.OrderType;
import main_laba3.Models.ProductType;

import java.sql.SQLException;
import java.util.ArrayList;

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
  private TableView<OrderType> orderTable;
  @FXML private TableColumn<OrderType, String> idFio;
  @FXML private TableColumn<OrderType, String> idProd;
  @FXML private TableColumn<OrderType, String> idDate;

  public ArrayList<Integer> prod;

  public ArrayList <Integer> buyer;

  DbConnection db = null;

  @FXML
  void initialize() throws SQLException, ClassNotFoundException {
    db = new DbConnection();
    LoadProduct();
    LoadBuyer();
    LoadOrder();
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


  }

}
