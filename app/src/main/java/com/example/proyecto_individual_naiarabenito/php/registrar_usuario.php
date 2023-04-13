<?php
	include 'conexion.php';

	$nombre = $_POST["nombre"];
    $apellido = $_POST["apellido"];
    $email = $_POST["email"];
    $password = $_POST["password"];
	$foto = $_POST["foto"];
	
	$hash = password_hash($password,PASSWORD_DEFAULT);
	
	$consulta = "INSERT INTO t_usuarios (nombre,apellido,email,password,foto) VALUES('$nombre','$apellido','$email','$hash','$foto')";
    $result = $con->query($consulta);
    if($result){
        echo "Datos insertados";
    } else{
        echo "No se pudo insertar";
    }
	$con->close();
?>