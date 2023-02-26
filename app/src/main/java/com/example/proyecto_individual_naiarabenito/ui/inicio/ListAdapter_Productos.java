package com.example.proyecto_individual_naiarabenito.ui.inicio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_individual_naiarabenito.Detalles_Producto;
import com.example.proyecto_individual_naiarabenito.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListAdapter_Productos extends RecyclerView.Adapter<ListAdapter_Productos.MyViewHolder> {

    private List<Producto> lista_prod;
    private LayoutInflater inflater;    // Describir de que archivo proviene la lista
    private Context context;

    private List<Producto> lista_prod_original;

    public ListAdapter_Productos(List<Producto> lista_prod, Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.lista_prod = lista_prod;
        this.lista_prod_original = new ArrayList<>();
        this.lista_prod_original.addAll(lista_prod);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.producto_cardview, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.nombre.setText(lista_prod.get(position).getNombre());
        holder.imagen.setImageResource(lista_prod.get(position).getImg_id());

        // Añadir un Listener para detectar la pulsación al CardView
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Detalles_Producto.class);

                // Pasarle los datos del producto a la nueva Actividad
                intent.putExtra("nombreProducto",lista_prod.get(position).getNombre());
                intent.putExtra("decripProducto",lista_prod.get(position).getDescripcion());
                intent.putExtra("imgProducto",lista_prod.get(position).getImg_id());
                context.startActivity(intent);
            }
        });
    }

    // Método que filtra la lista de productos con el String recibido
    public void filtrado(String txtBuscar){
        int l = txtBuscar.length();
        if(l == 0){
            this.lista_prod.clear();
            lista_prod.addAll(lista_prod_original);
        } else{
            // Comprobar si la version del dispositivo soporta la operación de filtrado
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                List<Producto> collection = lista_prod.stream().filter(i -> i.getNombre().toLowerCase().contains(txtBuscar.toLowerCase())).collect(Collectors.toList());
                lista_prod.clear();
                lista_prod.addAll(collection);
            }
            else {      // Si no la soporta, realizaremos el filtrado de otra manera
                for(Producto prod: lista_prod_original){
                    if(prod.getNombre().toLowerCase().contains(txtBuscar.toLowerCase())){
                        lista_prod.add(prod);
                    }
                }
            }
        }
        // Indicar que estamos relizando cambios
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return lista_prod.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nombre;
        ImageView imagen;
        CardView cardView;

        public MyViewHolder(View itemView){
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.nombre_producto_rv);
            imagen = (ImageView) itemView.findViewById(R.id.img_producto_rv);
            cardView = (CardView) itemView.findViewById(R.id.producto_cardview);
        }
    }

}
