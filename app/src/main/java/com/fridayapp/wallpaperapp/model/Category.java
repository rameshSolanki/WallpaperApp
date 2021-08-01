package com.fridayapp.wallpaperapp.model;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("category_name")
    private String category_name;

    @SerializedName("category_image")
    private String category_image;

    public Category(String category_name, String category_image) {
        this.category_name = category_name;
        this.category_image = category_image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getCategory_image() {
        return category_image;
    }
}

