<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/buscarEmpleadoModificar.css">
<title>Buscar por DNI</title>
</head>
<body>
    <h1>Buscar Empleado para Modificar</h1>

    <%-- Mostrar mensajes de error si los hay --%>
    <c:if test="${not empty error}">
        <p style="color:red;">${error}</p>
    </c:if>

    <form action="empresa" method="get">
        <input type="hidden" name="opcion" value="modificar" />
        <label for="dni">Introduce DNI del empleado:</label>
        <input type="text" id="dni" name="dni" required />
        <button type="submit">Buscar</button>
    </form>

    <p><a href="pagina.jsp">Volver</a></p>
</body>
</html>