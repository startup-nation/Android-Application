package com.example.bookmymeal.Backend.MVVM;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bookmymeal.Backend.FAQEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FAQRepository {
    private Api api;
    private MutableLiveData<List<FAQEntity>> faqList;

    public FAQRepository(){
        api=RetrofitRequest.getRetrofitInstance().create(Api.class);
    }

    public LiveData<List<FAQEntity>> getFAQList(){
        if(faqList==null){
            faqList=new MutableLiveData<>();
        }

        api.getAll().enqueue(new Callback<List<FAQEntity>>() {
            @Override
            public void onResponse(Call<List<FAQEntity>> call, Response<List<FAQEntity>> response) {
                System.out.println(response.toString());
                System.out.println(response.body().size());
                faqList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<FAQEntity>> call, Throwable t) {
                faqList.setValue(null);
            }
        });

        return faqList;
    }
}
