
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.ui.cesta;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import static com.example.proyecto_individual_naiarabenito.ui.cesta.CestaFragment.NOTIFICACION_ID;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextPaint;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.example.proyecto_individual_naiarabenito.GestorIdioma;
import com.example.proyecto_individual_naiarabenito.Menu_Principal;
import com.example.proyecto_individual_naiarabenito.R;
import java.io.File;
import java.io.FileOutputStream;


/* ################################### CLASE SI_ACT_NOTIFICACION ##################################
    *) Descripción:
        La función de esta clase es gestionar el proceso de haber pulsado Si en la notificación
        que aparece al terminar un pedido y pregunta por la factura.

    *) Tipo: Activity
*/
public class SiActNotificacion extends AppCompatActivity {

    // ___________________________________________ Variables ___________________________________________
    private String[] datosUser;  // Lista que contiene los datos del usuario para mantener la sesión
    private String tituloPDF;    // Variable que contiene el título de la factura que se genera al finalizar el pedido
    private String descripcionPDF;  // Variable que contiene el cuerpo de la factura

    private String idioma;          // String que contiene el idioma actual de la aplicación

// ____________________________________________ Métodos ____________________________________________

/*  Método onCreate:
    ----------------
        *) Parámetros (Input):
                1) (Bundle) savedInstanceState: Contiene el diseño predeterminado del Activity.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta la primera vez que se crea el Activity.
                Elimina la notificación, crea la factura y devuelve la ejecución al Menú Principal.
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Obtener el idioma de la aplicación del Bundle (mantener idioma al girar la pantalla)
        if (savedInstanceState != null) {
            idioma = savedInstanceState.getString("idioma");
        }

        // Obtener el idioma de la aplicación del intent (mantener idioma al moverse por la aplicación)
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            idioma = (String) extras.get("idioma");
        }

        // Si en la anterior ejecución se ha guardado el idioma
        if (idioma != null){
            // Instanciar el Gestor de idiomas
            GestorIdioma gI = new GestorIdioma();

            // Asignar el idioma a la pantalla actual
            gI.cambiarIdioma(getBaseContext(), idioma);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_si_act_notificacion);

        if(extras != null){
            // Obtener los datos del usuario para mantener la sesión
            datosUser = new String[4];
            datosUser[0] = extras.getString("nombreUsuario");
            datosUser[1] = extras.getString("apellidoUsuario");
            datosUser[2] = extras.getString("emailUsuario");
            datosUser[3] = extras.getString("fotoUsuario");

            tituloPDF = extras.getString("tituloFactura");
            descripcionPDF = extras.getString("descripcionFactura");

            // Crear la factura de la compra
            crearPDF();

            // Crear el intent que redirige la ejecución al Menú Principal
            Intent intent = new Intent(this, Menu_Principal.class);

            // Pasarle la información del usuario (mantener la sesión)
            intent.putExtra("nombreUsuario", datosUser[0]);
            intent.putExtra("apellidoUsuario", datosUser[1]);
            intent.putExtra("emailUsuario", datosUser[2]);
            intent.putExtra("fotoUsuario", datosUser[3]);

            // Enviar el idioma actual
            intent.putExtra("idioma",idioma);

            // Crear un manager de notificaciones
            NotificationManagerCompat nmc = NotificationManagerCompat.from(getApplicationContext());

            // Eliminar la notificación, pasándole el NOTIFICATION_ID
            nmc.cancel(NOTIFICACION_ID);

            // Evitar que se llene la pila de Actividades (Menú Principal solo tiene una instancia)
            onNewIntent(intent);
            finish();
        }
    }

    // _________________________________________________________________________________________________

    /*  Método crearPDF:
        ----------------
            *) Parámetros (Input):
            *) Parámetro (Output):
                    void
            *) Descripción:
                    Este método crea la factura de la compra realizada por el usuario en formato PDF.
    */
    private void crearPDF(){

        // Crear PDF
        PdfDocument pdfDocument = new PdfDocument();

        // Crear herramienta para editar el documento
        Paint paint = new Paint();

        // Escribir en el documento
        TextPaint titulo = new TextPaint();
        TextPaint descripcion = new TextPaint();

        // Obtener Bitmaps para dibujar el logo en la factura
        Bitmap bitmap, bitmapEscala;

        // Añadir especificaciones del documento (dimensiones de la página)
        PdfDocument.PageInfo paginaInfo = new PdfDocument.PageInfo.Builder(816,1054,1).create();
        PdfDocument.Page pagina = pdfDocument.startPage(paginaInfo);

        // Obtener página
        Canvas canvas = pagina.getCanvas();

        // Escalar el logo
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        bitmapEscala = Bitmap.createScaledBitmap(bitmap, 250, 250, false);

        // Pintar el logo en el documento
        canvas.drawBitmap(bitmapEscala, 500,15,paint);

        // Escribir el título en el documento
        titulo.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        titulo.setTextSize(25);
        canvas.drawText(tituloPDF, 10, 180, titulo);

        // Escribir el contenido en el documento
        descripcion.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        descripcion.setTextSize(14);
        String[] arrDescripcion = descripcionPDF.split("\n");

        int y = 237;    // Coordenada Y en la que comenzar a escribir el texto
        for(int i = 0; i<arrDescripcion.length; i++){
            canvas.drawText(arrDescripcion[i],10,y,descripcion);
            y +=  15;
        }

        // Finalizar la página
        pdfDocument.finishPage(pagina);

        // Exportar el documento PDF de la factura (sdcard/Factura_[nombre]_[apellido].pdf)
        String fich = getResources().getString(R.string.cf_factura);
        File file = new File(Environment.getExternalStorageDirectory(),fich + "_" + datosUser[0] +"_"+ datosUser[1] +".pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        }catch (Exception e){
            e.printStackTrace();
        }

        // Cerrar documento
        pdfDocument.close();
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

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        // Comprobar que el código de la petición de los permisos sea el de lectura y escritura
        if(requestCode == 200){
            // Comprobar que se ha recibido la respuesta a las 2 peticiones
            if (grantResults.length >0){
                // Comprobar si se han concedido los permisos
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if(writeStorage && readStorage){    // Si se han concedido ambos permisos
                    // Informar de que se han concedido los permisos
                    Toast.makeText(this,"Permiso concedido",Toast.LENGTH_LONG).show();
                } else {    // Si se ha rechazado alguno de los permisos
                    // Informar de que se han denegado los permisos
                    Toast.makeText(this,"Permiso denegado",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

// _________________________________________________________________________________________________

/*  Método onSaveInstanceState:
    ------------------------
        *) Parámetros (Input):
                1) (Bundle) outState: Contiene el diseño predeterminado del Activity.
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