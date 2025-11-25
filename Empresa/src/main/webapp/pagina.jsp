<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<meta charset="UTF-8">
<head>
<title>Gestión de Empleados</title>
<link rel="stylesheet" href="css/styles.css">
<style>
.menu {
    list-style: none;
    padding: 0;
    margin: 40px auto;
    display: flex;
    justify-content: center;
    gap: 20px;
}

.menu li {
    display: inline-block;
}

.menu a {
    display: inline-block;
    text-decoration: none;
    color: #a774c7; /*Lila pastel*/
    background-color: #f8f0ff; /*Fondo lila muy claro*/
    border: 2px solid #c8b6ff; /*Borde pastel*/
    padding: 12px 25px;
    border-radius: 12px;
    font-weight: 600;
    font-family: 'Comic Sans MS', Helvetica, Arial, sans-serif;
    transition: all 0.3s ease;
}

.menu a:hover {
    background-color: #c8b6ff; /*Lila pastel más intenso*/
    color: white;
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(200, 182, 255, 0.5);
}
</style>
</head>

<body>
	<div class="container">
		<h1>GESTIÓN DE EMPLEADOS</h1>

		<ul class="menu">
			<li><a href="empresa?accion=listar">Mostrar Empleados</a></li>
			<li><a href="empresa?accion=sueldo">Mostrar Salario por DNI</a></li>
			<li><a href="empresa?accion=modificar">Modificar Datos</a></li>
		</ul>
	</div>
</body>
</html>
