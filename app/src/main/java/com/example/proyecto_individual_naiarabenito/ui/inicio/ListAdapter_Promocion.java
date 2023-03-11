
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.ui.inicio;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyecto_individual_naiarabenito.R;
import java.util.List;


/* ################################# CLASE LIST_ADAPTER_PROMOCION ##################################
    *) Descripción:
        La función de esta clase es mostrar y gestionar la lista de promociones.

    *) Tipo: RecyclerView Adapter
*/
public class ListAdapter_Promocion extends RecyclerView.Adapter<ListAdapter_Promocion.ViewHolder> {

// ___________________________________________ Variables ___________________________________________
    private List<Promocion> lista_prom;  // Lista con los productos de la cesta
    private LayoutInflater inflater;    // Describir de que archivo proviene la lista
    private Context context;            // Contexto de InicioFragment

// __________________________________________ Constructor __________________________________________
    public ListAdapter_Promocion(List<Promocion> pList_ele, Context pContext){
        this.inflater = LayoutInflater.from(pContext);
        this.context = pContext;
        this.lista_prom = pList_ele;
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
                Este método se ejecuta al crear el RecyclerView de las promociones.
*/
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        // Obtener la vista
        View view = inflater.inflate(R.layout.promocion_cardview,parent,false);
        return new ViewHolder(view);
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
                Este método se ejecuta al crear el RecyclerView con las promociones para
                cada elemento. Transfiere la información de la lista de promociones a la vista.
*/
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        // Cargar los datos de la lista a la vista
        holder.bindData(lista_prom.get(position));
    }

// _________________________________________________________________________________________________

/*  Método getItemCount:
    --------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                (int) lista: Lista con las promociones.
        *) Descripción:
                Getter para obtener la lista de promociones de la aplicación.
*/
    @Override
    public int getItemCount(){
        return lista_prom.size();
    }

/* ######################################## CLASE VIEWHOLDER #######################################
    *) Descripción:
        La función de esta clase es modificar la vista del RecyclerView.

    *) Tipo: RecyclerView ViewHolder
*/
    public class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView img_icon;          // ImageView con la foto de la promoción

        ViewHolder(View itemView){
            super(itemView);
            // Obtener los objetos de la vista
            img_icon = itemView.findViewById(R.id.prom_img);
        }

// ____________________________________________ Métodos ____________________________________________

/*  Método bindData:
    --------------------
        *) Parámetros (Input):
                (Promocion) item: Contiene una Promocion.
        *) Parámetro (Output):
                void
        *) Descripción:
                Cargar la lista de pedidos en la vista.
*/
        void bindData(final Promocion item){
            img_icon.setImageResource(item.getImg_id());
        }
    }
}
