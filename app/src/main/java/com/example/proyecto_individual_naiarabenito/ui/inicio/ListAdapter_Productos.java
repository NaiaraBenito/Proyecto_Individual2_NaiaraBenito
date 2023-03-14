
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.ui.inicio;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyecto_individual_naiarabenito.R;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/* ################################# CLASE LIST_ADAPTER_PRODUCTOS ##################################
    *) Descripción:
        La función de esta clase es mostrar y gestionar la lista de productos.

    *) Tipo: RecyclerView Adapter
*/
public class ListAdapter_Productos extends RecyclerView.Adapter<ListAdapter_Productos.MyViewHolder> {

// ___________________________________________ Variables ___________________________________________
    private List<Producto> lista_prod;      // Lista con los productos de la aplicación (filtrada)
    private final LayoutInflater inflater;        // Describir de que archivo proviene la lista
    private final Context context;                // Contexto de InicioFragment

    private List<Producto> lista_prod_original;     // Lista con los productos de la aplicación

    private final String[] datosUser; // Lista que contiene los datos del usuario para mantener la sesión

    private String idioma;          // String que contiene el idioma actual de la aplicación

// __________________________________________ Constructor __________________________________________
    public ListAdapter_Productos(List<Producto> pLista_prod, Context pContext, String[] pDatosUser, String pIdioma) {
        this.inflater = LayoutInflater.from(pContext);
        this.context = pContext;
        this.lista_prod = pLista_prod;
        this.lista_prod_original = new ArrayList<>();
        this.lista_prod_original.addAll(pLista_prod);
        this.datosUser = pDatosUser;
        this.idioma = pIdioma;
    }

// ____________________________________________ Métodos ____________________________________________

/*  Método onCreateViewHolder:
    --------------------------
        *) Parámetros (Input):
                1) (ViewGroup) parent: Contiene la vista principal a la que se debe adjuntar la
                   vista del RecyclerView.
                2) (int) viewType: Contiene el índice del elemento de la lista.
        *) Parámetro (Output):
                (ViewHolder) viewholder: Vista con el producto de la posición viewType.
        *) Descripción:
                Este método se ejecuta al crear el RecyclerView con los productos de la aplicación.
*/
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Obtener la vista
        View view = inflater.inflate(R.layout.producto_cardview, parent,false);
        return new MyViewHolder(view);
    }

// _________________________________________________________________________________________________

/*  Método onBindViewHolder:
    ------------------------
        *) Parámetros (Input):
                1) (ViewHolder) parent: Contiene la vista de un elemento de la lista.
                2) (int) position: Contiene el índice del elemento de la lista.
        *) Parámetro (Output):
                (ViewHolder) viewholder: Vista con el producto de la posición viewType.
        *) Descripción:
                Este método se ejecuta al crear el RecyclerView con los productos que ofrece la
                aplicación para cada elemento. Transfiere la información de la lista de órdenes a la vista.
*/
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        // Cargar los datos de la lista en la vista
        holder.nombre.setText(lista_prod.get(position).getNombre());
        holder.imagen.setImageResource(lista_prod.get(position).getImg_id());

        // Añadir un Listener para detectar la pulsación al CardView
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    // Ha pulsado el elemento

                // Crear el intent que redirige la ejecución al Actividad con los detalles del producto
                Intent intent = new Intent(context, Detalles_Producto.class);

                // Pasarle el producto a la nueva Actividad
                intent.putExtra("nombreProducto",lista_prod.get(position).getNombre());
                intent.putExtra("decripProducto",lista_prod.get(position).getDescripcion());
                intent.putExtra("imgProducto",lista_prod.get(position).getImg_id());
                intent.putExtra("precioProducto",lista_prod.get(position).getPrecio());

                // Pasarle los datos del usuario a la nueva Actividad
                intent.putExtra("nombreUsuario", datosUser[0]);
                intent.putExtra("apellidoUsuario", datosUser[1]);
                intent.putExtra("emailUsuario", datosUser[2]);

                // Guardar el idioma actual de la aplicación
                intent.putExtra("idioma", idioma);

                context.startActivity(intent);
            }
        });
    }

// _________________________________________________________________________________________________

/*  Método filtrado:
    ----------------
        *) Parámetros (Input):
                1) (String) txtBuscar: Contiene el texto a filtrar.
        *) Parámetro (Output):
                (ViewHolder) viewholder: Vista con el producto de la posición viewType.
        *) Descripción:
                Este método se ejecuta al escribir en el SearchView del InicioFragment. Filtra
                 la lista de productos que ofrece la aplicación.
*/
    public void filtrado(String txtBuscar){

        // Obtener el tamaño del texto a filtrar
        int l = txtBuscar.length();

        // Comprobar si se ha escrito algo
        if(l == 0){     // Si está vacío: Recuperar lista original
            this.lista_prod.clear();
            lista_prod.addAll(lista_prod_original);
        } else{         // Si no está vacío: Filtrar la lista
            // Comprobar si la version del dispositivo soporta la operación de filtrado Nougat
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                // Filtrar la lista
                List<Producto> collection = lista_prod.stream().filter(i -> i.getNombre().toLowerCase().contains(txtBuscar.toLowerCase())).collect(Collectors.toList());
                lista_prod.clear();
                lista_prod.addAll(collection);
            }
            else {      // Si no la soporta, realizar el filtrado de otra manera
                for(Producto prod: lista_prod_original){
                    if(prod.getNombre().toLowerCase().contains(txtBuscar.toLowerCase())){
                        lista_prod.add(prod);
                    }
                }
            }
        }
        // Indicar que se han realizado cambios en la lista
        notifyDataSetChanged();
    }

// _________________________________________________________________________________________________

/*  Método getItemCount:
    --------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                (int) lista: Lista con los productos.
        *) Descripción:
                Getter para obtener la lista de productos.
*/
    @Override
    public int getItemCount() {
        return lista_prod.size();
    }

/* ######################################## CLASE MYVIEWHOLDER #######################################
    *) Descripción:
        La función de esta clase es modificar la vista del RecyclerView.

    *) Tipo: RecyclerView ViewHolder
*/
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView nombre;      // TextView con el nombre del producto
        private final ImageView imagen;     // ImageView con la foto del producto
        private final CardView cardView;    // CardView con el producto

        public MyViewHolder(View itemView){
            super(itemView);

            // Obtener los objetos de la vista
            nombre = itemView.findViewById(R.id.nombre_producto_rv);
            imagen = itemView.findViewById(R.id.img_producto_rv);
            cardView = itemView.findViewById(R.id.producto_cardview);
        }
    }
}
