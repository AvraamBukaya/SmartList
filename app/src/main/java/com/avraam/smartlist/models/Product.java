package com.avraam.smartlist.models;

public class Product {
    private String Product_Name;
    private String Barcode;
    private String Date_Added;
    private String Price;


    public Product(){
        //Empty constructor needed
    }



    public Product(String added_Date, String barcode, String title, String price) {
        this.Product_Name = title;
        this.Barcode = barcode;
        this.Price = price;
        this.Date_Added = added_Date;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public String getBarcode() {
        return Barcode;
    }

    public String getDate_Added() {
        return Date_Added;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public void setDate_Added(String date_Added) {
        Date_Added = date_Added;
    }
    public void setPrice(String price) {
        this.Price = price;
    }

    public String getPrice() {
        return this.Price;
    }
}
