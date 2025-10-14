package main_laba3.Models;

public class OrderType {

  private String fio;
  private String name;
  private String dateOrder;
  private String photo;

  public OrderType(String fio, String name, String dateOrder, String photo) {
    this.fio = fio;
    this.name = name;
    this.dateOrder = dateOrder;
    this.photo = photo;
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
}
