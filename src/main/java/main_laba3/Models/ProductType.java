package main_laba3.Models;

public class ProductType {
  private int idproducts;
  private String title;

//  public ProductType() {
//
//  }

  public ProductType(int idproducts, String title) {
    this.idproducts = idproducts;
    this.title = title;
  }

  public Integer getIdproducts() {
    return idproducts;
  }
  public String getTitle() {
    return title;
  }

  public void setIdproducts(int idproducts) {
    this.idproducts = idproducts;
  }
  public void setTitle(String title) {
    this.title = title;
  }

}
