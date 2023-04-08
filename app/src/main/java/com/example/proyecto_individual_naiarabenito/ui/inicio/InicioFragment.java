
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.ui.inicio;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyecto_individual_naiarabenito.activities.GestorIdioma;
import com.example.proyecto_individual_naiarabenito.R;
import java.util.ArrayList;
import java.util.List;


/* ################################ CLASE INICIO_FRAGMENT ###################################
    *) Descripción:
        La función de esta clase es mostrar y gestionar las lista del productos de la aplicación.

    *) Tipo: Fragment
*/
public class InicioFragment extends Fragment implements SearchView.OnQueryTextListener{

// ___________________________________________ Variables ___________________________________________
    private ListAdapter_Promocion adapterPromociones;   // Adaptador para gestionar lista de promociones
    private RecyclerView recyclerViewPromociones;   // RecyclerView que muestra la lista de promociones
    private List<Promocion> lista_prom; // Lista que contiene las promociones de la aplicación

    private List<Producto> lista_prod;  // Lista que contiene los productos de la aplicación
    private RecyclerView recyclerViewProductos; // RecyclerView que muestra la lista de productos
    private ListAdapter_Productos adapterProductos; // Adaptador para gestionar lista de productos

    private SearchView txtBuscar;   // SearchView para filtrar la lista de productos

    private String[] datosUser; // Lista que contiene los datos del usuario para mantener la sesión

    private String idioma;          // String que contiene el idioma actual de la aplicación

// ____________________________________________ Métodos ____________________________________________

/*  Método onCreateView:
    --------------------
        *) Parámetros (Input):
                1) (LayoutInflater) inflater: Se utiliza para enlazar el fragmento con su vista.
                2) (ViewGroup) container: Contiene la vista principal a la que se debe adjuntar la
                   vista del fragmento.
                3) (Bundle) savedInstanceState: Contiene el diseño predeterminado del Fragmento.
        *) Parámetro (Output):
                (View) view: Vista asociada al Fragment actual.
        *) Descripción:
                Este método se ejecuta cuando el usuario se ha logueado correctamente y accede al
                apartado inicio del menú.
*/
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Obtener el idioma de la aplicación del Bundle (mantener idioma al girar la pantalla)
        if (savedInstanceState != null) {
            idioma = savedInstanceState.getString("idioma");
        }

        // Obtener el idioma de la aplicación del intent (mantener idioma al moverse por la aplicación)
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null){
            idioma = (String) extras.get("idioma");
        }

        // Si en la anterior ejecución se ha guardado el idioma
        if (idioma != null){
            // Instanciar el Gestor de idiomas
            GestorIdioma gI = new GestorIdioma();

            // Asignar el idioma a la pantalla actual
            gI.cambiarIdioma(getActivity().getBaseContext(), idioma);
        }

        // Obtener la vista
        View view = inflater.inflate(R.layout.fragment_inicio,container,false);

        // Cargar las preferencias configuradas por el usuario
        cargar_configuracion(view);

        // Obtener los datos del usuario para mantener la sesión
        datosUser = new String[4];
        datosUser[0] = getActivity().getIntent().getExtras().getString("nombreUsuario");
        datosUser[1] = getActivity().getIntent().getExtras().getString("apellidoUsuario");
        datosUser[2] = getActivity().getIntent().getExtras().getString("emailUsuario");
        datosUser[3] = getActivity().getIntent().getExtras().getString("fotoUsuario");

        // Añadir el nombre del usuario en la vista de la aplicación
        TextView nombreUser = view.findViewById(R.id.tv_nombreUsuario_inicio);
        nombreUser.setText(datosUser[0]);

        // Cargar la lista de Promociones
        cargarListaPromociones(view);

        // Cargar la lista de Productos
        cargarListaProductos(view);

        // Obtener el searchView y añadir un Listener
        txtBuscar = view.findViewById(R.id.search_view);
        txtBuscar.setOnQueryTextListener(this);
        return view;
    }

// _________________________________________________________________________________________________

/*  Método cargarListaPromociones:
    ------------------------------
        *) Parámetros (Input):
                1) (View) view: Vista asociada al Fragment actual.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método carga el RecyclerView con las promociones de la aplicación.
*/
    private void cargarListaPromociones(View view) {

        // Añadir las promociones a la lista
        lista_prom = new ArrayList<>();
        lista_prom.add(new Promocion(R.drawable.an_burrito));
        lista_prom.add(new Promocion(R.drawable.an_hamburguesa));
        lista_prom.add(new Promocion(R.drawable.an_menu));
        lista_prom.add(new Promocion(R.drawable.an_menuyou));
        lista_prom.add(new Promocion(R.drawable.an_pizza));
        lista_prom.add(new Promocion(R.drawable.an_pizza_2x1));

        // Cargar las lista en el RecyclerView
        recyclerViewPromociones = view.findViewById(R.id.lista_promociones);
        recyclerViewPromociones.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        adapterPromociones = new ListAdapter_Promocion(lista_prom,getContext());
        recyclerViewPromociones.setAdapter(adapterPromociones);
    }

// _________________________________________________________________________________________________

