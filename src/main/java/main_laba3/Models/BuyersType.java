package main_laba3.Models;

public class BuyersType {

  private int idbuyer;
  private String fio;

  public BuyersType() {

  }

  public BuyersType(int idbuyer, String fio) {
    this.idbuyer = idbuyer;
    this.fio = fio;
  }

  public int getIdbyuer() {
    return idbuyer;
  }
  public String getFio() {
    return fio;
  }

  public void setIdbyuer(int idbuyer) {
    this.idbuyer = idbuyer;
  }
  public void setFio(String fio) {
    this.fio = fio;
  }

}

