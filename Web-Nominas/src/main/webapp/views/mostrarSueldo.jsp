<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sueldo</title>
<link rel="stylesheet" href="../css/mostrarSueldo.css">
</head>
<body>
	<h2>Sueldo del empleado</h2>
	<p>
		DNI: <strong><%= request.getAttribute("dni") %></strong>
	</p>
	<p>
		Sueldo: <strong><%= request.getAttribute("sueldo") %> €</strong>
	</p>
	<br>
	<form action="<%= request.getContextPath() %>/pagina.jsp" method="get">
		<input type="submit" value="Volver">
	</form>
</body>
</html>
