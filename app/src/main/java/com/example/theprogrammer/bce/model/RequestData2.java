package com.example.theprogrammer.bce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestData2 {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("faceFeatures")
    @Expose
    private String faceFeatures;

    public String getFaceFeatures() {
        return faceFeatures;
    }

    public void setFaceFeatures(String faceFeatures) {
        this.faceFeatures = faceFeatures;
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