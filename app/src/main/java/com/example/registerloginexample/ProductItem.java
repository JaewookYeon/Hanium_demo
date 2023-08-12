package com.example.registerloginexample;

import android.net.Uri;

public class ProductItem {
    private int refId;
    private String productName;
    private String quantity;
    private String expiryDate;
    private Uri imageUri;

    public ProductItem(Integer refId,String productName, String quantity, String expiryDate, Uri imageUri) {
        this.refId=refId;
        this.productName = productName;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.imageUri = imageUri;
    }

    public Integer getRefId(){return refId;}
    public String getProductName() {
        return productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public Uri getImageUri() {
        return imageUri;
    }



}
