package com.example.proyecto_individual_naiarabenito.ui.inicio;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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

public class InicioFragment extends Fragment implements SearchView.OnQueryTextListener{

    // Variables para mostrar la lista de Promociones
    ListAdapter_Promocion adapterPromociones;
    RecyclerView recyclerViewPromociones;
    List<Promocion> lista_prom;

    // Variables para mostrar la lista de productos
    List<Producto> lista_prod;
    RecyclerView recyclerViewProductos;
    ListAdapter_Productos adapterProductos;

    SearchView txtBuscar;

    String nombreUser;
    String apellidoUser;
    String emailUser;
    String[] datosUser;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inicio,container,false);
        cargar_configuracion(view);

        // Recibir datos del usuario
        datosUser = new String[3];
        datosUser[0] = getActivity().getIntent().getExtras().getString("nombreUsuario");
        datosUser[1] = getActivity().getIntent().getExtras().getString("apellidoUsuario");
        datosUser[2] = getActivity().getIntent().getExtras().getString("emailUsuario");

        Log.e("USER",datosUser[0] + " " + datosUser[1] + ": " + datosUser[2]);

        TextView nombreUser = (TextView) view.findViewById(R.id.tv_nombreUsuario_inicio);
        nombreUser.setText("Hola " + datosUser[0]);

        // Cargar la lista de Promociones
        cargarListaPromociones(view);

        // Cargar la lista de Productos
        cargarListaProductos(view);

        // Obtener el search View
        txtBuscar = (SearchView) view.findViewById(R.id.search_view);
        txtBuscar.setOnQueryTextListener(this);
        cargar_configuracion(view);
        return view;
    }


    private void cargarListaPromociones(View view) {

        // Añadir valores a la lista
        lista_prom = new ArrayList<>();
        lista_prom.add(new Promocion(R.drawable.an_burrito));
        lista_prom.add(new Promocion(R.drawable.an_hamburguesa));
        lista_prom.add(new Promocion(R.drawable.an_menu));
        lista_prom.add(new Promocion(R.drawable.an_menuyou));
        lista_prom.add(new Promocion(R.drawable.an_pizza));
        lista_prom.add(new Promocion(R.drawable.an_pizza_2x1));

        // Mostrar los valores
        recyclerViewPromociones = view.findViewById(R.id.lista_promociones);
        recyclerViewPromociones.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        adapterPromociones = new ListAdapter_Promocion(lista_prom,getContext());
        recyclerViewPromociones.setAdapter(adapterPromociones);
    }

    private void cargarListaProductos(View view) {

        // Añadir valores a la lista
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

        // Mostrar los valores
        recyclerViewProductos = view.findViewById(R.id.lista_productos);
        recyclerViewProductos.setLayoutManager(new GridLayoutManager(getContext(),3));
        adapterProductos = new ListAdapter_Productos(lista_prod,getContext(),datosUser);
        recyclerViewProductos.setAdapter(adapterProductos);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // Métodos para buscar en tiempo real el texto que pongamos en el SearchView
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapterProductos.filtrado(newText);
        return false;
    }

    private void cargar_configuracion(View view){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        boolean modoOscuro = sp.getBoolean("modo_oscuro", false);
        if(modoOscuro){
            view.setBackgroundColor(getResources().getColor(R.color.black));
        } else{
            view.setBackgroundColor(getResources().getColor(R.color.white));
        }

        String ori = sp.getString("orientacion","1");
        if("1".equals(ori)){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else if("2".equals(ori)){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if("3".equals(ori)){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
}