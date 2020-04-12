package com.example.bookmymeal.Backend.MVVM;

import com.example.bookmymeal.Backend.FAQEntity;
import com.example.bookmymeal.Backend.MyUrl;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    String BaseUrl= MyUrl.ApiUrl+"/";

    @GET("FAQ")
    Call<List<FAQEntity>> getAll();

}
