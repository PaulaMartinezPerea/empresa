<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Consultar sueldo de empleado</title>
<link rel="stylesheet" type="text/css" href="./css/consultarSueldo.css">
</head>
<body>
    <h1>Consultar sueldo de un empleado</h1>
    
    <form action="empresa" method="post">
        <input type="hidden" name="opcion" value="verSueldo">
        <label for="dni">DNI del empleado:</label>
        <input type="text" id="dni" name="dni" required>
        <button type="submit">Buscar sueldo</button>
    </form>

    <br>
    <div>
        <%
            String mensaje = (String) request.getAttribute("mensaje");
            String error = (String) request.getAttribute("error");
            if (mensaje != null) {
        %>
            <p style="color:green;"><%= mensaje %></p>
        <% } else if (error != null) { %>
            <p style="color:red;"><%= error %></p>
        <% } %>
    </div>

    <br>
    <a href="pagina.jsp">Volver</a>
</body>
</html>
