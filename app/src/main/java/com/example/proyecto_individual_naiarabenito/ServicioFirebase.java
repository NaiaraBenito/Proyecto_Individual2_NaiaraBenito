package com.example.proyecto_individual_naiarabenito;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import static android.content.ContentValues.TAG;
import java.util.Map;

public class ServicioFirebase extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        if(message.getData() != null){
            enviarNotificacion(message);
        }
        if(message.getNotification() != null){
            Log.d(TAG, "Body notification: " + message.getNotification().getBody());
            enviarNotificacion(message);
        }
    }

    private void enviarNotificacion(RemoteMessage remoteMessage){
        Map<String,String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("body");

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "fcm-message";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // Solo para android Oreo o superior
            @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Mi notificacion", NotificationManager.IMPORTANCE_MAX);
            // Configuración del canal de notificación
            channel.setDescription("fcm-message channel para app");
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.setVibrationPattern(new long[]{0,1000,500,1000});
            channel.enableVibration(true);

            manager.createNotificationChannel(channel);
        }

        Intent i = new Intent (this, Launch_Screen.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.fcm_icon)
                .setTicker("FCM")
                .setContentTitle(title)
                .setContentText(body)
                .setVibrate(new long[]{0,1000,500,1000})
                .setContentIntent(pendingIntent)
                .setContentInfo("info");

        manager.notify(1,builder.build());

    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG,"Refreshed Token " + token);
        FirebaseMessaging.getInstance().subscribeToTopic("dispositivos");
        //enviarTokenToServer(token, "n@n.com");
    }

    /*public void enviarTokenToServer(final String token, String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/nbenito012/WEB/registrar_token.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TOKEN",response.toString());
                Toast.makeText(getApplicationContext(),"Se registro correctamente", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Imprimir estado del registro
                String msg = getResources().getString(R.string.t_errorBBDD);
                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("token", token);
                parametros.put("email", email);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/
}
