package com.example.proyecto_individual_naiarabenito.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_individual_naiarabenito.R;

import java.util.ArrayList;
import java.util.List;

public class InicioFragment extends Fragment {

    ListAdapter_Categorias adapter;
    RecyclerView recyclerView;
    List<Categoria> lista_cat;

    //private FragmentInicioBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //InicioViewModel inicioViewModel = new ViewModelProvider(this).get(InicioViewModel.class);

        //binding = FragmentInicioBinding.inflate(inflater, container, false);
        //View root = binding.getRoot();

        //final TextView textView = binding.textHome;
        //inicioViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

       // Obtener la vista de fragment_inicio
        View view = inflater.inflate(R.layout.fragment_inicio,container,false);



        // Cargar la lista de Categorias
        recyclerView = view.findViewById(R.id.lista_categorias);
        cargarLista();
        mostrarDatos();
        return view;
        //return root;
    }


    private void cargarLista() {
        lista_cat = new ArrayList<>();
        lista_cat.add(new Categoria("Pizzas","#775447",R.drawable.cat_pizza));
        lista_cat.add(new Categoria("Hamburguesas","#776927",R.drawable.cat_hamburguesa));
        lista_cat.add(new Categoria("Kebab","#629078",R.drawable.cat_kebab));
        lista_cat.add(new Categoria("Burritos","#494771",R.drawable.cat_burrito));
        lista_cat.add(new Categoria("Sandwitches","#573654",R.drawable.cat_sandwitch));
        lista_cat.add(new Categoria("Bocadillos","#593862",R.drawable.cat_bocadillo));
        lista_cat.add(new Categoria("Postres","#689053",R.drawable.cat_postres));

    }
    private void mostrarDatos() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        adapter = new ListAdapter_Categorias(lista_cat,getContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String nombre = getActivity().getIntent().getStringExtra("nombreUsuario");
        String email = getActivity().getIntent().getStringExtra("emailUsuario");
        //String texto = getArguments().getString("emailUsuario");
        TextView emailUser = (TextView) view.findViewById(R.id.tv_emailUsuario_inicio);
        emailUser.setText("" + email);
        TextView nombreUser = (TextView) view.findViewById(R.id.tv_nombreUsuario_inicio);
        nombreUser.setText("Hola " + nombre);
    }
}