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
import com.example.proyecto_individual_naiarabenito.R;
import java.util.ArrayList;
import java.util.List;


/* ################################ CLASE CONFIGURACION_ACTIVITY ###################################
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
        // Obtener la vista
        View view = inflater.inflate(R.layout.fragment_inicio,container,false);

        // Cargar las preferencias configuradas por el usuario
        cargar_configuracion(view);

        // Obtener los datos del usuario para mantener la sesión
        datosUser = new String[3];
        datosUser[0] = getActivity().getIntent().getExtras().getString("nombreUsuario");
        datosUser[1] = getActivity().getIntent().getExtras().getString("apellidoUsuario");
        datosUser[2] = getActivity().getIntent().getExtras().getString("emailUsuario");

        // Añadir el nombre del usuario en la vista de la aplicación
        TextView nombreUser = view.findViewById(R.id.tv_nombreUsuario_inicio);
        nombreUser.setText("Hola " + datosUser[0]);

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
        lista_prod.add(new Producto("Pizza 4 Quesos","La pizza 4 quesos es una de las más consumidas del mundo, claro está, por los amantes del queso. Pero ojo, porque si seguimos la receta tradicional, no nos vale cualquier queso; de hecho, los cuatro quesos utilizados tienen la certificación de Denominación de Origen Protegida (DOP): El queso fontina, originario del Valle de Aosta; el queso gorgonzola, natural de Milán; el queso parmesano, originario de la ciudad de Parma; y el queso mozzarella, nacido en Campania.\nCon estos cuatro protagonistas tendrás entre tus manos una auténtica perdición quesera en forma de pizza. No tiene más, ni salsa de tomate ni especias, quizás un toque de pimienta negra molida.",R.drawable.prod_pizza_4quesos, 3.29));
        lista_prod.add(new Producto("Pizza Barbacoa","Como su propio nombre indica, el ingrediente estrella de esta pizza es la salsa barbacoa o salsa BBQ. Se trata de una pizza nacida en Estados Unidos, pero que se ha convertido en la favorita de mucha gente por ser jugosa y una de las más intensas y potentes.\nJunto a la salsa barbacoa, la pizza barbacoa se elabora con carne picada y, a elección del comensal, con cebolla, que aporta un contraste de sabor único con el resto de ingredientes.\nAunque Italia será siempre la reina de la pizza, Estados Unidos no se queda atrás en cuanto a consumo; tanto, que los norteamericanos tienen hasta su 'Pizza National Day', una festividad instituida por el Congreso que se celebra el día 9 de febrero.",R.drawable.prod_pizza_barbacoa, 3.89));
        lista_prod.add(new Producto("Pizza Calzone","La calzone es una pizza especial por esconder en su interior todos sus ingredientes (queso, carne, atún, vegetales, etc.), ya que está cubierta por otra masa del mismo tamaño que la base, y en forma de empanadilla.\nEsta pizza envuelta, originaria de la ciudad de Puglia (Nápoles), mantiene siempre su jugosidad y, después de la pasta y la pizza clásica, se ha convertido en uno de los platos más conocidos y versionados de Italia.",R.drawable.prod_pizza_calzone, 5.12));
        lista_prod.add(new Producto("Pizza Diavola","No apta para los paladares más delicados, la pizza Diávola se distingue por el sabor picante de su salsa, que debe notarse en cada bocado. Pero también del resto de ingredientes, que potencian aún más ese toque ardiente: chorizo, salami, incluso chile y una cantidad generosa de queso con carácter.\nLa versión americana de esta pizza sustituye el salami por el pepperoni, variedad del embutido italiano también picante pero con un sabor más ahumado y sazonado con pimentón o ají.",R.drawable.prod_pizza_diavola, 9.00));
        lista_prod.add(new Producto("Pizza Margarita","La pizza margarita es la más clásica de las pizzas italianas y la que abrió paso al resto de pizzas que posteriormente se fueron elaborando.\nLos ingredientes de esta pionera no fallan, y son los preferidos de los auténticos amantes de la pizza tradicional: salsa de tomate, mozzarella, albahaca, orégano y aceite de oliva. \n¿A qué te recuerdan los colores de estos ingredientes? Sí, a la bandera italiana; un sabor de bandera.",R.drawable.prod_pizza_margarita,4.00));

        lista_prod.add(new Producto("Hamburguesa Vacuno con Piña","La integración de la piña con carne es más común en América que en Europa. El resultado final de tal combinación bien merece ser probado, pues aporta una jugosidad al conjunto que difícilmente se consigue con otras recetas. Algunos de sus ingredientes son: Carne ternera, Jamón serrano loncheado, Piña natural, Queso Gouda, Aceite de oliva virgen, Sal y Pimienta negra molida.",R.drawable.prod_ham_pina,4.78));
        lista_prod.add(new Producto("Hamburquesa Shack burger de queso","Una de las hamburguesas más famosas de Nueva York. La hamburguesa clásica de Shake Shack, en presentación individual o doble. Todas las hamburguesas de este tipo son de carne de ternera y para acompañarla, las famosas patatas fritas rizadas.",R.drawable.prod_ham_shack,10.34));
        lista_prod.add(new Producto("Hamburguesa Clásica","Carne picada de ternera, pan humedecido en leche, huevo, tocineta de cerdo y otros condimentos constituyen la base de un excelente sabor. Esta es una hamburguesa súper jugosa que siempre va a triunfar entre quienes la prueban. Dentro de los ingredientes de esta delicia encontramos: Pan de molde, Carne de ternera, Hierbas provenzales, Mostaza de Dijon, Pimienta negra molida y aceite de oliva virgen.",R.drawable.prod_ham_clasica,4.68));
        lista_prod.add(new Producto("Hamburguesa a la Ranchera","Si prefieres carnes blancas en vez de rojas, entonces puedes elegir una hamburguesa de pollo. Algunos piensan que éstas, en comparación con las de ternera, quedan demasiado “dulces” o en algunos casos incluso algo insípidas. Por eso, tienes que probar la hamburguesa de pollo a la ranchera, ya que es de todo menos dulce o sin sabor. La acompaña buena salsa de tomate y cebolla sofritos, además de unos frijoles y varias tiras de bacon.",R.drawable.prod_ham_ranchera,5.31));

        lista_prod.add(new Producto("Bocadillo de Calamares","El bocata de calamares es uno de los platos más representativos de la cocina madrileña. Junto con los callos a la madrileña y el cocido madrileño, es el mejor plato que podemos encontrar en Madrid.",R.drawable.prod_boc_calamar, 7.32));
        lista_prod.add(new Producto("Sandwitch mixto","El sándwich de jamón y queso a la plancha, sándwich mixto, también conocido como bikini en Catalunya, es uno de los  más populares y fáciles de preparar. Sin embargo, por más que parezca una tontería, hay muchos trucos que se esconden tras esta básica receta. Por ejemplo, lograr que el queso quede perfectamente fundido sin quemar el pan o hacer que el pan quede crujiente sin que chorree grasa ni esté aplastado como un papel de fumar." ,R.drawable.prod_sand_mixto,5.60));

        // Cargar las lista en el RecyclerView
        recyclerViewProductos = view.findViewById(R.id.lista_productos);
        recyclerViewProductos.setLayoutManager(new GridLayoutManager(getContext(),3));
        adapterProductos = new ListAdapter_Productos(lista_prod,getContext(),datosUser);
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
}