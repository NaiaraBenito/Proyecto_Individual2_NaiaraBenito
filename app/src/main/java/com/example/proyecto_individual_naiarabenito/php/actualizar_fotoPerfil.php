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
		
		echo $SERVER_URL;
		file_put_contents($imagePath,base64_decode($foto));

		$con->close();
	}
?>