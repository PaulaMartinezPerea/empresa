<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Empleado" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Empleados</title>
    <link rel="stylesheet" type="text/css" href="./css/mostrarEmpleados.css">
</head>
<body>
    <h1>Todos los empleados</h1>
    <table border="1">
        <thead>
            <tr>
                <th>Nombre</th>
                <th>DNI</th>
                <th>Sexo</th>
                <th>Categoría</th>
                <th>Años Trabajados</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Empleado> empleados = (List<Empleado>) request.getAttribute("empresa");
                if (empleados != null && !empleados.isEmpty()) {
                    for (Empleado emp : empleados) {
            %>
            <tr>
                <td><%= emp.getNombre() %></td>
                <td><%= emp.getDni() %></td>
                <td><%= emp.getSexo() %></td>
                <td><%= emp.getCategoria() %></td>
                <td><%= emp.getAnyos() %></td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="5">No hay empleados para mostrar.</td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>
    <a href="pagina.jsp">Volver</a>
</body>
</html>