/*  Método cargarListaProductos:
    ----------------------------
        *) Parámetros (Input):
                1) (View) view: Vista asociada al Fragment actual.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método carga el RecyclerView con los productos que ofrece la aplicación.
*/
    private void cargarListaProductos(View view) {

        // Añadir los productos a la lista
        lista_prod = new ArrayList<>();
        lista_prod.add(new Producto("Pizza 4 Quesos",getResources().getString(R.string.desc_4Quesos),R.drawable.prod_pizza_4quesos, 3.29));
        lista_prod.add(new Producto("Pizza Barbacoa",getResources().getString(R.string.desc_Barbacoa),R.drawable.prod_pizza_barbacoa, 3.89));
        lista_prod.add(new Producto("Pizza Calzone",getResources().getString(R.string.desc_Calzone),R.drawable.prod_pizza_calzone, 5.12));
        lista_prod.add(new Producto("Pizza Diavola",getResources().getString(R.string.desc_Diabola),R.drawable.prod_pizza_diavola, 9.00));
        lista_prod.add(new Producto("Pizza Margarita",getResources().getString(R.string.desc_Margarita),R.drawable.prod_pizza_margarita,4.00));

        lista_prod.add(new Producto("Hamburguesa Vacuno con Piña",getResources().getString(R.string.desc_Vacuno),R.drawable.prod_ham_pina,4.78));
        lista_prod.add(new Producto("Hamburquesa Shack burger de queso",getResources().getString(R.string.desc_Shack),R.drawable.prod_ham_shack,10.34));
        lista_prod.add(new Producto("Hamburguesa Clásica",getResources().getString(R.string.desc_Clasica),R.drawable.prod_ham_clasica,4.68));
        lista_prod.add(new Producto("Hamburguesa a la Ranchera",getResources().getString(R.string.desc_Ranchera),R.drawable.prod_ham_ranchera,5.31));

        lista_prod.add(new Producto("Bocadillo de Calamares",getResources().getString(R.string.desc_Calamares),R.drawable.prod_boc_calamar, 7.32));
        lista_prod.add(new Producto("Sandwitch mixto",getResources().getString(R.string.desc_Mixto) ,R.drawable.prod_sand_mixto,5.60));

        // Cargar las lista en el RecyclerView
        recyclerViewProductos = view.findViewById(R.id.lista_productos);
        recyclerViewProductos.setLayoutManager(new GridLayoutManager(getContext(),3));
        adapterProductos = new ListAdapter_Productos(lista_prod,getContext(),datosUser, idioma);
        recyclerViewProductos.setAdapter(adapterProductos);
    }

// _________________________________________________________________________________________________

/*  Método onDestroyView:
    ---------------------
        *) Parámetos (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta al eliminar el Fragmento.
*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

// _________________________________________________________________________________________________

/*  Método onViewCreated:
    ---------------------
    *) Parámetos (Input):
            1) (View) view: Vista asociada al Fragment actual.
            2) (Bundle) savedInstanceState: Contiene el diseño predeterminado del Fragmento.
    *) Parámetro (Output):
            void
    *) Descripción:
            Este método se ejecuta al terminar de crearse el Fragmento.
*/
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

// _________________________________________________________________________________________________

/*  Métodos onQueryTextSubmit y onQueryTextChange:
    ----------------------------------------------
    *) Parámetos (Input):
            1) (String) query / newText: Texto que se desea filtrar.
    *) Parámetro (Output):
            (boolean) false
    *) Descripción:
            Métodos para buscar en tiempo real el texto que pongamos en el SearchView
*/
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        // Filtrar el texto en la lista
        adapterProductos.filtrado(newText);
        return false;
    }

// _________________________________________________________________________________________________

    /*  Método cargar_configuracion:
        ----------------------------
            *) Parámetros (Input):
                    (View) view: Vista asociada al fragmento.
            *) Parámetro (Output):
                    void
            *) Descripción:
                    Este método carga las preferencias configuradas por el usuario (modo oscuro,
                    orientación de la pantalla...).
    */
    private void cargar_configuracion(View view){

        // Obtener las preferencias configuradas por el usuario
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        // Comprobar el estado de la preferencia del modo oscuro
        boolean modoOscuro = sp.getBoolean("modo_oscuro", false);

        if(modoOscuro){         // Si el modo oscuro está activado: Pintar el fondo de negro
            view.setBackgroundColor(getResources().getColor(R.color.black));
        } else{                 // Si el modo oscuro está desactivado: Pintar el fondo de blanco
            view.setBackgroundColor(getResources().getColor(R.color.white));
        }

        // Comprobar el estado de la preferencia de la orientación
        String ori = sp.getString("orientacion","1");
        switch (ori) {
            case "1": // Si la orientación es 1: Desbloquear el giro automático de la app
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                break;
            case "2": // Si la orientación es 2: Bloquear la orientacion vertical
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case "3": // Si la orientación es 3: Bloquear la orientacion horizontal
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }

// _________________________________________________________________________________________________

/*  Método onSaveInstanceState:
    ------------------------
        *) Parámetros (Input):
                1) (Bundle) outState: Contiene el diseño predeterminado del Fragment.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta antes de eliminar la actividad. Guarda el idioma actual en el
                Bundle, para que al refrescar la actividad se mantenga.
*/
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Guardar en el Bundle el idioma actual de la aplicación
        outState.putString("idioma",idioma);
    }
}