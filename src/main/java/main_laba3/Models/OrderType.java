package main_laba3.Models;

public class OrderType {

  private String fio;
  private String name;
  private String dateOrder;
  private String photo;
  private int id;

  private ProductType product;
  public ProductType getProduct() { return product; }
  public void setProduct(ProductType product) { this.product = product; }

  public OrderType(String fio, String name, String dateOrder, String photo, int id) {
    this.fio = fio;
    this.name = name;
    this.dateOrder = dateOrder;
    this.photo = photo;
    this.id = id;
  }

  public String getFio() {
    return fio;
  }
  public String getName() {
    return name;
  }
  public String getDateOrder() {
    return dateOrder;
  }
  public String getPhoto() {
    return photo;
  }
  public int getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }
  public void setDateOrder(String dateOrder) {
    this.dateOrder = dateOrder;
  }
  public void setPhoto(String photo) {
    this.photo = photo;
  }
  public void setFio(String fio) {
    this.fio = fio;
  }
  public void setId(int id) {
    this.id = id;
  }
}
