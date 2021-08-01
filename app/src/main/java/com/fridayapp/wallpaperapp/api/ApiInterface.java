package com.fridayapp.wallpaperapp.api;

import com.fridayapp.wallpaperapp.model.Category;
import com.fridayapp.wallpaperapp.model.CategoryCount;
import com.fridayapp.wallpaperapp.model.WallpaperModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @GET("api.php")
    Call<ArrayList<WallpaperModel>> getData();

    @GET("category_api.php")
    Call<ArrayList<Category>> getCategory();

    @FormUrlEncoded
    @POST("searchFilter.php")
    Call<List<WallpaperModel>> searchData(@Field("query") String query);

    @FormUrlEncoded
    @POST("category_count.php")
    Call<List<CategoryCount>> searchCategoryCount(@Field("query") String query);
}