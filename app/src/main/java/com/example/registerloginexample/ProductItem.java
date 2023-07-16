package com.example.registerloginexample;

import android.net.Uri;

public class ProductItem {
    private String productName;
    private String quantity;
    private String expiryDate;
    private Uri imageUri;

    public ProductItem(String productName, String quantity, String expiryDate, Uri imageUri) {
        this.productName = productName;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.imageUri = imageUri;
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

    public Uri getImageUri() {
        return imageUri;
    }

    public Uri getImageResId() {
            return getImageUri();
    }

}
