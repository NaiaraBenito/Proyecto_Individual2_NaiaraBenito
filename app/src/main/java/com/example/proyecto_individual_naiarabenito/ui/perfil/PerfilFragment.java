
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.ui.perfil;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/* ###################################### CLASE PERFIL_FRAGMENT ####################################
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
    private ImageView iv_foto;
    private FloatingActionButton btn_foto;    // Botón para cerrar la sesión
    private String idioma;          // String que contiene el idioma actual de la aplicación
    private RequestQueue requestQueue;
    private Bitmap laminiatura;
    private ActivityResultLauncher<Intent> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData()!= null) {
                    Bundle bundle = result.getData().getExtras();
                    laminiatura = (Bitmap) bundle.get("data");
                    iv_foto.setImageBitmap(laminiatura);
                    //GUARDAR COMO FICHERO
                    // Memoria externa
                    File eldirectorio = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    String nombrefichero = "IMG_" + timeStamp + "_";
                    File imagenFich = new File(eldirectorio, nombrefichero + ".jpg");
                    OutputStream os;
                    try {
                        os = new FileOutputStream(imagenFich);
                        laminiatura.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        /*ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        imagen = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                        iv_foto.setImageBitmap(imagen);*/
                        os.flush();
                        os.close();
                        actualizarUsuario("http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/nbenito012/WEB/actualizar_usuario.php");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else{
                    Log.d("FOTO","ENTRA AQUI");
                }
            });

    /*private ActivityResultLauncher<Intent> takePictureLauncher =
            registerForActivityResult(new
                    ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData()!= null) {
                    Bundle bundle = result.getData().getExtras();
                    ImageView elImageView = getActivity().findViewById(R.id.pf_foto);
                    Bitmap laminiatura = (Bitmap) bundle.get("data");
                    elImageView.setImageBitmap(laminiatura);

                    try{
                        FileOutputStream fos = getActivity().openFileOutput();
                    } catch (Exception e){}
                } else {
                    Log.d("TakenPicture", "No photo taken");
                }
            });*/

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
        requestQueue = Volley.newRequestQueue(getContext());

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

        Glide.with(getContext())
                .load(datosUser[3])
                        .into(iv_foto);

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

    String currentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;

    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void sacarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;

            takePictureLauncher.launch(takePictureIntent);
            //photoFile = createImageFile();

            // Continue only if the File was successfully created
            /*if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),"com.example.proyecto_individual_naiarabenito.provider",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                //startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }*/
        }
    }

    /*static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data.getExtras() != null) {
            Bundle extras = data.getExtras();
            Log.d("FOTO","DATA"+data.getExtras().toString());
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            iv_foto.setImageBitmap(imageBitmap);
        }
    }*/

    private void actualizarUsuario(String pUrl){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, pUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("FOTO","onResponse");
                Log.d("FOTO",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Imprimir estado del registro
                String msg = getResources().getString(R.string.t_errorBBDD);
                Toast.makeText(getContext(),msg, Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Log.d("FOTO","getParams");
                //Log.d("FOTO",getStringImagen(laminiatura));
                //Log.d("FOTO","out getParams");

                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("email", tv_email.getText().toString());
                parametros.put("foto", getStringImagen(laminiatura));
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }
    public String getStringImagen(Bitmap bmp){
        Log.d("FOTO","getStringImagen");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void checkPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 100);
            } else{
                sacarFoto();
            }
        } else{
            sacarFoto();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100){
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                sacarFoto();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
