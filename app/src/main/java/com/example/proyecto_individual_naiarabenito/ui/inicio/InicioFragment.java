package com.example.proyecto_individual_naiarabenito.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_individual_naiarabenito.Categoria;
import com.example.proyecto_individual_naiarabenito.ListAdapter_Categorias;
import com.example.proyecto_individual_naiarabenito.R;
import com.example.proyecto_individual_naiarabenito.databinding.FragmentInicioBinding;

import java.util.ArrayList;
import java.util.List;

public class InicioFragment extends Fragment {

    ListAdapter_Categorias adapter;
    RecyclerView recyclerView;
    List<Categoria> lista_cat;

    private FragmentInicioBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        InicioViewModel inicioViewModel = new ViewModelProvider(this).get(InicioViewModel.class);

        binding = FragmentInicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textHome;
        //inicioViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        View view = inflater.inflate(R.layout.fragment_inicio,container,false);
        recyclerView = view.findViewById(R.id.lista_categorias);

        cargarLista();
        mostrarDatos();
        return view;
        //return root;
    }


    private void cargarLista() {
        lista_cat = new ArrayList<>();
        lista_cat.add(new Categoria("Pizza","#775447",R.drawable.cat_pizza));
        lista_cat.add(new Categoria("Hamburguesa","#776927",R.drawable.confi_menu));
        lista_cat.add(new Categoria("Kebab","#629078",R.drawable.confi_menu));
        lista_cat.add(new Categoria("Burrito","#494771",R.drawable.confi_menu));
        lista_cat.add(new Categoria("Sandwitch","#573654",R.drawable.confi_menu));
        lista_cat.add(new Categoria("Bocadillo","#593862",R.drawable.confi_menu));

    }
    private void mostrarDatos() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        adapter = new ListAdapter_Categorias(lista_cat,getContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}