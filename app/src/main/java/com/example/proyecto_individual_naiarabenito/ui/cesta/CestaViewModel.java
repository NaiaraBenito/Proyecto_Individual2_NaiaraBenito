package com.example.proyecto_individual_naiarabenito.ui.cesta;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CestaViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CestaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}