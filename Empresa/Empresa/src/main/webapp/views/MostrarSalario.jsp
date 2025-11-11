<html>
<meta charset="UTF-8">
<link rel="stylesheet" href="css/styles.css">
<body>
	<h2>CONSULTAR SALARIO DE UN EMPLEADO</h2>

	<form action="empresa" method="get">
		<input type="hidden" name="accion" value="sueldo"> DNI: 
		<input  type="text" name="dni" required> <input type="submit" value="Buscar">
	</form>

	<%
	String dni = (String) request.getAttribute("dni");
	Double sueldo = (Double) request.getAttribute("sueldo");

	if (dni != null) {
	%>
	
	<h3>Resultado</h3>
	<p>DNI: <b><%=dni%></b></p>
	
	<%
	if (sueldo != null && sueldo > 0) {
	%>
	
	<p>Salario: <b><%=sueldo%></b> euros</p>
	
	<%
	} else {
	%>
	
	<p style="color: red;">No se encontro ese empleado.</p>
	
	<%
	}
	%>
	<%
	}
	%>

	<a href="pagina.jsp" class="btn-volver">Volver</a>
</body>
</html>

