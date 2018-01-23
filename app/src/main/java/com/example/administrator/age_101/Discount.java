package com.example.administrator.age_101;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.view.View;

public class Discount {

    private String text;
    private String urlLogo;
    private String urlImage;


    public Discount(String text, String urlLogo, String urlImage){
        this.text = text;
    }

    public Discount(String text){
        this.text = text;
    }

    public String getIsim() {
        return text;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public String getUrlImage() {
        return urlImage;
    }
}
