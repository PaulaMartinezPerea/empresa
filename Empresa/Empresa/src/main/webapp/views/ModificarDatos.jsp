<html>
<link rel="stylesheet" href="css/styles.css">
<body>
	<h2>Modificar datos de un empleado</h2>

	<form action="empresa" method="get">
		<input type="hidden" name="accion" value="modificar"> <input
			type="hidden" name="op" value="guardar"> DNI: <input
			type="text" name="dni" required><br> Nombre: <input
			type="text" name="nombre" required><br> Sexo (M/F): <input
			type="text" name="sexo" maxlength="1" required><br>
		Categoría: <input type="text" name="categoria" required><br>
		Años trabajados: <input type="number" name="anyos" required><br>
		<!-- <input type="submit" value="Guardar cambios"> -->
		<a href="ModificarDatos.jsp"><button>Guardar cambios</button></a>
	</form>

	<%
	String mensaje = (String) request.getAttribute("mensaje");
	if (mensaje != null) {
	%>
	<p style="color: green;"><%=mensaje%></p>
	<%
	}
	%>

	<a href="pagina.jsp">Volver</a>
</body>
</html>
