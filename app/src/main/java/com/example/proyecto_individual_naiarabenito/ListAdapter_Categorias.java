package com.example.proyecto_individual_naiarabenito;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter_Categorias extends RecyclerView.Adapter<ListAdapter_Categorias.ViewHolder> implements View.OnClickListener{
    private List<Categoria> lista_cat;
    private LayoutInflater inflater;    // Describir de que archivo proviene la lista
    private Context context;

    private View.OnClickListener listener;

    public ListAdapter_Categorias(List<Categoria> list_ele, Context context){
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.lista_cat = list_ele;
    }

    @Override
    public int getItemCount(){
        return lista_cat.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.lista_categorias,parent,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        String nombre = lista_cat.get(position).getName();
        int img = lista_cat.get(position).getImg_id();
        holder.bindData(lista_cat.get(position));
        holder.name.setText(nombre);
        holder.img_icon.setImageResource(img);
    }

    public void setItems(List<Categoria> items){
        lista_cat = items;
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }
    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img_icon;
        TextView name;

        ViewHolder(View itemView){
            super(itemView);
            img_icon = itemView.findViewById(R.id.cat_img);
            name = itemView.findViewById(R.id.cat_tv);
        }

        // Método que transfiere la información de la lista de categorias a la vista
        void bindData(final Categoria item){
            img_icon.setColorFilter(Color.parseColor(item.getColor()), PorterDuff.Mode.SRC_IN);
            name.setText(item.getName());
        }
    }
}
