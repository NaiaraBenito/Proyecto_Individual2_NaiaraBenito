<?php
	include 'conexion.php';
	
	$email = $_POST["email"];
	$foto = $_POST["foto"];
	
	$consulta = "SELECT email FROM t_usuarios WHERE email='$email'";
	$result = $con->query($consulta);
	if($result){
		$fila = mysqli_fetch_row($result);
		$id = $fila[0];
		$imagePath = "upload/$id.jpg";
		
		$SERVER_URL = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/nbenito012/WEB/$imagePath";
		$consulta2 = "UPDATE t_usuarios SET foto='$SERVER_URL' WHERE email='$email'";
		$result2 = $con->query($consulta2);
		if($result2 === TRUE){
			echo $SERVER_URL;
			file_put_contents($imagePath,base64_decode($foto));
			//echo "listo";
		} else{
			echo "No se pudo insertar";
		}
		$con->close();
	}
?>