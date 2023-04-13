<?php
	include 'conexion.php';
    
    function sendPushNotification($to = '', $data = array()) {
        
        $apiKey = 'AAAAyUCO8-k:APA91bErKp8ZOVdpkb9D1cV_QfmmjM_94SP9uwnCbDhV7hKfwzECySHBQeZSudkVPM4Hy-jI59j-zymZiKmR_VUdX7MHiWTK4emcSbba7vEJKICdEuBg-YKlyxjIaGjXXcuAoQHZq92J';
        
        $fields = array(
                        'to' => $to,
                        'data' => $data,
                        );
        
        $headers = array('Authorization: key='.$apiKey, 'Content-Type: application/json');
        
        $url = 'https://fcm.googleapis.com/fcm/send';
        
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
        
        echo json_encode($fields);
        echo "<br><br>RESPUESTA SERVIDOR: ";
        
        $result = curl_exec($ch);
        
        curl_close($ch);
        
        return json_decode($result, true);
    }
    
    // DATOS DE  LOS DESTINATARIOS
    $to = "/topics/dispositivos";
    
    // DATOS DE LA NOTIFICACION
    $data = array(
		'title' => '¡Dale un Mordisco!',
        'body' => 'Tenemos nuevas existencias, ¡Pidelos ya!',
	);
    
    print_r(sendPushNotification($to,  $data));

?>