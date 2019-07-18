package com.avraam.smartlist.models;

public class Product {
    private String Product_Name;
    private String Barocde;
    private String Date_Added;


    public Product(){
        //Empty constructor needed
    }

    public Product( String added_Date, String barocde,String title) {
        this.Product_Name = title;
        this.Barocde = barocde;
        this.Date_Added = added_Date;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public String getBarocde() {
        return Barocde;
    }

    public String getDate_Added() {
        return Date_Added;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public void setBarocde(String barocde) {
        Barocde = barocde;
    }

    public void setDate_Added(String date_Added) {
        Date_Added = date_Added;
    }
}
