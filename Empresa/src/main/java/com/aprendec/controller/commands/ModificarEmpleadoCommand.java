package com.aprendec.controller.commands;

import javax.servlet.http.*;
import com.aprendec.dao.EmpleadoDAO;
import com.aprendec.model.Empleado;

public class ModificarEmpleadoCommand implements Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, EmpleadoDAO dao) throws Exception {

        if ("guardar".equals(request.getParameter("op"))) {
            Empleado e = new Empleado(
                request.getParameter("dni"),
                request.getParameter("nombre"),
                request.getParameter("sexo").charAt(0),
                Integer.parseInt(request.getParameter("categoria")),
                Integer.parseInt(request.getParameter("anyos"))
            );

            dao.actualizarEmpleado(e);
            request.setAttribute("mensaje", "Empleado actualizado correctamente");
        }

        request.getRequestDispatcher("views/ModificarDatos.jsp").forward(request, response);
    }
}
