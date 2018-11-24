package com.example.theprogrammer.bce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class image {

    @SerializedName("photo")
    @Expose
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
/*
    public List<Byte> getPhoto() {
        return photo;
    }

    public void setPhoto(List<Byte> photo) {
        this.photo = photo;
    }
    //*/

}
