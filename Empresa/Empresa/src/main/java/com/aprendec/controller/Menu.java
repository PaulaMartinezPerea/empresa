package com.aprendec.controller;

import com.aprendec.dao.EmpleadoDAO;
import com.aprendec.model.Empleado;
import java.io.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/empresa")
public class Menu extends HttpServlet {
	 private static final long serialVersionUID = 1L;
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        EmpleadoDAO dao = new EmpleadoDAO();

        try {
            if ("listar".equals(accion)) {
                List<Empleado> lista = dao.listarEmpleados();
                request.setAttribute("empleados", lista);
                request.getRequestDispatcher("views/MostrarEmpleados.jsp").forward(request, response);

            } else if ("sueldo".equals(accion)) {
                String dni = request.getParameter("dni");
                if (dni != null) {
                    double sueldo = dao.buscarSalarioPorDni(dni);
                    request.setAttribute("dni", dni);
                    request.setAttribute("sueldo", sueldo);
                }
                request.getRequestDispatcher("views/MostrarSalario.jsp").forward(request, response);

            } else if ("modificar".equals(accion)) {
                if ("guardar".equals(request.getParameter("op"))) {
                    Empleado e = new Empleado();
                    e.setDni(request.getParameter("dni"));
                    e.setNombre(request.getParameter("nombre"));
                    e.setSexo(request.getParameter("sexo").charAt(0));
                    e.setCategoria(Integer.parseInt(request.getParameter("categoria")));
                    e.setAniosTrabajados(Integer.parseInt(request.getParameter("anyos")));
                    dao.actualizarEmpleado(e);
                   
                }
                request.getRequestDispatcher("views/ModificarDatos.jsp").forward(request, response);

            } else {
                response.sendRedirect("pagina.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("exception", e);
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
