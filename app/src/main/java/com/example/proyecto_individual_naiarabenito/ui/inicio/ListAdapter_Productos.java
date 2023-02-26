package com.example.proyecto_individual_naiarabenito.ui.inicio;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_individual_naiarabenito.R;

import java.util.List;

public class ListAdapter_Productos extends BaseAdapter {

    private List<Producto> lista_prod;
    private LayoutInflater inflater;    // Describir de que archivo proviene la lista
    private Context context;

    public ListAdapter_Productos(List<Producto> lista_prod, Context context) {
        this.inflater = LayoutInflater.from(context);
        this.lista_prod = lista_prod;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lista_prod.size();
    }

    public void setItems(List<Producto> items){
        lista_prod = items;
    }

    @Override
    public Object getItem(int i) {
        return lista_prod.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Producto producto = lista_prod.get(i);

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.producto_cardview,viewGroup,false);
        }

        ImageView prod_img = view.findViewById(R.id.img_producto_gridview);
        TextView prod_nombre = view.findViewById(R.id.nombre_producto_gridview);

        int imagen = producto.getImg_id();
        String nombre = producto.getNombre();

        prod_img.setImageResource(imagen);
        prod_nombre.setText(nombre);

        return view;
    }

}
