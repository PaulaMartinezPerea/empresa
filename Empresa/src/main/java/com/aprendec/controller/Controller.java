package com.aprendec.controller;

import com.aprendec.controller.commands.Action;
import com.aprendec.dao.EmpleadoDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/empresa")
public class Controller extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        String accion = request.getParameter("accion");

        try {
            Action comando = CommandFactory.getAction(accion);
            comando.execute(request, response, EmpleadoDAO.getInstance());

        } catch (Exception e) {
            request.setAttribute("exception", e);
            try {
                request.getRequestDispatcher("error.jsp").forward(request, response);
            } catch (Exception ignored) {}
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doGet(req, resp);
    }
}
