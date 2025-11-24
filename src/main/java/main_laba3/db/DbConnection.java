package main_laba3.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main_laba3.Models.*;

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

      String sql = "select idproducts, title, photo from products";

      Statement statement = getDbConnection().createStatement();

      ResultSet res = statement.executeQuery(sql);

      while (res.next()) {
        Integer idproducts = res.getInt("idproducts");
        String title = res.getString("title");
        String photo = res.getString("photo");

        ls.add(new ProductType(idproducts, title, photo));
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
      String sql = "select title, fio, date_of_byuer, photo, id_order  from products p,  buyers b, product_has_buyers phb where p.idproducts = phb.product_idproducts and b.idbuyer = phb.buyer_idbuyers;";
      Statement statement = getDbConnection().createStatement();
      ResultSet res = statement.executeQuery(sql);
      while (res.next()) {
        String fio = res.getString("fio");
        String name = res.getString("title");
        String dateOrder = res.getString("date_of_byuer");
        String photo = res.getString("photo");
        int idorder = res.getInt("id_order");
        ls.add(new OrderType(fio, name, dateOrder, photo, idorder));
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
                rs.getString("role_name"),
                rs.getBoolean("is_blocked"),
                rs.getInt("failed_attempts")
        );
      }
    } catch (SQLException | ClassNotFoundException e) { e.printStackTrace(); }
    return null;
  }

  public void addLoginHistory(int userId, String status) {
    String sql = "INSERT INTO login_history (user_id, status) VALUES (?, ?)";
    try (PreparedStatement st = getDbConnection().prepareStatement(sql)) {
      st.setInt(1, userId);
      st.setString(2, status);
      st.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void increaseFailedAttempts(int userId) {
    String sql = "UPDATE users SET failed_attempts = failed_attempts + 1 WHERE idusers = ?";
    try (PreparedStatement st = getDbConnection().prepareStatement(sql)) {
      st.setInt(1, userId);
      st.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void resetFailedAttempts(int userId) {
    String sql = "UPDATE users SET failed_attempts = 0 WHERE idusers = ?";
    try (PreparedStatement st = getDbConnection().prepareStatement(sql)) {
      st.setInt(1, userId);
      st.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void blockUser(int userId) {
    String sql = "UPDATE users SET is_blocked = TRUE WHERE idusers = ?";
    try (PreparedStatement st = getDbConnection().prepareStatement(sql)) {
      st.setInt(1, userId);
      st.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void unblockUser(int userId) {
    String sql = "UPDATE users SET is_blocked = FALSE, failed_attempts = 0 WHERE idusers = ?";
    try (PreparedStatement st = getDbConnection().prepareStatement(sql)) {
      st.setInt(1, userId);
      st.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public ObservableList<LoginHistoryType> getLoginHistory() {
    ObservableList<LoginHistoryType> history = FXCollections.observableArrayList();

    String query = "SELECT lh.id, u.login, lh.login_time, lh.status FROM login_history lh join users u on u.idusers = lh.user_id ORDER BY login_time DESC";

    try (PreparedStatement stmt = getDbConnection().prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        history.add(new LoginHistoryType(
                rs.getInt("id"),
                rs.getString("login"),
                rs.getTimestamp("login_time"),
                rs.getString("status")
        ));
      }

    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }

    return history;
  }

  public int getFailCount(int userId) {
    String sql = "SELECT failed_attempts FROM users WHERE idusers = ?";
    int fails = 0;

    try (Connection conn = getDbConnection();
         PreparedStatement st = conn.prepareStatement(sql)) {

      st.setInt(1, userId);
      ResultSet rs = st.executeQuery();

      if (rs.next()) {
        fails = rs.getInt("failed_attempts");
      }

    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }

    return fails;
  }

  public UsersType authenticate(String username, String password) {
    UsersType u = findByUsername(username);
    if (u == null) return null;

    if (u.isBlocked()) {
      return u;
    }

    if (password.equals(u.getPassword())) {
      resetFailedAttempts(u.getIdusers());
      addLoginHistory(u.getIdusers(), "SUCCESS");
      return u;
    } else {
      increaseFailedAttempts(u.getIdusers());
      addLoginHistory(u.getIdusers(), "FAILED");

      int fails = getFailCount(u.getIdusers());
      if (fails >= 3) {
        blockUser(u.getIdusers());
      }

      return null;
    }
  }

  public void addNewProduct(String name_product, String imageName) throws SQLException, ClassNotFoundException {
    String sql = "insert into products (title, photo) values (?, ?)";

    PreparedStatement prstm = getDbConnection().prepareStatement(sql);
    prstm.setString(1, name_product);
    prstm.setString(2, imageName);
    prstm.executeUpdate();
  }

  public void updateProduct(int productId, String newTitle, String newPhoto) throws SQLException, ClassNotFoundException {
    String sql = "UPDATE products SET title = ?, photo = ? WHERE idproducts = ?";
    PreparedStatement prstm = getDbConnection().prepareStatement(sql);
    prstm.setString(1, newTitle);
    prstm.setString(2, newPhoto);
    prstm.setInt(3, productId);
    prstm.executeUpdate();
    prstm.close();
  }

  public ProductType getProductById(int id) throws SQLException, ClassNotFoundException {
    String sql = "SELECT idproducts, title, photo FROM products WHERE idproducts = ?";
    PreparedStatement prstm = getDbConnection().prepareStatement(sql);
    prstm.setInt(1, id);
    ResultSet res = prstm.executeQuery();
    if (res.next()) {
      return new ProductType(
              res.getInt("idproducts"),
              res.getString("title"),
              res.getString("photo")
      );
    }
    return null;
  }

  public int getProductIdByOrder(int orderId) throws SQLException, ClassNotFoundException {
    String sql = "SELECT product_idproducts FROM product_has_buyers WHERE id_order = ?";
    PreparedStatement stmt = getDbConnection().prepareStatement(sql);
    stmt.setInt(1, orderId);
    ResultSet res = stmt.executeQuery();
    if (res.next()) {
      return res.getInt("product_idproducts");
    }
    return -1; // если не найден
  }

  public int deleteItem(int id) throws SQLException, ClassNotFoundException {
      String sql = "DELETE FROM products WHERE idproducts = ?";
      PreparedStatement stmt = getDbConnection().prepareStatement(sql);
      stmt.setInt(1, id);
      return stmt.executeUpdate();
    }


}
