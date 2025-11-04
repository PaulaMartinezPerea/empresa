<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Error - Empresa</title>
<link rel="stylesheet" href="css/styles.css">
</head>
<body>
	<div class="container">
		<h2>⚠️ Ha ocurrido un error ⚠️</h2>
		<p class="error">Lo sentimos, se ha producido un problema al
			procesar tu solicitud.</p>
		<%
      if (exception != null) {
    %>
		<h3>Detalles técnicos:</h3>
		<p><%= exception.getClass().getName() %>
			-
			<%= exception.getMessage() %></p>
		<%
      }
    %>

		<a href="pagina.jsp" class="btn-volver">Volver</a>
	</div>
</body>
</html>
