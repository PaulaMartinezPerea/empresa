<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
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
	color: #1e3a8a;
	background-color: #f1f5f9;
	border: 2px solid #1e3a8a;
	padding: 12px 25px;
	border-radius: 10px;
	font-weight: 600;
	transition: all 0.3s ease;
}

.menu a:hover {
	background-color: #1e3a8a;
	color: white;
	transform: translateY(-2px);
	box-shadow: 0 3px 6px rgba(0, 0, 0, 0.2);
}
</style>
</head>

<body>
	<div class="container">
		<h1>Gestión de Empleados</h1>

		<ul class="menu">
			<li><a href="empresa?accion=listar">Mostrar Empleados</a></li>
			<li><a href="empresa?accion=sueldo">Mostrar Salario por DNI</a></li>
			<li><a href="empresa?accion=modificar">Modificar Datos</a></li>
		</ul>
	</div>
</body>
</html>
