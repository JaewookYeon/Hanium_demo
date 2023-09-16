package com.example.registerloginexample;


import android.graphics.Bitmap;

public class ProductItem {
    private int refId;
    private String productName;
    private String quantity;
    private String expiryDate;
    private Bitmap imageBitmap;


    public ProductItem(Integer refId, String productName, String quantity, String expiryDate, Bitmap imageUri) {
        this.refId = refId;
        this.productName = productName;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.imageBitmap = imageBitmap;
    }

    public Integer getRefId() {
        return refId;
    }

    public String getProductName() {
        return productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap; // 이미지 비트맵 반환
    }
}
