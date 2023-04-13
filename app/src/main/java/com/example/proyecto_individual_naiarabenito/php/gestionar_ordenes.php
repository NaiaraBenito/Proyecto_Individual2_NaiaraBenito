<?php
	include 'conexion.php';
	
	$operacion=$_POST['operacion'];

	$email=$_POST['email'];

	switch($operacion){
		case "obtener":
			$consulta = "SELECT * FROM t_orden WHERE emailUsuario='$email'";
			$result = mysqli_query($con, $consulta);

			if($result) {
				$fila = mysqli_fetch_row($result);
				if(isset($fila[0])){
					$vuelta = '{"exist":"true","orders":';
					$arrayresultados = array();
					do{
						$aux = array(
							'id' => $fila[0],
							'nombreProd' => $fila[1],
							'precioProd' => $fila[2],
							'imagenProd' => $fila[3],
							'cantidadProd' => $fila[4],
							'emailUsuario' => $fila[5]
						);
						array_push($arrayresultados,$aux);
					}while($fila = mysqli_fetch_row($result));

					$p = json_encode($arrayresultados);
					$vuelta = $vuelta.$p."}";
					echo $vuelta;

				} else{
					echo '{"exist":"false"}';
				}
			} else{
				echo '{"exist":"false"}';
			}
			break;

		case "añadir":
			$nombre=$_POST['nombre'];
			$cantidad=$_POST['cantidad'];

			$consulta = "SELECT cantidadProd FROM t_orden WHERE nombreProd='$nombre' AND emailUsuario='$email'";
			$result = mysqli_query($con, $consulta);

			if($result){
				#Acceder al resultado
				$fila = mysqli_fetch_row($result);
				if(isset($fila[0])){
					$cantidadAux=$fila[0] + $cantidad;
					
					$consulta2 = "UPDATE t_orden SET cantidadProd='$cantidadAux' WHERE nombreProd='$nombre' AND emailUsuario='$email'";
					$result = $con->query($consulta2);
					if($result){
						echo '{"done":"true"}';
					} else{
						echo '{"done":"false"}';
					}
				} else{
					$precio=$_POST['precio'];
					$imagen=$_POST['imagen'];
					
					$consulta2 = "INSERT INTO t_orden (nombreProd,precioProd,imagenProd,cantidadProd,emailUsuario) VALUES ('$nombre','$precio','$imagen','$cantidad','$email')";
					$result = $con->query($consulta2);
					if($result){
						echo '{"done":"true"}';
					} else{
						echo '{"done":"false"}';
					}
				}
			} else{
				echo '{"done":"false"}';
			}
			break;
			
		case "eliminar":
			$nombre=$_POST['nombre'];
			
			$consulta = "DELETE FROM t_orden WHERE nombreProd='$nombre' AND emailUsuario='$email'";
			$result = mysqli_query($con, $consulta);

			if($result){
				echo '{"done":"true"}';
			} else{
				echo '{"done":"false"}';
			}
			break;
	}

	$con->close();	
?>