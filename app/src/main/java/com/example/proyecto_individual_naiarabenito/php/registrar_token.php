<?php
	include 'conexion.php';

	$token = $_POST["token"];
	$email = $_POST["email"];

	$consulta = "UPDATE t_usuarios SET token='$token' WHERE email='$email'";
	$result = $con->query($consulta);
	
	if($result){
		echo "{'done':'true'}";
	} else{
		echo "{'done':'false'}";
	}

	$con->close();
?>