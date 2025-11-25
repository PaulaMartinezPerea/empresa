package com.aprendec.controller.commands;

import javax.servlet.http.*;
import com.aprendec.dao.EmpleadoDAO;

public class MostrarSueldoCommand implements Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, EmpleadoDAO dao) throws Exception {
        String dni = request.getParameter("dni");
        if (dni != null) {
            request.setAttribute("dni", dni);
            request.setAttribute("sueldo", dao.buscarSalarioPorDni(dni));
        }
        request.getRequestDispatcher("views/MostrarSalario.jsp").forward(request, response);
    }
}
