package main_laba3.Models;

public class ProductType {
  private int idproducts;
  private String title;
  private String photo;

  public ProductType() {
  }

  public ProductType(int idproducts, String title, String photo) {
    this.idproducts = idproducts;
    this.title = title;
    this.photo = photo;
  }

  public Integer getIdproducts() {
    return idproducts;
  }
  public String getTitle() {
    return title;
  }
  public String getPhoto() {
    return photo;
  }

  public void setIdproducts(int idproducts) {
    this.idproducts = idproducts;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public void setPhoto(String photo) {
    this.photo = photo;
  }

}
