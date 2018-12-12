package com.myfoodapp.seminarioproyect.myfoodapp.items;

public class ItemListRes {
    //private String idrestaurant;
    private String picture;
    private String name;

    public ItemListRes (String picture, String name){
        this.name = name;
        this.picture = picture;
    }

    public String getPicture(){
        return this.picture;
    }

    public String getName(){
        return this.name;
    }

}
