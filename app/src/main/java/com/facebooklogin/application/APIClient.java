package com.facebooklogin.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static APIClient instance = null;
    private Api myApi;
    private APIClient()
    {
        Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        myApi = retrofit.create(Api.class);
    }
    public static synchronized APIClient getInstance(){
        if (instance == null)
        {
            instance = new APIClient();
        }
        return instance;
    }
    public Api getMyApi(){
        return myApi;
    }

}
