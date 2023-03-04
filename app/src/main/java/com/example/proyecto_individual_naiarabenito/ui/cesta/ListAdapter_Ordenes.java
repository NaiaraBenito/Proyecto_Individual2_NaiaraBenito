package com.example.proyecto_individual_naiarabenito.ui.cesta;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_individual_naiarabenito.Menu_Principal;
import com.example.proyecto_individual_naiarabenito.R;
import com.example.proyecto_individual_naiarabenito.db.DBHelper;
import com.example.proyecto_individual_naiarabenito.ui.inicio.Detalles_Producto;
import com.example.proyecto_individual_naiarabenito.ui.inicio.Promocion;

import java.util.List;

//public class ListAdapter_Ordenes extends RecyclerView.Adapter<ListAdapter_Ordenes.ViewHolder> implements ConfirmarBorradoOrdenDialog.ListenerDialogo, CestaFragment.ListenerCesta{
class ListAdapter_Ordenes extends RecyclerView.Adapter<ListAdapter_Ordenes.ViewHolder>{

    private ListAdapter_Ordenes.ListenerCesta listener;
    public interface ListenerCesta {
        void actualizarDatos();

        @SuppressLint("MissingSuperCall")
        void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener = (ListenerCesta) context;
        }catch (ClassCastException e){
            throw new ClassCastException(this.toString() + " debe implementar ListenerCesta");
        }
    }*/


    /*private ListAdapter_Ordenes.ListenerAdaptador listener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (ListAdapter_Ordenes.ListenerAdaptador) this;
        }catch (ClassCastException e){
            throw new ClassCastException(this.toString() + " debe implementar ListenerCesta");
        }
    }

    @Override
    public void setPositiveButton(String producto, int position) {
        lista_orden.remove(position);
        DBHelper dbHelper = new DBHelper(context);
        dbHelper.borrarOrden(producto, datosUser[2]);
        dbHelper.close();
    }

    @Override
    public FragmentManager actualizarDatos() {
        return null;
    }

    public interface ListenerAdaptador{
        FragmentManager obtenerDialogo();
    }*/

    private List<Orden> lista_orden;
    private LayoutInflater inflater;    // Describir de que archivo proviene la lista
    private Context context;
    private String[] datosUser;


    public ListAdapter_Ordenes(List<Orden> list_ele, Context context, String[] datosUser){
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.lista_orden = list_ele;
        this.datosUser = datosUser;
    }

    public List<Orden> getListaOrden(){return lista_orden;}
    public void setListaOrden(List<Orden> lista){
        lista_orden=lista;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.orden_cardview,parent,false);
        cargar_configuracion(view);
        return new ViewHolder(view);
    }

    // Método que transfiere la información de la lista de promociones a la vista
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){

        holder.bindData(lista_orden.get(position));
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cantidad = Integer.parseInt(holder.cantidad.getText().toString());
                if(cantidad > 0){

                    String producto = holder.nombre.getText().toString();
                    int nuevaCantidad = cantidad - 1;
                    if (nuevaCantidad <= 0){
                        //ConfirmarBorradoOrdenDialog dialogo = new ConfirmarBorradoOrdenDialog(producto, position, inflater.getContext());
                        //dialogo.show(listener.obtenerDialogo(),"EliminarProd");
                        lista_orden.remove(position);
                        DBHelper dbHelper = new DBHelper(context);
                        dbHelper.borrarOrden(producto, datosUser[2]);
                        dbHelper.close();

                        //listener = (ListenerCesta) view.findViewById(R.id.total).getContext();
                        //listener.actualizarDatos();
                    }
                    else{
                        lista_orden.get(position).setCantidadProd(nuevaCantidad);
                        DBHelper dbHelper = new DBHelper(context);
                        dbHelper.actualizarOrden(producto, -1, datosUser[2]);
                        dbHelper.close();
                        //listener = (ListenerCesta) view.getContext();
                        //listener.actualizarDatos();

                    }
                    notifyDataSetChanged();
                }
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String producto = holder.nombre.getText().toString();

                int cantidad = Integer.parseInt(holder.cantidad.getText().toString()) + 1;
                lista_orden.get(position).setCantidadProd(cantidad);
                notifyDataSetChanged();

                DBHelper dbHelper = new DBHelper(context);
                System.out.println("CANTIDAD: " + String.valueOf(cantidad));
                dbHelper.actualizarOrden(producto, 1, datosUser[2]);
            }
        });
    }

    @Override
    public int getItemCount(){
        return lista_orden.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView nombre;
        Button minus;
        Button plus;
        TextView cantidad;
        TextView precioUnidad;
        TextView precioTotal;

        ViewHolder(View itemView){
            super(itemView);
            img = itemView.findViewById(R.id.orden_img);
            nombre = itemView.findViewById(R.id.orden_nombre);
            cantidad = itemView.findViewById(R.id.orden_cantidad);
            precioUnidad = itemView.findViewById(R.id.orden_precio_uniProd);
            precioTotal = itemView.findViewById(R.id.orden_precio_totProd);
            minus = (Button) itemView.findViewById(R.id.orden_minus);
            plus = (Button) itemView.findViewById(R.id.orden_plus);
        }

        // Método que transfiere la información de la lista de promociones a la vista
        void bindData(final Orden item){
            img.setImageResource(item.getImagenProd());
            nombre.setText(item.getNombreProd());
            cantidad.setText(String.valueOf(item.getCantidadProd()));
            precioUnidad.setText(String.valueOf(item.getPrecioProd()));
            //double tot = Math.round((item.getCantidadProd() * item.getPrecioProd() * 100))/100;
            double tot = Math.round(item.getCantidadProd() * item.getPrecioProd() * 100.0) / 100.0;
            precioTotal.setText(String.valueOf(tot));
        }
    }

    private void cargar_configuracion(View view){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        boolean modoOscuro = sp.getBoolean("modo_oscuro", false);
        if(modoOscuro){
            view.setBackgroundColor(context.getResources().getColor(R.color.black));
        } else{
            view.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
    }
}
