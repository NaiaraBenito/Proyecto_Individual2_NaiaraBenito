<?php
	include 'conexion.php';
	
	$id=$_POST['id'];
	$email=$_POST['email'];
	
	switch($id){
		case "login":
			$password=$_POST['password'];
			
			$consulta = "SELECT * FROM t_usuarios WHERE email='$email'";
			$result = mysqli_query($con, $consulta);
			if($result){
				#Acceder al resultado
				$fila = mysqli_fetch_row($result);
				$hash = $fila[3];
				$exist = password_verify($password, $hash);
				if($exist){
					# Generar el array con los resultados con la forma Atributo - Valor
					$arrayresultados = array(
						'exist' => 'true',
						'nombre' => $fila[0],
						'apellido' => $fila[1],
						'email' => $fila[2],
						'foto' => $fila[4]
					);
					#Devolver el resultado en formato JSON
					echo json_encode($arrayresultados);
				} else{
					echo '{"exist":"false"}';
				}
			} else{
					echo "No existe";
			}
			break;
		case "registro":
			$consulta = "SELECT * FROM t_usuarios WHERE email='$email'";
			$result = mysqli_query($con, $consulta);

			if($result){
				#Acceder al resultado
				$fila = mysqli_fetch_row($result);
				if(isset($fila[0])){
					echo '{"exist":"true"}';
				} else{
					echo '{"exist":"false"}';
				}
			} else{
				echo "No existe";
			}
			break;
	}

	$con->close();	
?>