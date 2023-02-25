package com.example.proyecto_individual_naiarabenito.ui.inicio;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_individual_naiarabenito.ListAdapter_Categorias;
import com.example.proyecto_individual_naiarabenito.Categoria;

import java.util.ArrayList;
import java.util.List;

public class InicioViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public InicioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}