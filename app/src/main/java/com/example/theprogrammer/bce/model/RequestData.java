package com.example.theprogrammer.bce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestData {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("facialfeature")
    @Expose
    private String facialfeature;
    @SerializedName("Token")
    @Expose
    private String Token;

    public String getFacialfeature() {
        return facialfeature;
    }

    public void setFacialfeature(String facialfeature) {
        this.facialfeature = facialfeature;
    }


/*
    public void setPhoto(byte[] photo) {
        for (byte b : photo) {
            this.photo.add(b);
        }
    }
*/

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}