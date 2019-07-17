package com.avraam.smartlist.models;

public class Product {
    private String description;
    private String barocde;
    private String added_Date;


    public Product(){
        //Empty constructor needed
    }

    public Product(String description, String barocde, String added_Date) {
        this.description = description;
        this.barocde = barocde;
        this.added_Date = added_Date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBarocde(String barocde) {
        this.barocde = barocde;
    }

    public void setAdded_Date(String added_Date) {
        this.added_Date = added_Date;
    }

    public String getDescription() {
        return description;
    }

    public String getBarocde() {
        return barocde;
    }

    public String getAdded_Date() {
        return added_Date;
    }
}
