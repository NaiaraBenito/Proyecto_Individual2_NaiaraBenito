package com.example.proyecto_individual_naiarabenito.ui.inicio;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_individual_naiarabenito.R;

import java.util.List;

public class ListAdapter_Categorias extends RecyclerView.Adapter<ListAdapter_Categorias.ViewHolder> {
    private List<Categoria> lista_cat;
    private LayoutInflater inflater;    // Describir de que archivo proviene la lista
    private Context context;

    public ListAdapter_Categorias(List<Categoria> list_ele, Context context){
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.lista_cat = list_ele;
    }

    //  Método para aplicar el onClick
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.categoria_cardview,parent,false);
        return new ViewHolder(view);
    }

    // Método que transfiere la información de la lista de categorias a la vista
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        holder.bindData(lista_cat.get(position));
    }

    @Override
    public int getItemCount(){
        return lista_cat.size();
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
            name.setText(item.getName());
            name.setTextColor(Color.parseColor(item.getColor()));
            img_icon.setImageResource(item.getImg_id());
        }
    }
}
