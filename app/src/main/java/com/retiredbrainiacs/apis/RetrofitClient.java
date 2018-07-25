package com.retiredbrainiacs.apis;

import com.retiredbrainiacs.common.GlobalConstants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit =null;
    public static Retrofit getClient(String url){
if(retrofit == null){
    retrofit = new Retrofit.Builder().baseUrl(GlobalConstants.API_URL1).addConverterFactory(GsonConverterFactory.create()).build();
}
return retrofit;
    }
}
