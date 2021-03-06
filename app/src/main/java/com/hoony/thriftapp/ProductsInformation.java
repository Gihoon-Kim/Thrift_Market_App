package com.hoony.thriftapp;

import android.graphics.Bitmap;

public class ProductsInformation {

    private String productNumber;
    private String productName;
    private String productDesc;
    private String productOwner;
    private String productPrice;
    private String tradeLocation;
    private String status;
    private String addedDate;
    private int productOwnerNumber;

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    private Bitmap imageBitmap;

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTradeLocation() {
        return tradeLocation;
    }

    public void setTradeLocation(String tradeLocation) {
        this.tradeLocation = tradeLocation;
    }

    public ProductsInformation(
            String productNumber,
            String productName,
            String productDesc,
            String  productOwner,
            String productPrice,
            String tradeLocation,
            String addedDate,
            int productOwnerNumber,
            Bitmap imageBitmap
    ) {

        this.productNumber = productNumber;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productOwner = productOwner;
        this.productPrice = productPrice;
        this.tradeLocation = tradeLocation;
        this.addedDate = addedDate;
        this.productOwnerNumber = productOwnerNumber;
        this.imageBitmap = imageBitmap;
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

    public int getProductOwnerNumber() {
        return productOwnerNumber;
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
