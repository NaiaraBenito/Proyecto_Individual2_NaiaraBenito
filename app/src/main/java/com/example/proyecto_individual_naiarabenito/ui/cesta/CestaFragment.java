package com.example.proyecto_individual_naiarabenito.ui.cesta;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_individual_naiarabenito.Menu_Principal;
import com.example.proyecto_individual_naiarabenito.R;
import com.example.proyecto_individual_naiarabenito.databinding.FragmentCestaBinding;
import com.example.proyecto_individual_naiarabenito.db.DBHelper;
import com.example.proyecto_individual_naiarabenito.ui.inicio.ListAdapter_Productos;
import com.example.proyecto_individual_naiarabenito.ui.inicio.ListAdapter_Promocion;
import com.example.proyecto_individual_naiarabenito.ui.inicio.Producto;
import com.example.proyecto_individual_naiarabenito.ui.inicio.Promocion;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CestaFragment extends Fragment {

    //private FragmentCestaBinding binding;
    String[] datosUser;

    // Variables para mostrar la lista de productos
    List<Orden> lista_ordenes;
    RecyclerView recyclerViewOrdenes;
    ListAdapter_Ordenes adapterOrdenes;

    TextView precioProd;
    TextView envio;
    TextView impuestos;
    TextView total;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cesta,container,false);

        // Recibir datos del usuario
        datosUser = new String[3];
        datosUser[0] = getActivity().getIntent().getExtras().getString("nombreUsuario");
        datosUser[1] = getActivity().getIntent().getExtras().getString("apellidoUsuario");
        datosUser[2] = getActivity().getIntent().getExtras().getString("emailUsuario");

        cargarListaOrdenes(view);
        cargarResumen(view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

    private void cargarListaOrdenes(View view) {

        // Añadir valores a la lista
        DBHelper dbHelper = new DBHelper(getContext());
        lista_ordenes = dbHelper.getOrdenes(datosUser[2]);

        // Mostrar los valores
        recyclerViewOrdenes = view.findViewById(R.id.lista_carrito);
        recyclerViewOrdenes.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        adapterOrdenes = new ListAdapter_Ordenes(lista_ordenes,getContext(),datosUser);
        recyclerViewOrdenes.setAdapter(adapterOrdenes);
    }

    private void cargarResumen(View view) {

        double precioTotProd = calcularPrecioTotProductos(view);
        double envio = calcularEnvio(view, precioTotProd);
        double impuestos = calcularImpuestos(view, precioTotProd);
        calcularTotal(view, precioTotProd, impuestos, envio);
    }

    public double calcularPrecioTotProductos(View view){
        precioProd = (TextView) view.findViewById(R.id.total_prod_carrito);

        double total = 0.00;
        for(int i = 0; i < adapterOrdenes.getItemCount(); i++){
            double auxPrecio = (double) Math.round(adapterOrdenes.getListaOrden().get(i).getCantidadProd() * adapterOrdenes.getListaOrden().get(i).getPrecioProd() * 100.0) / 100.0;
            total = Math.round((total + auxPrecio) * 100.0) / 100.0;
        }
        precioProd.setText(String.valueOf(total) + "€");
        return total;
    }

    public double calcularImpuestos(View view, double precio){
        impuestos = (TextView) view.findViewById(R.id.total_impuesto_carrito);
        double auxImp = 0;
        auxImp = Math.round(((precio * 0.1) * 100.0))/100.0;
        //auxImp = precio * 0.1;
        impuestos.setText(String.valueOf(auxImp) + "€");
        return auxImp;
    }

    public double calcularEnvio(View view, double precio){
        envio = (TextView) view.findViewById(R.id.total_envio_carrito);

        double auxenvio = 0.00;

        if (precio > 0){
            auxenvio = 3.00;
        }
        envio.setText(String.valueOf(auxenvio) + "€");
        return auxenvio;
    }
    public void calcularTotal(View view, double precioProd, double impuestos, double envio){
        total = (TextView) view.findViewById(R.id.total_carrito);
        double auxTotal = 0;
        auxTotal = Math.round((precioProd + impuestos + envio) * 100.0) / 100.0;

        total.setText(String.valueOf(auxTotal) + "€");
    }

}