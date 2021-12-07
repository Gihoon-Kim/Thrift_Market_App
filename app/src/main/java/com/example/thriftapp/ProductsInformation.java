package com.example.thriftapp;

public class ProductsInformation {

    private String productNumber;
    private String productName;
    private String productDesc;
    private String productOwner;
    private String productPrice;
    private String tradeLocation;

    public String getTradeLocation() {
        return tradeLocation;
    }

    public void setTradeLocation(String tradeLocation) {
        this.tradeLocation = tradeLocation;
    }

    public ProductsInformation(String productNumber, String productName, String productDesc, String  productOwner, String productPrice, String tradeLocation) {

        this.productNumber = productNumber;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productOwner = productOwner;
        this.productPrice = productPrice;
        this.tradeLocation = tradeLocation;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductOwner() {
        return productOwner;
    }

    public void setProductOwner(String productOwner) {
        this.productOwner = productOwner;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}
