package com.example.proyecto_individual_naiarabenito.ui.cesta;


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

import com.example.proyecto_individual_naiarabenito.Menu_Principal;
import com.example.proyecto_individual_naiarabenito.R;

import java.io.File;
import java.io.FileOutputStream;

public class SiActNotificacion extends AppCompatActivity {

    String[] datosUser;
    String tituloPDF;
    String descripcionPDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_si_act_notificacion);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            // Obtener los datos del usuario para mantener la sesión
            datosUser = new String[3];
            datosUser[0] = extras.getString("nombreUsuario");
            datosUser[1] = extras.getString("apellidoUsuario");
            datosUser[2] = extras.getString("emailUsuario");
            tituloPDF = extras.getString("tituloFactura");
            descripcionPDF = extras.getString("descripcionFactura");

            crearPDF();

            // Crear el intent que redirige la ejecución al Menú Principal
            Intent intent = new Intent(this, Menu_Principal.class);

            // Pasarle la información del usuario (mantener la sesión)
            intent.putExtra("nombreUsuario", datosUser[0]);
            intent.putExtra("apellidoUsuario", datosUser[1]);
            intent.putExtra("emailUsuario", datosUser[2]);
            // Para hacer que la notificación desaparezca al ejecutar la actividad
            // Crear un manager de notificaciones
            NotificationManagerCompat nmc = NotificationManagerCompat.from(getApplicationContext());
            // Cancelar la notificación, pasándole el NOTIFICATION_ID
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
}