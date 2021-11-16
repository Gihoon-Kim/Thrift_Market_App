package com.example.thriftapp;

public class ProductsInformation {

    private String productName;
    private String productDesc;
    private String productOwner;
    private String productPrice;

    public ProductsInformation(String productName, String productDesc, String  productOwner, String productPrice) {

        this.productName = productName;
        this.productDesc = productDesc;
        this.productOwner = productOwner;
        this.productPrice = productPrice;
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
