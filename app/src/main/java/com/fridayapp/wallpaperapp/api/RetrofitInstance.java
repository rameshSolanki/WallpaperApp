package com.fridayapp.wallpaperapp.api;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit = null;
    public static final String BASE_URL = "https://app0209.000webhostapp.com/wallpapers/";
    static Context c;
    public static final String CACHE_CONTROL = "Cache-Control";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(provideOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;

    }

    private static OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        okhttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);
        return okhttpClientBuilder.build();
    }

}