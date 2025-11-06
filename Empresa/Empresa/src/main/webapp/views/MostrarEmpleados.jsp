<%@ page import="java.util.*, com.aprendec.model.Empleado"%>
<html>
<meta charset="ISO-8859-1">
<link rel="stylesheet" href="css/styles.css">
<head>
<title>Mostrar Empleados</title>
</head>
<body>
	<h2>LISTA DE EMPLEADOS</h2>

	<table border="1">
		<tr>
			<th>DNI</th>
			<th>Nombre</th>
			<th>Sexo</th>
			<th>Categoría</th>
			<th>Años Trabajados</th>
		</tr>

<%
        List<Empleado> lista = (List<Empleado>) request.getAttribute("empleados");
  
		if (lista != null) {
			for (Empleado e : lista) {
        %>
        
		<tr>
			<td><%= e.getDni() %></td>
			<td><%= e.getNombre() %></td>
			<td><%= e.getSexo() %></td>
			<td><%= e.getCategoria() %></td>
			<td><%= e.getAniosTrabajados() %></td>
		</tr>
		
		<%
    }
  }
%>
	</table>

	<a href="pagina.jsp">Volver</a>
</body>
</html>


