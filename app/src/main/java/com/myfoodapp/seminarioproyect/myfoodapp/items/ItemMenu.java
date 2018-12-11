package com.myfoodapp.seminarioproyect.myfoodapp.items;


public class ItemMenu{

    private String id,idrestaurant, name, picture;
    private Double price;

    public ItemMenu(String idrestaurant, String name, String picture, Double price,String id) {
        this.idrestaurant = idrestaurant;
        this.name = name;
        this.picture = picture;
        this.price = price;
        this.id=id;
    }

    public String getIdrestaurant() {
        return idrestaurant;
    }

    public void setIdrestaurant(String idrestaurant) {
        this.idrestaurant = idrestaurant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}