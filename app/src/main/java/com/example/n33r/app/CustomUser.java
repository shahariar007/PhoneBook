package com.example.n33r.app;

import android.graphics.Bitmap;

/**
 * Created by N33R on 6/19/2015.
 */
public class CustomUser {
    String name;
    String phoneNumber;
    Bitmap image;

    public CustomUser(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;

    }

public CustomUser() {
    }

    public CustomUser(String name, String phoneNumber, Bitmap image) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Bitmap getImage() {
        return image;
    }


}
