
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.ui.perfil;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import static android.app.Activity.RESULT_OK;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.proyecto_individual_naiarabenito.activities.GestorIdioma;
import com.example.proyecto_individual_naiarabenito.activities.Login;
import android.Manifest;
import android.widget.Toast;
import com.example.proyecto_individual_naiarabenito.activities.Menu_Principal;
import com.example.proyecto_individual_naiarabenito.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/* ###################################### CLASE PERFIL FRAGMENT ####################################
    *) Descripción:
        La función de esta clase es mostrar y gestionar la configuración del perfil de usuario.

    *) Tipo: Fragment
*/
public class PerfilFragment extends Fragment {

// ___________________________________________ Variables ___________________________________________
    private String[] datosUser; // Lista que contiene los datos del usuario para mantener la sesión
    private Button btn_cerrarSesion;    // Botón para cerrar la sesión
    private TextView tv_nombre;         // TextView que contiene el nombre del usuario
    private TextView tv_apellido;         // TextView que contiene el apellido del usuario
    private TextView tv_email;         // TextView que contiene el email del usuario
    private ImageView iv_foto;          // ImageView que contiene la foto de perfil del usuario
    private FloatingActionButton btn_foto;    // Botón para cerrar la sesión
    private String idioma;          // String que contiene el idioma actual de la aplicación
    private RequestQueue requestQueue; // Variable que gestiona el envío de peticiones a la BBDD remota
    private Bitmap laminiatura; // Bitmap que contiene la imagen de perfil


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
                apartado Perfil del menú.
*/
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

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
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Cargar las preferencias configuradas por el usuario
        cargar_configuracion(view);

        // Obtener los datos del usuario para mantener la sesión
        datosUser = new String[4];
        datosUser[0] = getActivity().getIntent().getExtras().getString("nombreUsuario");
        datosUser[1] = getActivity().getIntent().getExtras().getString("apellidoUsuario");
        datosUser[2] = getActivity().getIntent().getExtras().getString("emailUsuario");
        datosUser[3] = getActivity().getIntent().getExtras().getString("fotoUsuario");

        // Obtener los Objetos de la vista
        iv_foto = view.findViewById(R.id.pf_foto);
        tv_nombre = view.findViewById(R.id.pf_nombre);
        tv_apellido = view.findViewById(R.id.pf_apellido);
        tv_email = view.findViewById(R.id.pf_email);
        btn_cerrarSesion = view.findViewById(R.id.btn_logout);
        btn_foto = view.findViewById(R.id.pf_btnFoto);

        // Asignar valor a los elementos
        tv_nombre.setText(datosUser[0]);
        tv_apellido.setText(datosUser[1]);
        tv_email.setText(datosUser[2]);

        // Cargar la imagen de perfil
        cargarImagen();

        // Asignar acción al pulsar el botón "CERRAR SESIÓN"
        btn_cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cerrar la sesión y volver al Login
                cerrarSesion();
            }
        });

        // Obtener el botón para cambiar el idioma de la aplicación al inglés
        Button bot_eng = view.findViewById(R.id.btn_en);
        // Cambiar idioma al Inglés --> Funcionamiento del botón English
        bot_eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar variable auxiliar que indica el idioma de la aplicación
                idioma = "en";

                // Instanciar el Gestor de idiomas
                GestorIdioma gI = new GestorIdioma();

                // Asignar el idioma a la pantalla actual
                gI.cambiarIdioma(getActivity().getBaseContext(), idioma);

                // Recargar de nuevo la actividad
                Intent intent = new Intent(getContext(), Menu_Principal.class);

                // Guardar los datos del usuario (mantener la sesión)
                intent.putExtra("nombreUsuario", datosUser[0]);
                intent.putExtra("apellidoUsuario", datosUser[1]);
                intent.putExtra("emailUsuario", datosUser[2]);
                intent.putExtra("fotoUsuario", datosUser[3]);

                // Enviar el idioma actual
                intent.putExtra("idioma",idioma);

                getActivity().finish();
                startActivity(intent);
            }
        });

        // Obtener el botón para cambiar el idioma de la aplicación al castellano
        Button bot_esp = view.findViewById(R.id.btn_es);
        bot_esp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar variable auxiliar que indica el idioma de la aplicación
                idioma = "es";

                // Instanciar el Gestor de idiomas
                GestorIdioma gI = new GestorIdioma();

                // Asignar el idioma a la pantalla actual
                gI.cambiarIdioma(getActivity().getBaseContext(), idioma);

                // Recargar de nuevo la actividad
                Intent intent = new Intent(getContext(), Menu_Principal.class);

                // Guardar los datos del usuario (mantener la sesión)
                intent.putExtra("nombreUsuario", datosUser[0]);
                intent.putExtra("apellidoUsuario", datosUser[1]);
                intent.putExtra("emailUsuario", datosUser[2]);
                intent.putExtra("fotoUsuario", datosUser[3]);

                // Enviar el idioma actual
                intent.putExtra("idioma",idioma);

                getActivity().finish();
                startActivity(intent);
            }
        });

        // Asignar acción al pulsar el botón para cambiar la imagen de perfil
        btn_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pedir permisos para usar la cámara
                checkPermission();
            }
        });

        return view;
    }

// _________________________________________________________________________________________________

/*  Método cerrarSesion:
    --------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta cuando el usuario pulsa el botón "Cerrar Sesión". Redirige al
                usuario al Login sin guardar sus datos.
*/
    private void cerrarSesion(){
        // Crear el intent que redirige la ejecución al Login
        Intent intent = new Intent(getContext(), Login.class);

        // Guardar el idioma actual de la aplicación
        intent.putExtra("idioma", idioma);

        startActivity(intent);
        getActivity().finish();
    }

