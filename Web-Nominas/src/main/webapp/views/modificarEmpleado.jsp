<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Empleado" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Modificar empleado</title>
<link rel="stylesheet" type="text/css" href="./css/modificarEmpleado.css">
</head>
<body>
    <h1>MODIFICAR EMPLEADO</h1>

    <%
        Empleado emp = (Empleado) request.getAttribute("empleado");
        String error = (String) request.getAttribute("error");
        String mensaje = (String) request.getAttribute("mensaje");

        if (error != null) {
    %>
        <p style="color:red;"><%= error %></p>
    <% } %>

    <% if (mensaje != null) { %>
        <p style="color:green;"><%= mensaje %></p>
    <% } %>

    <% if (emp != null) { %>
    <form action="empresa" method="post">
        <input type="hidden" name="opcion" value="modificar">
        <input type="hidden" name="dni" value="<%= emp.getDni() %>">

        <label for="nombre">Nombre:</label><br>
        <input type="text" id="nombre" name="nombre" value="<%= emp.getNombre() %>" required><br><br>

        <label for="genero">Sexo:</label><br>
        <select id="genero" name="genero" required>
            <option value="M" <%= "M".equals(emp.getSexo()) ? "selected" : "" %>>Masculino</option>
            <option value="F" <%= "F".equals(emp.getSexo()) ? "selected" : "" %>>Femenino</option>
        </select><br><br>

        <label for="categoria">Categoría:</label><br>
        <input type="number" id="categoria" name="categoria" value="<%= emp.getCategoria() %>" min="1" required><br><br>

        <label for="anyos_trabajados">Años trabajados:</label><br>
        <input type="number" id="anyos_trabajados" name="anyos_trabajados" value="<%= emp.getAnyos() %>" min="0" required><br><br>

        <button type="submit">Guardar cambios</button>
    </form>
    <% } else { %>
        <p>No se encontró el empleado.</p>
        <a href="empresa?opcion=modificar">Buscar otro empleado</a>
    <% } %>

    <br><br>
    <a href="pagina.jsp">Volver</a>
</body>
</html>
