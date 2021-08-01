package com.fridayapp.wallpaperapp.model;

import com.google.gson.annotations.SerializedName;

public class CategoryCount {
    @SerializedName("type_of_category")
    private String type_of_category;

    public CategoryCount(String type_of_category) {
        this.type_of_category = type_of_category;
    }

    public String getType_of_category() {
        return type_of_category;
    }

    public void setType_of_category(String type_of_category) {
        this.type_of_category = type_of_category;
    }
}