// _________________________________________________________________________________________________

    /*  Método onDestroyView:
        ---------------------
            *) Parámetos (Input):
            *) Parámetro (Output):
                    void
            *) Descripción:
                    Este método se ejecuta cuando se cierra el fragmento y borra la vista
    */
    @Override
    public void onDestroyView() {
        // Borrar la vista
        super.onDestroyView();
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
        String ori = sp.getString("orientacion","false");
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

// _________________________________________________________________________________________________

/*  Método actualizarFotoPerfil:
    ----------------------------
        *) Parámetros (Input):
                1) (String) pUrl: Contiene la dirección URL del PHP que actualiza la foto de perfil
                   del usuario en la BBDD.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta tras sacar una foto con la cámara. Se encarga de comprobar
                actualizar la foto del servidor.
*/

    private void actualizarFotoPerfil(String pUrl){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, pUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {}   // Si la petición ha sido exitosa
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { // Si ha ocurriddo un error
                // Imprimir mensaje de error
                String msg = getResources().getString(R.string.t_errorBBDD);
                Toast.makeText(getContext(),msg, Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError { // Parámetros que enviar con la petición
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("email", tv_email.getText().toString());
                parametros.put("foto", getStringImagen(laminiatura));
                return parametros;
            }
        };
        // Añadir petición a la cola
        requestQueue.add(stringRequest);
    }

// _________________________________________________________________________________________________

/*  Método getStringImagen:
    -----------------------
        *) Parámetros (Input):
                1) (Bitmap) bmp: Contiene el bitmap con la imagen obtenida con la camara.
        *) Parámetro (Output):
                (String) encodedImage: Contiene la imagen codificada en Base64.
        *) Descripción:
                Este método se ejecuta tras sacar una foto con la cámara. Se encarga de prerarar
                la imagen en un String codificado en Base64 para poder subirla al servidor.
*/
    public String getStringImagen(Bitmap bmp){
        // Comprimir la imagen
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        // Codificar la imagen en Base64
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

// _________________________________________________________________________________________________

/*  Método checkPermission:
    -----------------------
    *) Parámetros (Input):
    *) Parámetro (Output):
            void
    *) Descripción:
            Este método se encarga de comprobar que la app tenga permisos para utilizar la cámara.
*/
    public void checkPermission(){
        // Comprobar la versión Android del dispositivo
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Comprobar si tiene permisos
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Pedir permiso
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 100);
            } else{
                // Abrir la cámara
                abrirCamara();
            }
        } else{
            // Abrir la cámara
            abrirCamara();
        }
    }

// _________________________________________________________________________________________________

/*  Método onRequestPermissionResult:
    ---------------------------------
        *) Parámetos (Input):
                (int) requestCode: Contiene el código de la petición (identificador).
                (String[]) permissions: Contiene la lista de los permisos que se piden.
                (int[]) grantResults: Contiene el resultado de las peticiones.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta cuando se ha realizado la petición de permisos de la
                aplicación.
*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100){
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                abrirCamara();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

// _________________________________________________________________________________________________

/*  Método abrirCamara:
    -------------------
        *) Parámetos (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta cuando se ha pulsado el botón con el icono de la cámara.
                Abre la cámara para poder realizar una foto
*/
    public void abrirCamara(){
        // Crear un intent que redirija la ejecución a la cámara del dispositivo
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(i.resolveActivity(getContext().getPackageManager()) != null){
            startActivityForResult(i,1);
        }
    }

// _________________________________________________________________________________________________

/*  Método onActivityResult:
    ------------------------
        *) Parámetos (Input):
                (int) requestCode: Contiene el código de la petición (identificador).
                (int) resultCode: Contiene el código del resultado de la petición.
                (Intent) data: Contiene el intent donde se guarda la foto recién sacada.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta tras realizar la foto con la cámara del dispositivo. Guarda
                la imagen en el dispositivo y actualiza la imagen del servidor.
*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Comprobar que a foto se ha realizado correctamente
        if (requestCode == 1 && resultCode == RESULT_OK && data.getExtras() != null) {
            // Obtener la imagen
            Bundle extras = data.getExtras();
            laminiatura = (Bitmap) extras.get("data");

            // Poner la imagen en pantalla
            iv_foto.setImageBitmap(laminiatura);

            //Guardar la imagen en la memoria externa del dispositivo
            File eldirectorio = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String nombrefichero = "IMG_" + timeStamp + "_";
            File imagenFich = new File(eldirectorio, nombrefichero + ".jpg");
            OutputStream os;
            try {
                os = new FileOutputStream(imagenFich);
                laminiatura.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();

                // Actializar la imagen del servidor
                actualizarFotoPerfil("http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/nbenito012/WEB/actualizar_fotoPerfil.php");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

// _________________________________________________________________________________________________

/*  Método cargarImagen:
    --------------------
        *) Parámetos (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta cada vez que se crea la vista del fragmento. Se encarga de
                obtener la foto de perfil del servidor y la pone en pantalla.
*/
    private void cargarImagen(){
        ImageRequest imageRequest = new ImageRequest(datosUser[3],
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {   // Si la petición se ha realizado correctamente
                        // Mostrar la imagen
                        iv_foto.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {   // Si ha ocurrido un error
            }
        });
        // Añadir petición a la cola
        requestQueue.add(imageRequest);
    }
}
