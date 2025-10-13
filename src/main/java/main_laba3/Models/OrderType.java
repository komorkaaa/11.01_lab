package main_laba3.Models;

public class OrderType {

  private String fio;
  private String name;
  private String dateOrder;

  public OrderType(String fio, String name, String dateOrder) {
    this.fio = fio;
    this.name = name;
    this.dateOrder = dateOrder;
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
}
