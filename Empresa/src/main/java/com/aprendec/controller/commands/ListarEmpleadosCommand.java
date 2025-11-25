package com.aprendec.controller.commands;

import javax.servlet.http.*;
import com.aprendec.dao.EmpleadoDAO;

public class ListarEmpleadosCommand implements Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, EmpleadoDAO dao) throws Exception {
        request.setAttribute("empleados", dao.listarEmpleados());
        request.getRequestDispatcher("views/MostrarEmpleados.jsp").forward(request, response);
    }
}
