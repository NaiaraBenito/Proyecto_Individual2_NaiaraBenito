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

public class ListAdapter_Promocion extends RecyclerView.Adapter<ListAdapter_Promocion.ViewHolder> {
    private List<Promocion> lista_prom;
    private LayoutInflater inflater;    // Describir de que archivo proviene la lista
    private Context context;

    public ListAdapter_Promocion(List<Promocion> list_ele, Context context){
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.lista_prom = list_ele;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.promocion_cardview,parent,false);
        return new ViewHolder(view);
    }

    // Método que transfiere la información de la lista de promociones a la vista
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        holder.bindData(lista_prom.get(position));
    }

    @Override
    public int getItemCount(){
        return lista_prom.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img_icon;

        ViewHolder(View itemView){
            super(itemView);
            img_icon = itemView.findViewById(R.id.prom_img);
        }

        // Método que transfiere la información de la lista de promociones a la vista
        void bindData(final Promocion item){
            img_icon.setImageResource(item.getImg_id());
        }
    }
}
