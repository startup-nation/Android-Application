package com.example.bookmymeal.Backend.MVVM;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bookmymeal.Backend.FAQEntity;

import java.util.List;

public class FAQModelView extends AndroidViewModel {
    private FAQRepository faqRepository;
    private LiveData<List<FAQEntity>>faqList;

    public FAQModelView(@NonNull Application application) {
        super(application);
        faqRepository=new FAQRepository();
        this.faqList=faqRepository.getFAQList();
    }

    public LiveData<List<FAQEntity>>getFAQLiveData(){
        return faqList;
    }
}
