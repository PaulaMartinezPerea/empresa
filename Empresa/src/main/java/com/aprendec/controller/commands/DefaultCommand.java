package com.aprendec.controller.commands;

import javax.servlet.http.*;
import com.aprendec.dao.EmpleadoDAO;

public class DefaultCommand implements Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, EmpleadoDAO dao) throws Exception {
        response.sendRedirect("pagina.jsp");
    }
}
