package main_laba3.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main_laba3.Models.BuyersType;
import main_laba3.Models.OrderType;
import main_laba3.Models.ProductType;
import main_laba3.Models.UsersType;

import java.sql.*;
public class DbConnection {
    private final String HOST = "localhost";
    private final String PORT = "5432";
    private final String DB_NAME = "postgres";
    private final String LOGIN = "postgres"; // Если OpenServer, то здесь mysql напишите
    private final String PASS = "postgres"; // Если OpenServer, то здесь mysql напишите

    private Connection dbConn = null;
    private Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME+"?currentSchema=public";
        Class.forName("org.postgresql.Driver");

        dbConn = DriverManager.getConnection(connStr, LOGIN, PASS);
        return dbConn;
    }

//    public int getUser(String loginu, String passwordu) throws SQLException, ClassNotFoundException {
//        String sql = "SELECT COUNT(*) as count FROM users WHERE login = ? AND password= ?";
//
//        PreparedStatement statement = getDbConnection().prepareStatement(sql);
//        statement.setString(1, loginu);
//        statement.setString(2, passwordu);
//
//        ResultSet res = statement.executeQuery();
//
//        int count = 0;
//        if (res.next()) {
//            count = res.getInt("count");
//        }
//        return count;
//    }


    public void addUser(String fio, String loginu,String passwordu) throws SQLException, ClassNotFoundException {
        String sql = "insert into users (fio, login, password) values (?, ?, ?)";

        PreparedStatement prstm = getDbConnection().prepareStatement(sql);
        prstm.setString(1, fio);
        prstm.setString(2, loginu);
        prstm.setString(3, passwordu);
        prstm.executeUpdate();
    }

    public void addOrder(Integer idproduct, Integer idbuyer) throws SQLException, ClassNotFoundException {
      String sql = "insert into product_has_buyers (product_idproducts, buyer_idbuyers) values (?, ?)";

      PreparedStatement prstm = getDbConnection().prepareStatement(sql);
      prstm.setInt(1, idproduct);
      prstm.setInt(2, idbuyer);
      prstm.executeUpdate();
    }

    public ObservableList<ProductType> getProduct() throws SQLException, ClassNotFoundException {
      ObservableList<ProductType> ls = FXCollections.observableArrayList();

      String sql = "select idproducts, title from products";

      Statement statement = getDbConnection().createStatement();

      ResultSet res = statement.executeQuery(sql);

      while (res.next()) {
        Integer idproducts = res.getInt("idproducts");
        String title = res.getString("title");

        ls.add(new ProductType(idproducts, title));
      }
      return ls;
    }

  public ObservableList<BuyersType> getBuyer() throws SQLException, ClassNotFoundException {
    ObservableList<BuyersType> ls = FXCollections.observableArrayList();

    String sql = "select idbuyer, fio from buyers";

    Statement statement = getDbConnection().createStatement();

    ResultSet res = statement.executeQuery(sql);

    while (res.next()) {
      Integer idbyuer = res.getInt("idbuyer");
      String fio = res.getString("fio");

      ls.add(new BuyersType(idbyuer, fio));
    }
    return ls;
  }

  public ObservableList<OrderType> getOrder() throws SQLException, ClassNotFoundException {
      ObservableList<OrderType> ls = FXCollections.observableArrayList();
      String sql = "select title, fio, date_of_byuer, photo from products p,  buyers b, product_has_buyers phb where p.idproducts = phb.product_idproducts and b.idbuyer = phb.buyer_idbuyers;";
      Statement statement = getDbConnection().createStatement();
      ResultSet res = statement.executeQuery(sql);
      while (res.next()) {
        String fio = res.getString("fio");
        String name = res.getString("title");
        String dateOrder = res.getString("date_of_byuer");
        String photo = res.getString("photo");
        ls.add(new OrderType(fio, name, dateOrder, photo));
      }
      return ls;
  }

  public UsersType findByUsername(String username) {
    String sql = "SELECT * FROM users WHERE login = ?";
    try (
            PreparedStatement st = getDbConnection().prepareStatement(sql)){
      st.setString(1, username);
      ResultSet rs = st.executeQuery();
      if (rs.next()) {
        return new UsersType(
                rs.getInt("idusers"),
                rs.getString("fio"),
                rs.getString("login"),
                rs.getString("password"),
                rs.getString("role_name")
        );
      }
    } catch (SQLException | ClassNotFoundException e) { e.printStackTrace(); }
    return null;
  }

  public UsersType authenticate(String username, String Password) {
    UsersType u = findByUsername(username);
    if (u == null) return null;
    if (Password.equals(u.getPassword()));
    return u;
  }

  public void addNewProduct(String name_product, String imageName) throws SQLException, ClassNotFoundException {
    String sql = "insert into products (title, photo) values (?, ?)";

    PreparedStatement prstm = getDbConnection().prepareStatement(sql);
    prstm.setString(1, name_product);
    prstm.setString(2, imageName);
    prstm.executeUpdate();
  }


}
